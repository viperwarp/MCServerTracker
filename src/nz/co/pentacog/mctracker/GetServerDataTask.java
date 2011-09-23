/**
 * 
 */
package nz.co.pentacog.mctracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.os.AsyncTask;

/**
 * @author Affian
 *
 */
public class GetServerDataTask extends AsyncTask<Void, Void, String> {

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
			
			
			String[] parts = null;
			byte[] bytes = new byte[128];
			Socket sock = new Socket(server.address, server.port);
			OutputStream os = sock.getOutputStream();
			InputStream is = sock.getInputStream();
			
			os.write(MCServerTrackerActivity.PACKET_REQUEST_CODE);
			is.read(bytes);
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
				
			} else {
				error = "Server is running an incompatible version";
			}
			
		} catch (IOException e) {
			error = e.getLocalizedMessage();
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
