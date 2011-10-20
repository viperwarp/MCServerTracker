/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial 3.0 New Zealand License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/3.0/nz/ or send a 
 * letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
package com.pentacog.mctracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Affian
 *
 */
public class Server {
	
	/* Value Tags */
	public static final String SERVER_NAME = "serverName";
	public static final String SERVER_ADDRESS = "serverAddress";
	public static final String SERVER_PORT = "serverPort";
	public static final String SERVER_ID = "serverId";
	public static final String SERVER_FAVORITE = "serverFavorite";
	
	/* Persistent Data */
	public String name = "Undefined";
	public String address = "Undefined";
	public int port = 25565;
	public boolean favorite = false;
	
	/* Transient Data */
	public int id = -1; //currently sync to the servers location in the list
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
			
			//exception handling optional JSON values
			try {
				this.favorite = obj.getBoolean(SERVER_FAVORITE);
			} catch (JSONException e) {
				//Continue silently
			}
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put(SERVER_NAME, this.name);
			obj.put(SERVER_ADDRESS, this.address);
			obj.put(SERVER_PORT, this.port);
			obj.put(SERVER_FAVORITE, this.favorite);
		} catch (JSONException e) {
			//I really get sick of mandatory exception handling
			e.printStackTrace();
		}
		
		return obj;
	}


}
