package com.example.reddit.imaginary.karma;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

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
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Holds the method to bu
 * 
 * @author terra
 * 
 */
public class MUtil {
	static String rid = "terra";
	static String cookie;
	/**
	 * a login https://github.com/reddit/reddit/wiki/API%3A-login
	 * www.reddit.com/api/login/username?api_type=json&user=example&passwd=hunter2
	 */
	public void login(String username, String password,Activity ac) {
		try {
			URL url = new URL("http://www.reddit.com/api/login/"+username+"?api_type=json&user="+username+"&passwd="+password);
			
			JsonReader reader = new JsonReader(new InputStreamReader(url.openStream()));
			JsonParser jp = new JsonParser();
			JsonElement je=jp.parse(reader);
			JsonObject jo = je.getAsJsonObject().get("json").getAsJsonObject().get("data").getAsJsonObject();
			cookie=jo.get("cookie").toString();
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
	public static void sell(BRep ridSold) {
		CRPC.getRPC().sell(rid, ridSold);
	}

	public static void addBuySell(final Activity activity) {
		Button buy= (Button) activity.findViewById(R.id.buyButton);
		buy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(activity,BuyActivity.class);  
				activity.startActivity(i);
			}
		});
		
		Button sell= (Button) activity.findViewById(R.id.sellButton);
		sell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(activity,SellActivity.class);  
				activity.startActivity(i);
			}
		});
	}

	public static List<BRep> getSellList() {
		return Arrays.asList(CRPC.getRPC().getBoughtList(rid));
	}
}
