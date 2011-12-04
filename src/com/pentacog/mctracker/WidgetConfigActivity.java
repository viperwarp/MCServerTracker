/**
 * 
 */
package com.pentacog.mctracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;

/**
 * @author Affian
 *
 */
public class WidgetConfigActivity extends ListActivity {

	
    private int mAppWidgetId;



	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Intent intent = getIntent();
    	Bundle extras = intent.getExtras();
    	if (extras != null) {
    	    mAppWidgetId = extras.getInt(
    	            AppWidgetManager.EXTRA_APPWIDGET_ID, 
    	            AppWidgetManager.INVALID_APPWIDGET_ID);
    	}
    	
    	super.onCreate(savedInstanceState);
		ListView lv = getListView();
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setBackgroundResource(R.drawable.dirt_tile);
		
		Intent resultValue = new Intent();
	    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
	    setResult(RESULT_CANCELED, resultValue);
    }

	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		ListView lv = getListView();
		
		ArrayList<HashMap<String, String>> serverList = new ArrayList<HashMap<String, String>>();
		
		try {
			InputStream instream = openFileInput(MCServerTrackerActivity.SERVER_CACHE_FILE);
			 
		    // if file the available for reading
		    if (instream != null) {
		        // prepare the file for reading
		        InputStreamReader inputreader = new InputStreamReader(instream);
		        BufferedReader br = new BufferedReader(inputreader, 4000);
			
				JSONArray servers = null;
				StringBuilder output = new StringBuilder(100);
				String temp = null;
				while ((temp = br.readLine()) != null) {
					output.append(temp);
				}
				
				servers = new JSONArray(output.toString());
				
				br.close();
				instream.close();
				
				for (int i = 0; i < servers.length(); i++) {
					JSONObject obj = servers.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(Server.SERVER_NAME, obj.getString(Server.SERVER_NAME));
					map.put(Server.SERVER_ADDRESS, obj.getString(Server.SERVER_ADDRESS) + ":" + obj.optString(Server.SERVER_PORT));
					serverList.add(map);
				}
				
			}
		} catch (IOException e) {
			/*
			 * Breaks here on first load with file not found
			 * New one will be created on next save
			 */
		} catch (JSONException e) {
			
		}
		
		
		SimpleAdapter adapter = new SimpleAdapter(this, serverList, android.R.layout.two_line_list_item, 
				new String[] {Server.SERVER_NAME, Server.SERVER_ADDRESS}, 
				new int[] {android.R.id.text1, android.R.id.text2});
		
		lv.setAdapter(adapter);
		
		super.onResume();
	}



	/**
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) l.getAdapter().getItem(position);
		String[] address = map.get(Server.SERVER_ADDRESS).split(":");
		
		Server server = new Server(map.get(Server.SERVER_NAME), address[0]);
		server.port = Integer.valueOf(address[1]);
		
		SharedPreferences settings = getSharedPreferences(ServerCraftWidget.PREFS_NAME, MODE_WORLD_READABLE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(String.valueOf(mAppWidgetId), server.toString());
	    editor.commit();

	    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
	    RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.appwidget_provider_layout);
	    appWidgetManager.updateAppWidget(mAppWidgetId, views);
	    
	    Intent resultValue = new Intent();
	    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
	    setResult(RESULT_OK, resultValue);
	    finish();
		
		super.onListItemClick(l, v, position, id);
	}
    
    
    
    
}