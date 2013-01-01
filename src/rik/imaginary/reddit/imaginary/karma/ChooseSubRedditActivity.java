package rik.imaginary.reddit.imaginary.karma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rik.shared.RURep;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseSubRedditActivity extends RBaseActivity{



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosesubreddit);
		//setContentView(R.layout.activity_leaderboard);
		MUtil.addMain(this,"Choose Subreddit");
		MUtil.showLogin(this,this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}
	{

		aTask=new AsyncTask<String, Void, String>() {
			boolean error;
			String[] listArray;
			protected void onPreExecute() {
			
			}

			@Override
			protected String doInBackground(String... params) {
				if(!shouldRepeatBackground()){
					return null;
				}
				try {
					listArray = MUtil.getMySubreddits();
				} catch (Exception e) {
					error = true;
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if(error){
					MUtil.showNetworkError(ChooseSubRedditActivity.this);
					error = false;
				}
				

		        String[] stringArray = getResources().getStringArray(R.array.defaultReddits);
				List<String> l=new ArrayList<String>(Arrays.asList(stringArray));
				if(listArray != null){
					l.addAll(Arrays.asList(listArray));	
				}
			
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChooseSubRedditActivity.this,
		                android.R.layout.simple_dropdown_item_1line, l.toArray(new String[0]));
				
		        AutoCompleteTextView textView = (AutoCompleteTextView)
		                findViewById(R.id.autocomplete);
		        textView.setAdapter(adapter);


		        
		        ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setAdapter(new ArrayAdapter<String>(ChooseSubRedditActivity.this,  android.R.layout.simple_list_item_1, l));
    			OnItemClickListener onClickListener = new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    						long arg3) {
    					
    					String o= (String) arg0.getItemAtPosition(position);
    					chooseReddit(o);
    	            	//Toast.makeText(getApplicationContext(), "blah", Toast.LENGTH_LONG);
    				}
    	        };
    	        listView.setOnItemClickListener(onClickListener);

			}
		};
	}

	
	public void chooseReddit(View v){
		String string = ((AutoCompleteTextView)findViewById(R.id.autocomplete)).getText().toString();
		chooseReddit(string);
	}

	public void chooseReddit(String string) {
		MUtil.setSubReddit( string,this);
		
		Intent i = new Intent(this, LeaderBoardActivity.class);
		this.startActivity(i);
	}
	
	@Override
	protected String getTutorialText() {
		return "Type in a subreddit or choose one";
	}

}
