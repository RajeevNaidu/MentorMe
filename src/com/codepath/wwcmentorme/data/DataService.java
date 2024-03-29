package com.codepath.wwcmentorme.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.content.Context;

import com.codepath.wwcmentorme.helpers.Async;
import com.codepath.wwcmentorme.models.Message;
import com.codepath.wwcmentorme.models.Rating;
import com.codepath.wwcmentorme.models.Request;
import com.codepath.wwcmentorme.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class DataService {
	
	public static void getMentors(Context context, ParseGeoPoint geoPoint, Double distance, String skill, FindCallback<User> callback) {
		ParseQuery<User> query = User.getQuery();		
		query.whereEqualTo(User.IS_MENTOR_KEY, true);
		if(geoPoint != null) {
			query.whereWithinMiles(User.LOCATION_KEY, geoPoint, distance);
		}
		if(skill != null) {
			ArrayList<String> names = new ArrayList<String>();
			if(!skill.equals("All")){
				names.add(skill);
				query.whereContainsAll(User.MENTOR_SKILLS_KEY, names);
			}			
		}
		query.findInBackground(callback);
	}
	
	public static void getMentees(final long userId, final Runnable completion) {
		getConnections(userId, completion, true);
	}
	
	public static void getMentees(final ArrayList<Long> userIds, final Runnable completion) {
		getConnections(userIds, completion, true);
	}
	
	public static void getMentors(final long userId, final Runnable completion) {
		getConnections(userId, completion, false);
	}
	
	public static void getMentors(final ArrayList<Long> userIds, final Runnable completion) {
		getConnections(userIds, completion, false);
	}
	
	public static void getConnections(final long userId, final Runnable completion, final boolean incoming) {
		final ArrayList<Long> userIds = new ArrayList<Long>();
		userIds.add(userId);
		getConnections(userIds, completion, incoming);
	}
	
	public static void getConnections(final ArrayList<Long> userIds, final Runnable completion, final boolean incoming)
	{
		ParseQuery<Request> query = Request.getQuery();
		final String pKey = incoming ? Request.MENTOR_ID_KEY : Request.MENTEE_ID_KEY;
		final String fKey = incoming ? Request.MENTEE_ID_KEY : Request.MENTOR_ID_KEY;
		query.whereContainedIn(pKey, userIds);
		query.findInBackground(new FindCallback<Request>() {
			@Override
			public void done(final List<Request> requests, ParseException e1) {
				if (e1 == null) {
					ArrayList<Long> userIds = new ArrayList<Long>();
					for (Request request : requests) {
						if (User.getUser(request.getLong(fKey)) == null) {
							userIds.add(request.getLong(fKey));
						}
					}
					final Runnable processUsers = new Runnable() {
						@Override
						public void run() {
							for (final Request request : requests) {
								final User primary = User.getUser(request.getLong(pKey));
								final User secondary = User.getUser(request.getLong(fKey));
								User.addSortedByMenteeCountInIds(primary.getConnections(incoming), secondary.getFacebookId());
							}
							if (completion != null) {
								completion.run();
							}
						}
					};
					if (userIds.size() > 0) {
						DataService.getUsers(userIds, new FindCallback<User>() {
							@Override
							public void done(List<User> users, ParseException e2) {
								if (e2 == null) {
									User.saveAllUsers(users);
									processUsers.run();
								} else {

								}								
							}
						});
					} else {
						processUsers.run();
					}
				}
			}
		});
	}
	
	public static void getUser(long userId, GetCallback<User> callback) 
	{
		ParseQuery<User> query = User.getQuery();
		query.whereEqualTo(User.FACEBOOK_ID_KEY, userId);
		query.getFirstInBackground(callback);
	}
	
	public static void getUsers(ArrayList<Long> userIds, FindCallback<User> callback) {
		ParseQuery<User> query = User.getQuery();
		query.whereContainedIn(User.FACEBOOK_ID_KEY, userIds);
		query.findInBackground(callback);
	}
	
	public static void getAverageRating(long userId, FindCallback<Rating> callback) {
		ParseQuery<Rating> query = Rating.getQuery();
		query.whereEqualTo(Rating.RATED_FACEBOOK_ID_KEY, userId);
		query.findInBackground(callback);
	}
	
	public static void getRatingByUser(long currentUserId, long ratedUserId, GetCallback<Rating> callback) {
		ParseQuery<Rating> query = Rating.getQuery();
		query.whereEqualTo(Rating.FACEBOOK_ID_KEY, currentUserId);
		query.whereEqualTo(Rating.RATED_FACEBOOK_ID_KEY, ratedUserId);
		query.getFirstInBackground(callback);
	}
	
	public static void putRating(final Rating ratingIn, final long userId, final float value, final Async.Block<Boolean> completion) {
		Rating rating = null;
		if (ratingIn == null) {
			rating = new Rating();
			rating.put(Rating.FACEBOOK_ID_KEY, User.meId());
			rating.put(Rating.RATED_FACEBOOK_ID_KEY, userId);
		} else {
			rating = ratingIn;
		}
		rating.put(Rating.RATING_KEY, value);
		rating.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (completion != null) {
					completion.call(e == null);
				}
			}
		});
	}
	
	public static void upsertRequest(final long menteeId, final long mentorId, final boolean accepted, final Async.Block<Boolean> completion) {
		ParseQuery<Request> query = Request.getQuery();
		query.whereEqualTo(Request.MENTEE_ID_KEY, menteeId);
		query.whereEqualTo(Request.MENTOR_ID_KEY, mentorId);
		query.getFirstInBackground(new GetCallback<Request>() {
			@Override
			public void done(Request request, ParseException e) {
				if (e != null || request == null) {
					request = new Request();
					request.put(Request.MENTEE_ID_KEY, menteeId);
					request.put(Request.MENTOR_ID_KEY, mentorId);
				}
				request.put(Request.ACCEPTED_KEY, accepted);
				request.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (completion != null) {
							completion.call(e == null);
						}				
					}
				});
			}
		});
	}
	
	private static HashSet<Long> sResponsesPending = new HashSet<Long>();
	
	public static boolean isResponsePending(final long userId) {
		return sResponsesPending.contains(userId);
	}
	
	public static void removeResponsePending(final long userId) {
		sResponsesPending.remove(userId);
	}
	
	public static void addResponsePending(final long userId) {
		sResponsesPending.add(userId);
	}
	
	private static HashSet<Long> sRequestsSent = new HashSet<Long>();
	
	public static boolean isRequestsSent(final long userId) {
		return sRequestsSent.contains(userId);
	}
	
	public static void removeRequestsSent(final long userId) {
		sRequestsSent.remove(userId);
	}
	
	public static void addRequestsSent(final long userId) {
		sRequestsSent.add(userId);
	}
	
	public static void getMessages(final long userId1, final long userId2, final int numMessages, final Date createdMin, final Date createdMax,
			final Async.Block<List<Message>> completion) {
		ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
		query.whereEqualTo(Message.GROUP_ID_KEY, Message.getGroup(userId1, userId2));
		if (createdMin != null) {
			query.whereGreaterThanOrEqualTo(Message.CREATED_AT_KEY, createdMin);
		}
		if (createdMax != null) {
			query.whereLessThanOrEqualTo(Message.CREATED_AT_KEY, createdMax);
		}
		query.addDescendingOrder(Message.CREATED_AT_KEY);
		query.findInBackground(new FindCallback<Message>() {
			@Override
			public void done(final List<Message> messages, ParseException e) {
				if (completion != null) {
					completion.call(e == null ? messages : null);
				}
				if (e != null) {
					e.printStackTrace();
				}
			}
		});
	}
}
