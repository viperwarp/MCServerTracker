package com.pentacog.mctracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MCTrackerService extends Service {
	public MCTrackerService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
