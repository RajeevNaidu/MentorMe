package com.codepath.wwcmentorme.data;

import java.util.List;

import android.content.Context;

import com.codepath.wwcmentorme.models.User;
import com.parse.*;

public class ChatDataService 
{
	static String applicationid="Arp8EWbcNsGwgLeHg5x7bqYqA869hXPLLtRNjBNm";
	static String clientkey="53cpqVPyEjahm6dAma3qRUYBvVe2OgysOUPp3JBw";
	ChatDataService(Context context)
	{
		Parse.initialize(context, applicationid, clientkey);
		//register here with parse if doesn't
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MentorMe");
		User me=User.me();
		long mefacebookID=me.getFacebookId();
		query.whereEqualTo("receiverid", mefacebookID);
		query.findInBackground(new FindCallback<ParseObject>() 
		{
		    @Override
			public void done(List<ParseObject> arg0, ParseException arg1)
	    	{
				// TODO Auto-generated method stub
		    	
		    		
				
			}
		});
		
	}
	public void registerWithParse()
	{
		ParseObject gameScore = new ParseObject("MentorMe");
		gameScore.put("senderid", 1337);
		gameScore.put("playerName", "Sean Plott");
		gameScore.put("cheatMode", false);
		gameScore.saveEventually();	
	}
	public String getNewMessageFromId(String receiverId,String senderId)
	{
		String message="";
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MentorMe");
		User me=User.me();
		long mefacebookID=me.getFacebookId();
		query.whereEqualTo("receiverid", mefacebookID);
		query.findInBackground(new FindCallback<ParseObject>() 
		{
		    @Override
			public void done(List<ParseObject> arg0, ParseException arg1)
	    	{
				// TODO Auto-generated method stub
				
			}
		});
		
		return message;
	}
    public boolean sendNewMessageToId(String receiverId,String senderId,String message)
    {
    	boolean flag=false;
    	
    	return flag;
    }
    
}
