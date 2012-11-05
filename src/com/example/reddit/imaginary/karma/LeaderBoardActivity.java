package com.example.reddit.imaginary.karma;

import java.util.List;


import com.rik.shared.BRep;
import com.rik.shared.CRPC;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author terra
 * 
 */
public class LeaderBoardActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
        MUtil.addBuySell(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;

	}

	/**
	 * Take the reddit name and pull from the app engine the current list of
	 * users associated with that reddit. It holds the username, their RIK,
	 * their message, and a link
	 * 
	 * @param redditname
	 */
	public void populateList(final String rid,final String redditname) {
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				
				return CRPC.getRPC().getLeaderBoard(redditname);
			}
			@Override
			protected void onPostExecute(Object result) {
				List<BRep> list = (List<BRep>) result;
				ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setAdapter(new ArrayAdapter<BRep>(LeaderBoardActivity.this,  android.R.layout.simple_list_item_1, list.toArray(new BRep[0])));
    			OnItemClickListener onClickListener = new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    						long arg3) {
    					
    					//o= arg0.getItemAtPosition(position);
    	            	
    				}
    	        };
    	        listView.setOnItemClickListener(onClickListener);

			}
		}.execute();
		
	}
}
