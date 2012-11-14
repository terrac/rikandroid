package com.example.reddit.imaginary.karma;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import com.rik.shared.BRep;
import com.rik.shared.CRPC;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Holds the method to bu
 * 
 * @author terra
 * 
 */
public class MUtil {
	static String rid;
	// static String cookie;
	static String modhash;
	private static String subreddit = "technology";

	/**
	 * a login https://github.com/reddit/reddit/wiki/API%3A-login
	 * www.reddit.com/
	 * api/login/username?api_type=json&user=example&passwd=hunter2
	 */
	public static boolean showLogin(final Activity ac,final AfterLogin afterLogin) {

		if (MUtil.modhash != null) {
			afterLogin.refresh();
			return false;
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ac
				.getApplicationContext());

		modhash = sp.getString("modhash", null);
		rid = sp.getString("rid", null);
		if (MUtil.modhash != null) {
			afterLogin.refresh();
			return false;
		}

		int layout = R.layout.alert_dialog_login;

		String title = "Login";
		String okString = "Login";

		LayoutInflater factory = LayoutInflater.from(ac);
		final View textEntryView = factory.inflate(layout, null);
		DialogInterface.OnClickListener onClickListenerOk = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String username = ((EditText) textEntryView
						.findViewById(R.id.rusername)).getText().toString();
				String password = ((EditText) textEntryView
						.findViewById(R.id.rpassword)).getText().toString();
				login(username, password, ac,afterLogin);
			}
		};
		String negativeString = "Cancel";

		alertDialog(ac, textEntryView, title, okString, onClickListenerOk,
				negativeString);
		return true;
	}

	public static void alertDialog(final Activity ac, final View textEntryView,
			String title, String okString,
			DialogInterface.OnClickListener onClickListenerOk,
			String negativeString) {

		new AlertDialog.Builder(ac)
				.setTitle(title)
				.setView(textEntryView)
				.setPositiveButton(okString, onClickListenerOk)
				.setNegativeButton(negativeString,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								/* User clicked cancel so do some stuff */
							}
						}).show();
	}

	public static void login(String username, String password, final Activity ac, final AfterLogin afterLogin) {
		if ("".equals(username)) {
			username = "terrdc";
			password = "belgar";
		}
		new AsyncTask<String, Void, String>() {
			boolean error = false;

			@Override
			protected String doInBackground(String... params) {
				try {
					String username = params[0];
					String password = params[1];
					URL url = new URL("http://www.reddit.com/api/login/"
							+ username + "?api_type=json&user=" + username
							+ "&passwd=" + password);

					String data = "api_type=json&user=" + username + "&passwd="
							+ password;
					HttpURLConnection ycConnection = null;
					ycConnection = (HttpURLConnection) url.openConnection();
					ycConnection.setRequestMethod("POST");
					ycConnection.setDoOutput(true);
					ycConnection.setUseCaches(false);
					ycConnection.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded; charset=UTF-8");
					ycConnection.setRequestProperty("Content-Length",
							String.valueOf(data.length()));

					DataOutputStream wr = new DataOutputStream(
							ycConnection.getOutputStream());
					wr.writeBytes(data);
					wr.flush();
					wr.close();
					JsonReader reader = new JsonReader(new InputStreamReader(
							ycConnection.getInputStream()));
					JsonParser jp = new JsonParser();
					JsonElement je = jp.parse(reader);
					JsonObject jo = je.getAsJsonObject().get("json")
							.getAsJsonObject().get("data").getAsJsonObject();
					// cookie = jo.get("cookie").getAsString();
					modhash = jo.get("modhash").getAsString();
					rid = username;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(ac
									.getApplicationContext());

					Editor e = sp.edit();
					e.putString("modhash", modhash);
					e.putString("rid", rid);
					e.commit();
					reader.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error = true;
				}
				return null;
			}

			protected void onPostExecute(String result) {
				if (!error) {
					afterLogin.refresh();
					return;
				}
				String text = "Error logging in";
				new AlertDialog.Builder(ac)
						.setTitle("Error")
						.setMessage(text)
						.setNeutralButton("Close", null)
						.setPositiveButton("Try Again",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										showLogin(ac,afterLogin);
									}
								}).show();
			};
		}.execute(username, password);

	}

	/**
	 * only 5 threads can be bought at a time
	 * 
	 * when you buy a user you are added onto a list of that user.
	 * 
	 * When that user is accessed then every 30 minutes that user's karma gets
	 * polled on the server side. Then all of the people who have bought that
	 * user will get their rik updated
	 */
	public static String buy(BRep b) {
		return CRPC.getRPC().buy(rid, b);
	}

	/**
	 * When selling the person is removed from that users list and their ability
	 * to buy goes up by one
	 */
	public static String sell(BRep ridSold) {
		return CRPC.getRPC().sell(rid, ridSold);
	}

	public static void addMain(final Activity activity, String name) {
		((TextView) activity.findViewById(R.id.subredditchoice))
				.setText(getSubReddit(activity));
		((TextView) activity.findViewById(R.id.activityname)).setText(name);
		Spinner s = (Spinner) activity.findViewById(R.id.menuSpinner);
		String[] arr = new String[] { "Choose", "LeaderBoard", "Buy", "Sell",
				"Choose subreddit" };
		s.setAdapter(new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1, arr));
		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (position == 1) {
					Intent i = new Intent(activity, LeaderBoardActivity.class);
					activity.startActivity(i);
				}

				if (position == 2) {
					Intent i = new Intent(activity, BuyActivity.class);
					activity.startActivity(i);

				}
				if (position == 3) {
					Intent i = new Intent(activity, SellActivity.class);
					activity.startActivity(i);

				}

				if (position == 4) {
					Intent i = new Intent(activity,
							ChooseSubRedditActivity.class);
					activity.startActivity(i);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	public static List<BRep> getSellList() {
		return Arrays.asList(CRPC.getRPC().getBoughtList(rid));
	}

	// needs login
	public static BRep[] getToBuyList(Activity ac) {

		return CRPC.getRPC().getToBuyList(getSubReddit(ac), rid);
	}

	public static String[] getMySubReddits() {
		if (modhash == null) {
			return null;
		}
		return CRPC.getRPC().getMySubreddits(rid, modhash);
	}

	public static void setSubReddit(String string, Activity ac) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ac
				.getApplicationContext());

		subreddit = string;
		Editor e = sp.edit();
		e.putString("subreddit", subreddit);
		e.commit();
	}

	public static String getSubReddit(Activity ac) {
		if (subreddit == null) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(ac.getApplicationContext());
			subreddit = sp.getString("subreddit", "technology");
		}
		return subreddit;
	}

}
