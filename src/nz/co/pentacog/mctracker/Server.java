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
	public InetAddress address;
	
	public int ping;
	public int playerCount;
	public int maxPlayers;
	public String motd;

	/**
	 * 
	 */
	public Server(String name, InetAddress address) {
		this.name = name;
		this.address = address;
	}

}
