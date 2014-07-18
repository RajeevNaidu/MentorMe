package com.codepath.wwcmentorme;

import com.codepath.wwcmentorme.fragments.ForChat_fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class Chat extends Activity {
    ListView listview_chat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_chat);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.animator.card_slide_right_in,
				R.animator.card_slide_right_out, R.animator.card_slide_left_in,
				R.animator.card_slide_left_out);
		ft.replace(R.id.clContainer, new ForChat_fragment("Sender","Receever","MyId"),
				"My Tag").addToBackStack(null);
		ft.commit();
		 
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
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

	
	class ChatTask extends AsyncTask<String,String,String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			// TODO Auto-generated method stub
			return "something";
		}
		
	}
}
