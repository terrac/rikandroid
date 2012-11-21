package rik.imaginary.reddit.imaginary.karma;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class RBaseActivity extends Activity implements AfterLogin{
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MUtil.addMenu(this, menu);
		if(MUtil.getRID(this) == null){
			menu.getItem(0).setTitle("Login");
		} else {
			menu.getItem(0).setTitle("Logout");
			
		}
		return true;

	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.loginout) {
			if(MUtil.rid != null){
				MUtil.rid = null;
				MUtil.modhash = null;
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this
						.getApplicationContext());
				Editor e=sp.edit();
				e.remove("modhash");
				e.remove("rid");
				e.commit();

				Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT);
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
		//do nothing by default
	}
	AsyncTask<String,Void,String> aTask;
	@Override
	protected void onPause() {
		if (aTask != null) {
			aTask.cancel(true);

		}
		super.onPause();
	}
}
