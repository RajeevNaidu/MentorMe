package com.codepath.wwcmentorme.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.wwcmentorme.R;
import com.codepath.wwcmentorme.helpers.Async;
import com.codepath.wwcmentorme.helpers.Constants.Persona;
import com.codepath.wwcmentorme.helpers.UIUtils;
import com.codepath.wwcmentorme.models.User;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		if (ParseUser.getCurrentUser() != null) {
			presentMentorList();
		}
		ImageView img = (ImageView)findViewById(R.id.ivBackground);
		img.setImageResource(R.drawable.spin_animation);
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getDrawable();
		frameAnimation.setEnterFadeDuration(3000);
		frameAnimation.setExitFadeDuration(3000);
		frameAnimation.start();
		final TextView tvFindMentors = (TextView)findViewById(R.id.tvFindMentor);
		tvFindMentors.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				presentMentorList();
			}
		});
		final TextView tvBecomeAMentor = (TextView)findViewById(R.id.tvBecomeMentor);
		tvBecomeAMentor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "in onclick", Toast.LENGTH_LONG).show();
				
				UIUtils.login(HomeActivity.this, "Login to become a mentor", Persona.MENTOR, new Async.Block<User>() {
					@Override
					public void call(final User user) 
					{
						Toast.makeText(getApplicationContext(), "user here"+user, Toast.LENGTH_LONG).show();
						if (user == null) 
						{
							Toast.makeText(getApplicationContext(), "user if null "+user, Toast.LENGTH_LONG).show();
								
						} 
						else
						{
							presentMentorList();
							final Intent intent2 = new Intent(HomeActivity.this, ThankMentorActivity.class);
							startActivity(intent2);
							overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
							Toast.makeText(getApplicationContext(), "user not null"+user, Toast.LENGTH_LONG).show();
							
						}
					}
				}, false);
			}
		});
	}
	
	public void presentMentorList() {
		final Intent intent = new Intent(HomeActivity.this, MentorListActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

}
