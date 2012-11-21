package rik.imaginary.reddit.imaginary.karma;

import java.util.List;

import rik.shared.CRPC;
import rik.shared.RURep;



import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author terra
 * 
 */
public class LeaderBoardActivity extends RBaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
		MUtil.addMain(this,"Leaderboard");
        populateList(MUtil.getSubReddit(this));

	}



	/**
	 * Take the reddit name and pull from the app engine the current list of
	 * users associated with that reddit. It holds the username, their RIK,
	 * their message, and a link
	 * 
	 * @param redditname
	 */
	public void populateList(final String redditname) {
		aTask=new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				
				return CRPC.getRPC().getLeaderBoard(redditname);
			}
			@Override
			protected void onPostExecute(Object result) {
				RURep[] list = (RURep[]) result;
				ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setAdapter(new ArrayAdapter<RURep>(LeaderBoardActivity.this,  android.R.layout.simple_list_item_1, list));
    			OnItemClickListener onClickListener = new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    						long arg3) {
    					
    					//o= arg0.getItemAtPosition(position);
    	            	Toast.makeText(getApplicationContext(), "blah", Toast.LENGTH_LONG);
    				}
    	        };
    	        listView.setOnItemClickListener(onClickListener);

			}
		};
		aTask.execute();
		
	}
}
