package rik.imaginary.reddit.imaginary.karma;

import rik.shared.BRep;
import rik.shared.CRPC;
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
public class BuyActivity extends RBaseActivity {

	BRep[] listArray;
	protected final class BuyAsync extends AsyncTask<String, Void, String> {
		boolean error;

		protected void onPreExecute() {
		
		}

		@Override
		protected String doInBackground(String... params) {
			if(!shouldRepeatBackground()){
				return null;
			}
			try {
				listArray = MUtil.getToBuyList(BuyActivity.this);
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
			if(error){
				MUtil.showNetworkError(BuyActivity.this);
				error = false;
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
		   MUtil.setScore(BuyActivity.this);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic);
		MUtil.addMain(this,"Buy");
		MUtil.showLogin(this,this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}

	public AsyncTask getAsync(){
		return new BuyAsync();
	}
	@Override
	protected String getTutorialText() {
		return "The goal is to buy the links that will go up the most in karma.  You have a limit of 5";
	}
}
