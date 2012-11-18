package com.example.reddit.imaginary.karma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.rik.shared.BRep;
import com.rik.shared.CRPC;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class ChooseSubRedditActivity extends Activity implements AfterLogin{



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosesubreddit);
		//setContentView(R.layout.activity_leaderboard);
		MUtil.addMain(this,"Choose Subreddit");
		MUtil.showLogin(this,this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public void refresh() {

		new AsyncTask<String, Void, String>() {
			boolean error;
			String[] listArray;
			protected void onPreExecute() {
			
			}

			@Override
			protected String doInBackground(String... params) {
				listArray = MUtil.getMySubReddits();
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				
				

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

			}
		}.execute("");
	}

	
	public void chooseReddit(View v){
		MUtil.setSubReddit( ((AutoCompleteTextView)findViewById(R.id.autocomplete)).getText().toString(),this);
		
		Intent i = new Intent(this, LeaderBoardActivity.class);
		this.startActivity(i);
	}

}
