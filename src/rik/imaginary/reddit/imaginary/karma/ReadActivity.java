package rik.imaginary.reddit.imaginary.karma;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
/**
 *  b http://www.reddit.com/reddits/
    pull into json
    grab titles
    
    JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
 String query = object.getString("query");
 JSONArray locations = object.getJSONArray("locations");
 
 * @author terra
 *
 */
public class ReadActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MUtil.addMain(this,"read");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
