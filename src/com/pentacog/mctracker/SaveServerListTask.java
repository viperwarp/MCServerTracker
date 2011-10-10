/**
 * 
 */
package com.pentacog.mctracker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @author macpro
 *
 */
public class SaveServerListTask extends AsyncTask<ArrayList<Server>, Void, Void> {

	private Context context;
	
	/**
	 * 
	 */
	public SaveServerListTask(Context context) {
		this.context = context.getApplicationContext();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(final ArrayList<Server>... params) {
		File serverFile = new File(context.getFilesDir(), MCServerTrackerActivity.SERVER_CACHE_FILE);
		try {
			if (!serverFile.exists()) {
				serverFile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(serverFile));
			
			JSONArray array = new JSONArray();
			for (Server server : params[0]) {
				array.put(server.toJSON());
			}
			
			bw.write(array.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		context = null;
		return null;
	}

}
