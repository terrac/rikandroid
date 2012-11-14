package com.example.reddit.imaginary.karma;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.rik.shared.BRep;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

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
public class SellActivity extends Activity implements AfterLogin{



	private final class getSell implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			final BRep b = (BRep) arg0.getItemAtPosition(position);
			
			new AsyncTask<BRep, Void, String>(){
				String text;
				@Override
				protected String doInBackground(BRep... params) {
					text = MUtil.sell(params[0]);
					return null;
				}
				@Override
				protected void onPostExecute(String result) {

					Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
					super.onPostExecute(result);
				}
				
			}.execute(b);

				}
	}
	
	private class BRepArrayAdapter extends ArrayAdapter<BRep>  {
		public BRepArrayAdapter(Context context, int textViewResourceId,
				List<BRep> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BRep b=getItem(position);
			TextView tv = new TextView(parent.getContext());
			tv.setText(Html.fromHtml(b.toHtmlString()));
			return tv;
		}
	}
	
	protected String redditChoice;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_sell);
		setContentView(R.layout.activity_leaderboard);
		MUtil.addMain(this,"sell");
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
			List<BRep> listArray;
			

			@Override
			protected String doInBackground(String... params) {
				listArray = MUtil.getSellList();
				if (listArray == null) {
					error = true;
					return null;
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if (error) {
					String uri = getIntent().getStringExtra("url");
					String text = "Cannot currently access "
							+ uri
							+ " Please Check your internet connection or the tag if you have set it";
					String title = "Error";
					new AlertDialog.Builder(SellActivity.this).setTitle(title)
							.setMessage(text).setNeutralButton("Close", null)
							.show();
					//
					error = false;
					return;
				}
				ListView lv = (ListView) findViewById(R.id.listView1);

				
				
				ArrayAdapter<BRep> arrayAdapter = new BRepArrayAdapter(SellActivity.this,
						android.R.layout.simple_list_item_1, listArray);
				lv.setAdapter(arrayAdapter);
				OnItemClickListener onClickListener = new getSell();
    	        lv.setOnItemClickListener(onClickListener);
    		
			}
		}.execute("");
	}

	


}
