package rik.imaginary.reddit.imaginary.karma;

import rik.shared.CRPC;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PrefsFragment extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
	}

	public void displayAdMessage(View view) {
		Toast.makeText(
				getApplicationContext(),
				"Please consider rating the app highly or mentioning it to your friends",
				Toast.LENGTH_LONG).show();
	}

	public void setMessage(View view) {
		if (MUtil.rid == null) {
			return;
		}
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());

				CRPC.getRPC().setMessage(MUtil.rid,
						preferences.getString("message", ""));
				return null;
			}
		}.execute("");
	}

	@Override
	protected void onPause() {
		setMessage(null);
		super.onPause();
	}

}