package com.codepath.wwcmentorme.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.wwcmentorme.R;



class CustomChatAdapter extends ArrayAdapter<String>
{
    Context context;
    int layoutId;
    DataItemHolder holder;
    boolean sender=false;
    boolean receiver=false;
    
    ArrayList<String> left;
    ArrayList<String> right;
	public CustomChatAdapter(Context con, int resource,String who,ArrayList<String> l,ArrayList<String> r)
	{
		super(con, resource);
		this.left=l;
		this.right=r;
		// TODO Auto-generated constructor stub
		this.context=con;
		this.layoutId=resource;
		
		if(who.equals("sender"))
		{
			this.sender=true;
		}
		else
		{
			this.receiver=true;
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=convertView;
		if(view==null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view=inflater.inflate(layoutId, parent, false);
			holder = new DataItemHolder(view);
			holder.left=(TextView)view.findViewById(R.id.tvMessage_left);
			holder.right = (TextView) view.findViewById(R.id.tvMessage_right);
			view.setTag(holder);
			
		}
		else
		{

			holder = (DataItemHolder) view.getTag();	
		}
		
		if(sender)
		{
			this.holder.left.setVisibility(LinearLayout.VISIBLE);
		   
			this.holder.right.setVisibility(LinearLayout.INVISIBLE);
		}
		else if(receiver)
		{
			this.holder.right.setVisibility(LinearLayout.VISIBLE);
		
			this.holder.left.setVisibility(LinearLayout.INVISIBLE);
		}
		return view;
	}
	private static class DataItemHolder {
		TextView left,right;
		public DataItemHolder(View v)
		{
			left=(TextView)v.findViewById(R.id.tvMessage_left);
			right=(TextView)v.findViewById(R.id.tvMessage_right);
		}
		
	}
	
	
}
public class ForChat_fragment extends Fragment
{
  ListView chat_list;
  String from="";
  String to="";
  String myid="";
  ForChat_fragment()
  {
	  
  }
  public ForChat_fragment(String sender,String receiver,String myid)
  {
	  this.from=sender;
	  this.to=receiver;
	  this.myid=myid;
  }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.forchat_fragment, container, false);
	    chat_list=(ListView)view.findViewById(R.id.listView_chat);
	    //im senidn gthe message
	    if(myid.equals(from))
	    {
	    	//load left chat item as I am sending message
	    	
	        
	    }
	    else if(myid.equals(to))
	    {
	    	//load right chat item as I am receiving message
	    	
	    	
	    }
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

}
