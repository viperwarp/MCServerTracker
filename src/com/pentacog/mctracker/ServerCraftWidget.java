/**
 * 
 */
package com.pentacog.mctracker;

import org.json.JSONException;
import org.json.JSONObject;

import com.pentacog.mctracker.GetServerDataTask.ServerDataResultHandler;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * @author Affian
 *
 */
public class ServerCraftWidget extends AppWidgetProvider {

	public static final String PREFS_NAME = "ServerCraftWidgetPrefs";

	/**
	 * 
	 */
	public ServerCraftWidget() {
		// TODO Auto-generated constructor stub
	}

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
           // Intent intent = new Intent(context, MCServerTrackerActivity.class);
           // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
           // views.setOnClickPendingIntent(R.id.widgetLinearLayout, pendingIntent);

            WidgetUpdater updater = new WidgetUpdater(appWidgetId, views, appWidgetManager);
            updater.execute(context);
            
            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

	/**
	 * @see android.appwidget.AppWidgetProvider#onDeleted(android.content.Context, int[])
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		for (int i:appWidgetIds) {
			SharedPreferences settings = context.getSharedPreferences(ServerCraftWidget.PREFS_NAME, 0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.remove(String.valueOf(i));
		    editor.commit();
		}
		super.onDeleted(context, appWidgetIds);
	}



	private class WidgetUpdater implements ServerDataResultHandler {

		private int id;
		private RemoteViews views;
		private AppWidgetManager appWidgetManager;
		
		public WidgetUpdater(int id, RemoteViews views, AppWidgetManager appWidgetManager) {
			this.views = views;
			this.id = id;
			this.appWidgetManager = appWidgetManager;
		}
		
		public void execute(Context context) {
			SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		    JSONObject data;
			try {
				data = new JSONObject(settings.getString(String.valueOf(id), "{}"));
				Server server = new Server(data);
				new GetServerDataTask(server, this).execute();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void onServerDataResult(Server server, String result) {
			views.setTextViewText(R.id.widgetServerTitle, server.name);
			if (result == null) {
				
				views.setTextViewText(R.id.widgetMOTD, server.motd);
				views.setTextViewText(R.id.widgetPlayerCount, "" + server.playerCount +"/" + server.maxPlayers);
			} else {
				views.setTextViewText(R.id.widgetMOTD, result);
				views.setTextViewText(R.id.widgetPlayerCount, "");
			}
			appWidgetManager.updateAppWidget(id, views);
		}
		
	}
	
}
