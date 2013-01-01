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
	List<BRep> listArray;
	
	@Override
	protected String getTutorialText() {
		return "After you have bought some items, sell them here when they have hit their top karma";
	}

	protected final class SellAsync extends AsyncTask<String, Void, String> {
		boolean error;

		@Override
		protected String doInBackground(String... params) {
			if(!shouldRepeatBackground()){
				return null;
			}
			try {
				listArray = MUtil.getSellList();
			} catch (Exception e) {
			}
			if (listArray == null) {
				error = true;
				return null;
			}
			MUtil.getScore();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (error) {
				MUtil.showNetworkError(SellActivity.this);
				return;
			}
			if(listArray == null||listArray.size() == 0){
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
			MUtil.setScore(SellActivity.this);
		}

		
	}

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
					MUtil.updateScore();
					return null;
				}

				@Override
				protected void onPostExecute(String result) {

					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_LONG).show();
					MUtil.setScore(SellActivity.this);
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
	

	
	Runnable tt = new Runnable()
	{
		int count = 0;

	     @Override 
	     public void run() {
	    	 	AsyncTask<String,Void,BRep> asyncTask = new AsyncTask<String,Void,BRep>() {
	    	 		boolean error;
					@Override
					protected BRep doInBackground(String... params) {
						
						ListView lv = (ListView) findViewById(R.id.listView1);
						ArrayAdapter<BRep> aa=(ArrayAdapter<BRep>) lv.getAdapter();
						if(count >= aa.getCount()){
							return null;
						}
			    	 	String rid = ((BRep)lv.getAdapter().getItem(count)).rid;
						BRep byId = null;
						try {
							byId = CRPC.getRPC().getById(rid);
						} catch (Exception e) {
							error = true;
						}
						
						return byId;
					}
					protected void onPostExecute(BRep result) {
						if(error){
							MUtil.showNetworkError(SellActivity.this);
							error = false;
						}
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
						
						lv.postDelayed(tt, 1000);

					}
	    	 		
				};
	    	 	asyncTask.execute(new String[]{});

	     }
	};

	public AsyncTask getAsync() {return new SellAsync();};
	
}
