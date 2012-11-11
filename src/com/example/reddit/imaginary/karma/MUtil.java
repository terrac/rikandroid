package com.example.reddit.imaginary.karma;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Holds the method to bu
 * 
 * @author terra
 * 
 */
public class MUtil {
	static String rid ;
	static String cookie;
	static String modhash;
	static String subreddit = "technology";
	/**
	 * a login https://github.com/reddit/reddit/wiki/API%3A-login
	 * www.reddit.com/api/login/username?api_type=json&user=example&passwd=hunter2
	 */
	public static boolean showLogin(final Activity ac) {
		if(MUtil.modhash != null){
			return false;
		}
		int layout = R.layout.alert_dialog_login;
		
		

		String title = "Login";
		String okString = "Login";
		DialogInterface.OnClickListener onClickListenerOk = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				String username=	(String) ((TextView)ac.findViewById(R.id.rusername)).getText();
				String password=	(String) ((TextView)ac.findViewById(R.id.rpassword)).getText();
				login(username, password, ac);
			}
		};
		String negativeString = "Cancel";
		LayoutInflater factory = LayoutInflater.from(ac);
		View textEntryView = factory.inflate(
				layout, null);
		alertDialog(ac, textEntryView, title, okString, onClickListenerOk,
				negativeString);
		return true;
	}
	public static void alertDialog(final Activity ac,final View textEntryView, String title,
			String okString, DialogInterface.OnClickListener onClickListenerOk,
			String negativeString) {
		
		new AlertDialog.Builder(
				ac)
				.setTitle(title)
				.setView(textEntryView)
				.setPositiveButton(okString,
						onClickListenerOk)
				.setNegativeButton(negativeString,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								/* User clicked cancel so do some stuff */
							}
						}).show();
	}
	public static void login(String username, String password,Activity ac) {
				
		try {
			URL url = new URL("http://www.reddit.com/api/login/"+username+"?api_type=json&user="+username+"&passwd="+password);
			
			JsonReader reader = new JsonReader(new InputStreamReader(url.openStream()));
			JsonParser jp = new JsonParser();
			JsonElement je=jp.parse(reader);
			JsonObject jo = je.getAsJsonObject().get("json").getAsJsonObject().get("data").getAsJsonObject();
			cookie=jo.get("cookie").toString();
			modhash=jo.get("modhash").toString();
			rid = username;
			reader.close();
		} catch (Exception e) {
			String text = "Error logging in";
			new AlertDialog.Builder(ac).setTitle("Error")
					.setMessage(text).setNeutralButton("Close", null)
					.show();
		}

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

	public static void addMain(final Activity activity,String name) {
		((TextView) activity.findViewById(R.id.subredditchoice)).setText(MUtil.subreddit);
		((TextView) activity.findViewById(R.id.subredditchoice)).setText(name);
		Spinner s = (Spinner) activity.findViewById(R.id.menuSpinner);
		String[] arr = new String[]{"Choose","LeaderBoard","Buy","Sell","Choose subreddit"};
		s.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1, arr));
		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if(position == 1){
					Intent i = new Intent(activity,LeaderBoardActivity.class);  
					activity.startActivity(i);
				}
				
				if(position == 2){
					Intent i = new Intent(activity,BuyActivity.class);  
					activity.startActivity(i);
				
				}
				if(position == 3){
					Intent i = new Intent(activity,SellActivity.class);  
					activity.startActivity(i);
				
				}
				
				if(position == 4){
					Intent i = new Intent(activity,ChooseSubRedditActivity.class);  
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

	public static BRep[] getToBuyList() {
		
		return CRPC.getRPC().getToBuyList(subreddit,rid);
	}
	public static String[] getMySubReddits() {
		if(modhash == null){
			return null;
		}
		return CRPC.getRPC().getMySubreddits(rid, modhash);
	}
}
