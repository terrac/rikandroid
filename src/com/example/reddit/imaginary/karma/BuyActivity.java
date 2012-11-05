package com.example.reddit.imaginary.karma;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
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
public class BuyActivity extends Activity {



	protected String redditChoice = "all";



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	private void refresh() {

		new AsyncTask<String, Void, String>() {
			boolean error;
			BRep[] listArray;
			protected void onPreExecute() {
			
			}

			@Override
			protected String doInBackground(String... params) {
				listArray = CRPC.getRPC().getToBuyList(redditChoice);
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

				lv.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						BRep b = (BRep) arg0.getItemAtPosition(position);
						String text=MUtil.buy(b);
						Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

			}
		}.execute("");
	}

	

	public void chooseReddit(View view){
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_textentry, null);

		new AlertDialog.Builder(
				BuyActivity.this)
				.setTitle("Buy Custom")
				.setView(textEntryView)
				.setPositiveButton("ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
									CharSequence text = ((TextView) textEntryView
											.findViewById(R.id.alert_text_add_tag))
											.getText();
									Button b=(Button) BuyActivity.this.findViewById(R.id.chooseReddit);
									b.setText("/r/"+text);
									redditChoice = (String) text;
							}
						})
				.setNegativeButton("cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								/* User clicked cancel so do some stuff */
							}
						}).show();
	}

}
