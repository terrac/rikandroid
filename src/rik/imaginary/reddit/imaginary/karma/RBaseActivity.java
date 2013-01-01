package rik.imaginary.reddit.imaginary.karma;

import java.util.Calendar;
import java.util.Date;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public abstract class RBaseActivity extends Activity implements AfterLogin {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		final SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if(preferences.getBoolean("tutorial", true)){
			LayoutInflater factory = LayoutInflater.from(this);
			
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_tutorial, null);
			((TextView) textEntryView.findViewById(R.id.message)).setText(getTutorialText());
			textEntryView.findViewById(R.id.checkBox).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					CheckBox cb = (CheckBox) arg0;
					Editor e=preferences.edit();
					e.putBoolean("tutorial", !cb.isChecked());
					e.commit();
				}
			});
			new AlertDialog.Builder(this).setView(textEntryView).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).show();
		}
	}
	
	protected String getTutorialText() {
		return "Generic Tutorial Text";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MUtil.addMenu(this, menu);

		return true;

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (MUtil.getRID(this) == null) {
			menu.getItem(0).setTitle("Login");
		} else {
			menu.getItem(0).setTitle("Logout");

		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.loginout) {
			if (MUtil.rid != null) {
				MUtil.rid = null;
				MUtil.modhash = null;
				MUtil.cookie = null;
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(this
								.getApplicationContext());
				Editor e = sp.edit();
				e.remove("modhash");
				e.remove("rid");
				e.commit();

				Toast.makeText(getApplicationContext(), "Logged out",
						Toast.LENGTH_LONG);
			} else {
				MUtil.showLogin(this, this);
			}
		}
		if (item.getItemId() == R.id.profile) {
			Intent myIntent = new Intent(this, PrefsFragment.class);
			this.startActivity(myIntent);

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void refresh() {
		
		AsyncTask a = getAsync();
		if(a != null){
			aTask = a;
		}
		if(aTask != null){
			aTask.execute("");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, 15);
			lastExecuted = c.getTime();
		}
	}

	public void refresh(View v){
		lastExecuted = null;
		refresh();
	}
	public boolean shouldRepeatBackground(){
		return lastExecuted != null&& lastExecuted.after(Calendar.getInstance().getTime());
	}
	public AsyncTask getAsync(){
		return null;
	}
	AsyncTask<String, Void, String> aTask;
	Date lastExecuted;
	@Override
	protected void onPause() {
		if (aTask != null) {
			aTask.cancel(true);

		}
		super.onPause();
	}
	@Override
	protected void onResume() {
		lastResume = this;
		super.onResume();
	}
	public static Activity lastResume;
}
