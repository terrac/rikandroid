package rik.shared;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import rik.imaginary.reddit.imaginary.karma.MUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class SUtil {
	public static JsonElement getJsonElement(String url){
		try {
			

			URL uurl = new URL(url);
			HttpURLConnection ycConnection = null;
			ycConnection = (HttpURLConnection) uurl.openConnection();
			ycConnection.setRequestMethod("GET");
			ycConnection.setDoOutput(true);
			ycConnection.setUseCaches(false);
			ycConnection.setRequestProperty("Cookie", "reddit_session="+MUtil.cookie);
			ycConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
//			ycConnection.setRequestProperty("Content-Length",
//					String.valueOf(data.length()));

//			DataOutputStream wr = new DataOutputStream(
//					ycConnection.getOutputStream());
//			wr.writeBytes(data);
//			wr.flush();
//			wr.close();
			JsonReader reader = new JsonReader(new InputStreamReader(
					ycConnection.getInputStream()));
			JsonParser jp = new JsonParser();
			JsonElement je=jp.parse(reader);
			return je;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static JsonObject getChild(JsonArray jsonArray, int i) {
		JsonObject child = jsonArray.get(i).getAsJsonObject().get("data").getAsJsonObject();
		return child;
	}

	public static JsonArray getArray(JsonElement je) {
		JsonArray jsonArray = je.getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
		return jsonArray;
	}
}
