/**
 * 
 */
package nz.co.pentacog.mctracker;

/**
 * @author Affian
 *
 */
public class Server {
	
	public static final String SERVER_NAME = "serverName";
	public static final String SERVER_ADDRESS = "serverAddress";
	public static final String SERVER_PORT = "serverPort";
	
	public String name = "";
	public String address = null;
	public int port = 25565;
	
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
	

}
