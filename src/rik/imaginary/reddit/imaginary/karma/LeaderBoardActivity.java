package rik.imaginary.reddit.imaginary.karma;

import java.util.List;

import rik.shared.CRPC;
import rik.shared.LeaderBoardRep;
import rik.shared.RURep;
import rik.shared.Rpc;



import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author terra
 * 
 */
public class LeaderBoardActivity extends RBaseActivity {
	LeaderBoardRep leaderBoardRep;
	LeaderBoardRep dailyLeaderBoardRep;
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
				
				Rpc rpc = CRPC.getRPC();
				leaderBoardRep = rpc.getLeaderBoard(redditname);
				dailyLeaderBoardRep = rpc.getDailyLeaderBoard(redditname);
				return null;
			}
			@Override
			protected void onPostExecute(Object result) {
				
				
				ListView listView = setAll();
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



	public ListView setAll() {
		Button button = (Button) findViewById(R.id.button1);
		button.setText("All");
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(new ArrayAdapter<RURep>(LeaderBoardActivity.this,  android.R.layout.simple_list_item_1, leaderBoardRep.repL));
		return listView;
	}
	
	public void iterateLeader(View v){
		Button b = (Button) v;
		if("All".equals(b.getText())){
			b.setText("Daily");
			ListView listView = (ListView) findViewById(R.id.listView1);
			listView.setAdapter(new ArrayAdapter<RURep>(LeaderBoardActivity.this,  android.R.layout.simple_list_item_1, dailyLeaderBoardRep.repL));
			
		}
		if("Daily".equals(b.getText())){
			setAll();
		}
		b.invalidate();
	}
}
