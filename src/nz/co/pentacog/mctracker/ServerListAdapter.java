/**
 * 
 */
package nz.co.pentacog.mctracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Affian
 *
 */
public class ServerListAdapter implements ListAdapter, Filterable {
	
	private ArrayList<Server> serverList = null;

	/**
	 * 
	 */
	public ServerListAdapter() {
		serverList = new ArrayList<Server>();
		
		try {
			serverList.add(new Server("My Server", InetAddress.getByName("119.224.43.89")));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return serverList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Server getItem(int position) {
		return serverList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemViewType(int)
	 */
	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		RelativeLayout serverView = null;
		Server server = serverList.get(position);
		
		if (convertView == null) {
			serverView = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
		} else {
			serverView = (RelativeLayout) convertView;
		}
		String[] parts = null;
		try {
			byte[] bytes = new byte[128];
			Socket sock = new Socket("119.224.43.89", 25565);
			OutputStream os = sock.getOutputStream();
			InputStream is = sock.getInputStream();
			
			os.write(MCServerTrackerActivity.PACKET_REQUEST_CODE);
			is.read(bytes);
			ByteBuffer b = ByteBuffer.wrap(bytes);
			b.get();
			short stringLen = b.getShort();
			byte[] stringData = new byte[stringLen * 2];
			b.get(stringData);

			String message = "";
			try {
				message = new String(stringData, "UTF-16BE");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			parts = message.split("¤");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//set server name
		TextView serverTitle = (TextView) serverView.findViewById(R.id.serverTitle);
		serverTitle.setText(server.name);
		//set server IP
		TextView serverIp = (TextView) serverView.findViewById(R.id.serverIp);
		serverIp.setText(server.address.toString() + ":25565");
		
		if (parts != null) {
			TextView serverData = (TextView) serverView.findViewById(R.id.serverData);
			serverData.setText(parts[0]);
		}
		//Server data
		
		
		//if has ping/player data set that too
		
		return serverView;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getViewTypeCount()
	 */
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return serverList.isEmpty();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#registerDataSetObserver(android.database.DataSetObserver)
	 */
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#unregisterDataSetObserver(android.database.DataSetObserver)
	 */
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	/* (non-Javadoc)
	 * @see android.widget.ListAdapter#areAllItemsEnabled()
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.ListAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
