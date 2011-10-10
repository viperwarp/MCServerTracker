/**
 * 
 */
package com.pentacog.mctracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.os.AsyncTask;

/**
 * @author Affian
 *
 */
public class GetServerDataTask extends AsyncTask<Void, Void, String> {

	private static final int SOCKET_TIMEOUT = 10000;
	private Server server = null;
	private ServerDataResultHandler handler = null;
	/**
	 * 
	 */
	public GetServerDataTask(Server server, ServerDataResultHandler handler) {
		this.server = server;
		this.handler = handler;
	}
	
	

	/**
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
	}



	/**
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {
		String error = null;
		
		try {
			long requestTime = 0;
			
			String[] parts = null;
			byte[] bytes = new byte[128];
//			ByteBuffer b = ByteBuffer.allocate(512);
			Socket sock = new Socket(server.address, server.port);
			sock.setSoTimeout(SOCKET_TIMEOUT);
			OutputStream os = sock.getOutputStream();
			InputStream is = sock.getInputStream();
			
			requestTime = System.currentTimeMillis();
			os.write(MCServerTrackerActivity.PACKET_REQUEST_CODE);
			is.read(bytes);
			//TODO allow for reading of multiple packets
//			while (is.read(bytes) != -1) {
//				b.put(bytes);
//			}
			requestTime = System.currentTimeMillis() - requestTime;
			
			ByteBuffer b = ByteBuffer.wrap(bytes);
			b.get();
			short stringLen = b.getShort();
			byte[] stringData = new byte[stringLen * 2];
			b.get(stringData);

			sock.close();
			
			String message = "";
			try {
				message = new String(stringData, "UTF-16BE");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
 
			parts = message.split("\u00A7");
			
			if (parts.length == 3) {
				server.motd = parts[0];
				server.playerCount = Integer.parseInt(parts[1]);
				server.maxPlayers = Integer.parseInt(parts[2]);
				server.ping = (int)requestTime;
			} else {
				error = "Server is running an incompatible version";
			}
			
		} catch (IOException e) {
			//TODO move to strings.xml
			if (e instanceof SocketTimeoutException) {
				error = "Connection timed out";
			} else if (e instanceof UnknownHostException) {
				error = "Unable to resolve DNS";
			} else if (e.getMessage().endsWith("Connection refused")) {
				error = "Connection refused";
			} else {
				error = e.getMessage();
			}
		}
		server.queried = true;
		if (error != null) {
			server.motd = error;
		}
		
		return error;
	}
	
	

	/**
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		handler.onServerDataResult(server, result);
		super.onPostExecute(result);
	}



	public interface ServerDataResultHandler {
		public void onServerDataResult(Server server, String result);
	}
	
}
