package com.example.reddit.imaginary.karma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rik.shared.BRep;
import com.rik.shared.CRPC;

/**
 * b http://www.reddit.com/reddits/ pull into json grab titles
 * 
 * JSONObject object = (JSONObject) new JSONTokener(json).nextValue(); String
 * query = object.getString("query"); JSONArray locations =
 * object.getJSONArray("locations");
 * 
 * @author terra
 * 
 */
public class BuyActivity extends Activity implements AfterLogin {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
		MUtil.addMain(this,"Buy");
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
			BRep[] listArray;
			protected void onPreExecute() {
			
			}

			@Override
			protected String doInBackground(String... params) {
				listArray = MUtil.getToBuyList(BuyActivity.this);
				if (listArray == null) {
					error = true;
					return null;
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if (error) {
					String text = "Cannot currently access "
							+ MUtil.getSubReddit(BuyActivity.this)
							+ " Please Check your internet connection or the tag if you have set it";
					String title = "Error";
					new AlertDialog.Builder(BuyActivity.this).setTitle(title)
							.setMessage(text).setNeutralButton("Close", null)
							.show();
					//
					error = false;
					return;
				}
				ListView lv = (ListView) findViewById(R.id.listView1);

				
				
				lv.setAdapter(new ArrayAdapter<BRep>(BuyActivity.this,
						android.R.layout.simple_list_item_1, listArray));

				
				
				OnItemClickListener onClickListener = new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
    						long arg3) {
    					final BRep b = (BRep) arg0.getItemAtPosition(position);
    					//arg0.removeViewAt(position);
    					new AsyncTask<String, Void, String>(){
    						String text;
    						@Override
    						protected String doInBackground(String... params) {
    							text = MUtil.buy(b);
    							return null;
    						}
    						@Override
    						protected void onPostExecute(String result) {

    	    					Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    	    					super.onPostExecute(result);
    						}
    						
    					}.execute("");
    				}
    	        };
    	        lv.setOnItemClickListener(onClickListener);
    		
			}
		}.execute("");
	}

}
