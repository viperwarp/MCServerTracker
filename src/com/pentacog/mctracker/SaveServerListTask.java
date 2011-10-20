/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial 3.0 New Zealand License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/3.0/nz/ or send a 
 * letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
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
 * @author Affian
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
			BufferedWriter bw = new BufferedWriter(new FileWriter(serverFile), 4000);
			
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
