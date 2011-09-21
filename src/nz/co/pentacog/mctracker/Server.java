/**
 * 
 */
package nz.co.pentacog.mctracker;

import java.net.InetAddress;

/**
 * @author macpro
 *
 */
public class Server {
	
	public String name = "";
	public InetAddress address = null;
	public int port = 25565;
	
	public int ping = 0;
	public int playerCount = 0;
	public int maxPlayers = 0;
	public String motd = "";

	/**
	 * 
	 */
	public Server(String name, InetAddress address) {
		this.name = name;
		this.address = address;
	}

}
