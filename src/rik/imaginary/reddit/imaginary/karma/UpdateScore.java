package rik.imaginary.reddit.imaginary.karma;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class UpdateScore extends AsyncTask<String, Void, String>{
	int score;
	Activity a;
	public UpdateScore(Activity activity) {
		a = activity;
	}
	@Override
	protected String doInBackground(String... params) {
		score =MUtil.getScore();
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		((TextView) a.findViewById(R.id.score)).setText(""+score);
		super.onPostExecute(result);
	}

}
