package rik.imaginary.reddit.imaginary.karma;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rik.shared.BRep;
import rik.shared.CRPC;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.SlidingDrawer;
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
public class SellActivity extends RBaseActivity {

	private final class getSell implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			final BRep b = (BRep) arg0.getItemAtPosition(position);
			ListView lv = (ListView) findViewById(R.id.listView1);
			((ArrayAdapter<BRep>)lv.getAdapter()).remove(b);
			new AsyncTask<BRep, Void, String>() {
				String text;

				@Override
				protected String doInBackground(BRep... params) {
					text = MUtil.sell(params[0]);
					return null;
				}

				@Override
				protected void onPostExecute(String result) {

					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_LONG).show();
					super.onPostExecute(result);
				}

			}.execute(b);

		}
	}

	private class BRepArrayAdapter extends ArrayAdapter<BRep> {
		public BRepArrayAdapter(Context context, int textViewResourceId,
				List<BRep> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BRep b = getItem(position);
			TextView tv = new TextView(parent.getContext());
			tv.setText(Html.fromHtml(b.toHtmlString()));
			return tv;
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_sell);
		setContentView(R.layout.activity_generic);
		MUtil.addMain(this, "Sell");
		MUtil.showLogin(this, this);
		((TextView) findViewById(R.id.subredditchoice))
		.setText("Bought Items");

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}

	public void refresh() {

		aTask=new AsyncTask<String, Void, String>() {
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

				ArrayAdapter<BRep> arrayAdapter = new BRepArrayAdapter(
						SellActivity.this, android.R.layout.simple_list_item_1,
						listArray);
				lv.setAdapter(arrayAdapter);
				OnItemClickListener onClickListener = new getSell();
				lv.setOnItemClickListener(onClickListener);
				if(lv.getCount() > 0){
					tt.run();	
				}
			}
		};
		aTask.execute("");
	}
	
	
	Runnable tt = new Runnable()
	{
		int count = 0;

	     @Override 
	     public void run() {
	    	 	AsyncTask<String,Void,BRep> asyncTask = new AsyncTask<String,Void,BRep>() {

					@Override
					protected BRep doInBackground(String... params) {
						
						ListView lv = (ListView) findViewById(R.id.listView1);
						ArrayAdapter<BRep> aa=(ArrayAdapter<BRep>) lv.getAdapter();
						if(count >= aa.getCount()){
							return null;
						}
			    	 	String rid = ((BRep)lv.getAdapter().getItem(count)).rid;
						BRep byId = CRPC.getRPC().getById(rid);
						
						return byId;
					}
					protected void onPostExecute(BRep result) {
						ListView lv = (ListView) findViewById(R.id.listView1);
						
						ArrayAdapter<BRep> aa=(ArrayAdapter<BRep>) lv.getAdapter();
						if(count >= aa.getCount()){
							return;
						}
						BRep listItem = aa.getItem(count);
						BRep item = listItem;
						listItem.htmlString =  listItem.message+"<br><font color=grey>"+item.score+"</font> "+BRep.getColorCoded(result.score);
						
						count++;
						//make this a different color and another line
						
						//aa.getItem(count).rid = item.rid;
						//aa.getItem(count).score = item.score;
						aa.notifyDataSetChanged();
						
						lv.postDelayed(tt, 2000);

					}
	    	 		
				};
	    	 	asyncTask.execute(new String[]{});

	     }
	};
	
}
