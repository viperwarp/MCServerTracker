/**
 * 
 */
package nz.co.pentacog.mctracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nz.co.pentacog.mctracker.GetServerDataTask.ServerDataResultHandler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Affian
 *
 */
public class ServerListAdapter extends BaseAdapter implements Filterable {
	
	private ArrayList<Server> serverList = null;
	
	/**
	 * 
	 */
	public ServerListAdapter() {
		this(new ArrayList<Server>());
	}
	
	/**
	 * 
	 * @param serverList
	 */
	public ServerListAdapter(ArrayList<Server> serverList) {
		this.serverList = serverList;
		
	}

	/**
	 * 
	 * @param servers
	 */
	public ServerListAdapter(JSONArray servers) {
		this.serverList = new ArrayList<Server>();
		
		for (int i = 0; i < servers.length(); i++) {
			try {
				JSONObject obj = servers.getJSONObject(i);
				
				Server server = new Server(obj);
				serverList.add(server);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
		return serverList.get(position).id;
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
		ServerViewHolder holder = null;
		Server server = serverList.get(position);
		server.id = position;
		if (convertView == null) {
			serverView = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
			holder = new ServerViewHolder((int) getItemId(position), serverView);
			serverView.setTag(holder);
		} else {
			serverView = (RelativeLayout) convertView;
			holder = (ServerViewHolder) serverView.getTag();
			holder.id = (int) getItemId(position);
		}
		
		
		//set server name
		holder.serverTitle.setText(server.name);
		//set server IP
		String serverName = server.address.toString();
		if (!serverName.startsWith("/")) {
			int index = serverName.lastIndexOf('/');
			if (index != -1) {
				String tempString;
				tempString = serverName.substring(index+1);
				serverName = serverName.substring(0, index);
				serverName += " " + tempString;
			}
		} else {
			serverName = serverName.replace("/", "");
		}
		holder.serverIp.setText(serverName + ":" + server.port);
		
		if (!server.queried) {
			holder.loading.setVisibility(View.VISIBLE);
			holder.playerCount.setText("" + server.playerCount + "/" + server.maxPlayers);
			holder.serverData.setText(R.string.loading);
			new ServerViewUpdater(serverView, server);
		} else {
			holder.loading.setVisibility(View.GONE);
			holder.playerCount.setText("" + server.playerCount + "/" + server.maxPlayers);
			holder.serverData.setText(server.motd);
			holder.ping.setText("" + server.ping + "ms");
		}
		
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
	 * @see android.widget.ListAdapter#areAllItemsEnabled()
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see android.widget.ListAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void remove(int position) {
		serverList.remove(position);
	}

	public void add(Server newServer) {
		serverList.add(newServer);
		
	}
	
	public void refresh() {
		for (Server server : serverList) {
			server.queried = false;
		}
		this.notifyDataSetChanged();
	}
	
	public class ServerViewHolder {
		
		
		
		public int id;
		public TextView serverTitle;
		public TextView serverIp;
		public TextView playerCount;
		public TextView serverData;
		public TextView ping;
		public ProgressBar loading;
		
		ServerViewHolder(int id, View serverView) {
			this.id = id;
			serverTitle = (TextView) serverView.findViewById(R.id.serverTitle);
			serverIp = (TextView) serverView.findViewById(R.id.serverIp);
			playerCount = (TextView) serverView.findViewById(R.id.playerCount);
			serverData = (TextView) serverView.findViewById(R.id.serverData);
			loading = (ProgressBar) serverView.findViewById(R.id.updating_server);
			ping = (TextView) serverView.findViewById(R.id.ping);
		}
	}

	private class ServerViewUpdater implements ServerDataResultHandler {
		private View view;

		public ServerViewUpdater(View view, Server server) {
			this.view = view;
			new GetServerDataTask(server, this).execute();
		}
		
		@Override
		public void onServerDataResult(Server server, String result) {
			ServerViewHolder holder = (ServerViewHolder) view.getTag();
			if (holder.id == server.id) {
				holder.loading.setVisibility(View.GONE);
				holder.playerCount.setText("" + server.playerCount + "/" + server.maxPlayers);
				holder.ping.setText("" + server.ping + "ms");
				/*
				 * No Internet = "Network Unreachable"
				 * open port but no server = "The operation timed out"
				 * No open ports = <address> - Connection refused
				 */
				
				holder.serverData.setText(server.motd);
			} else {
				server.queried = false;
			}

		}
		
		
	}
	
	public final ArrayList<Server> getServerList() {
		return this.serverList;
	}
	
	

}
