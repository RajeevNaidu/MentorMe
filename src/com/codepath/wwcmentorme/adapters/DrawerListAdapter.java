package com.codepath.wwcmentorme.adapters;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.wwcmentorme.R;
import com.codepath.wwcmentorme.activities.EditProfileActivity;
import com.codepath.wwcmentorme.activities.UserListActivity;
import com.codepath.wwcmentorme.helpers.Async;
import com.codepath.wwcmentorme.helpers.Constants.Persona;
import com.codepath.wwcmentorme.helpers.Constants.UserDisplayMode;
import com.codepath.wwcmentorme.helpers.UIUtils;
import com.codepath.wwcmentorme.helpers.ViewHolder;
import com.codepath.wwcmentorme.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DrawerListAdapter extends
		ArrayAdapter<DrawerListAdapter.DrawerItem> 
{

	public static class DrawerItem 
	{
		public int stringId;
		public int iconId;

		public DrawerItem(final int stringId, final int iconId)
		{
			this.stringId = stringId;
			this.iconId = iconId;
		}
	}

	public DrawerListAdapter(Context context)
	{
		super(context, 0);
	}

	public View getHeaderView()
	{
		final User user = User.me();
		if (user == null)
		{
			final View view = UIUtils.getLoginView(getContext());
			final Button loginButton = (Button) view.findViewById(R.id.loginButton);
			loginButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UIUtils.login((Activity)getContext(), null, Persona.BOTH, new Async.Block<User>() {
						@Override
						public void call(User result) {
							
						}
						
					}, true);
				}
			});
			return view;
		}
		LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflator.inflate(R.layout.user_header, null);
		final ViewHolder.UserItem holder = new ViewHolder.UserItem();
		holder.ivMentorProfile = (ImageView) view
				.findViewById(R.id.ivMentorProfile);
		holder.tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
		holder.tvLastName = (TextView) view.findViewById(R.id.tvLastName);
		holder.tvPosition = (TextView) view.findViewById(R.id.tvPosition);
		try
		{
			populate(holder, user);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(getItem(position), convertView, parent);
	}

	private View getView(final DrawerItem item, View convertView,
			ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.drawer_list_item, null);
			final ViewHolder.DrawerItem holder = new ViewHolder.DrawerItem();
			holder.btnItem = (Button) view.findViewById(R.id.btnDrawerItem);
			view.setTag(holder);
		}
		final ViewHolder.DrawerItem holder = (ViewHolder.DrawerItem) view
				.getTag();
		holder.btnItem.setCompoundDrawablesWithIntrinsicBounds(item.iconId, 0,
				0, 0);
		holder.btnItem.setText(item.stringId);
		
		holder.btnItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btnItem = (Button) v;				
				String buttonText = btnItem.getText().toString();
				if (buttonText == getContext().getResources().getString(R.string.drawer_requests_received)) {
					final Intent intent = new Intent(getContext(), UserListActivity.class);
					intent.putExtra("userId", User.meId());
					intent.putExtra("persona", Persona.MENTOR);
					getContext().startActivity(intent);
				} else if (buttonText == getContext().getResources().getString(R.string.drawer_requests_Sent)) {
					final Intent intent = new Intent(getContext(), UserListActivity.class);
					intent.putExtra("persona", Persona.MENTEE);
					intent.putExtra("userId", User.meId());
					getContext().startActivity(intent);
				} else if (buttonText == getContext().getResources().getString(R.string.drawer_edit_profile)) {
					final Intent intent = new Intent(getContext(), EditProfileActivity.class);
					getContext().startActivity(intent);
				} else if (buttonText == getContext().getResources().getString(R.string.drawer_sign_out)) {
					UIUtils.logout(getContext());
				} else if (buttonText == getContext().getResources().getString(R.string.drawer_messages)) {
					final Intent intent = new Intent(getContext(), UserListActivity.class);
					intent.putExtra("userId", User.meId());
					intent.putExtra("persona", Persona.MENTOR);
					intent.putExtra("userDisplayMode", UserDisplayMode.CHAT);
					getContext().startActivity(intent);
				}
			}
		});
		return view;
	}

	private void populate(final ViewHolder.UserItem holder, User user)
			throws JSONException {
		final ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
		imageLoader.displayImage(user.getProfileImageUrl(200),
				holder.ivMentorProfile);

		holder.tvFirstName.setText(user.getFirstName());
		holder.tvLastName.setText(user.getLastName());

		String formattedPosition = user.getJobTitle() + ", "
				+ user.getCompanyName();
		holder.tvPosition.setText(Html.fromHtml(formattedPosition));
	}
}
