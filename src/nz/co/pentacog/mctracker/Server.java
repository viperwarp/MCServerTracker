/**
 * 
 */
package nz.co.pentacog.mctracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Affian
 *
 */
public class Server {
	
	public static final String SERVER_NAME = "serverName";
	public static final String SERVER_ADDRESS = "serverAddress";
	public static final String SERVER_PORT = "serverPort";
	
	public String name = "Undefined";
	public String address = "Undefined";
	public int port = 25565;
	
	public int id = -1;
	public int ping = 0;
	public int playerCount = 0;
	public int maxPlayers = 0;
	public String motd = "";
	public boolean queried = false;

	/**
	 * 
	 */
	public Server(String name, String address) {
		this.name = name;
		this.address = address;
	}
	
	public Server(JSONObject obj) throws JSONException {
			this.name = obj.getString(SERVER_NAME);
			this.address = obj.getString(SERVER_ADDRESS);
			this.port = obj.getInt(SERVER_PORT);
	}
	
	public String toString() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put(SERVER_NAME, this.name);
			obj.put(SERVER_ADDRESS, this.address);
			obj.put(SERVER_PORT, this.port);
		} catch (JSONException e) {
			//I really get sick of mandatory exception handling
			e.printStackTrace();
		}
		
		return obj.toString();
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put(SERVER_NAME, this.name);
			obj.put(SERVER_ADDRESS, this.address);
			obj.put(SERVER_PORT, this.port);
		} catch (JSONException e) {
			//I really get sick of mandatory exception handling
			e.printStackTrace();
		}
		
		return obj;
	}


}
