package nz.co.pentacog.mctracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MCServerTrackerActivity extends ListActivity {
	
	public static final int PACKET_REQUEST_CODE = 254;
	private static final String SERVER_CACHE_FILE = "mcTrackerServerCache.json";
	
	private static ServerListAdapter serverList = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
		

		ListView lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setBackgroundResource(R.drawable.dirt_tile);
		
		registerForContextMenu(lv);

    }
    
    /**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		File serverFile = new File(this.getFilesDir(), SERVER_CACHE_FILE);
		try {
		if (serverFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(serverFile));
			JSONArray servers = null;
			String jsonOutput = "";
			String temp = null;
			while ((temp = br.readLine()) != null) {
				jsonOutput += temp;
			}
			
			servers = new JSONArray(jsonOutput);
			serverList = new ServerListAdapter(servers);
		} else {
			serverFile.createNewFile();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (serverList == null) {
			serverList = new ServerListAdapter();
		}
		setListAdapter(serverList);
	}



	/**
     * Helper function to update the server list
     */
    public void updateListView() {
    	ServerListAdapter adapter = (ServerListAdapter) MCServerTrackerActivity.this.getListAdapter();
		adapter.notifyDataSetChanged();
    }
    
    /**
     * @see android.app.Activity#onCreateOptionsMenu(Menu menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.server_list_menu, menu);
        return true;
    }
    
    /**
     * @see android.app.Activity#onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.server_context, menu);
    }
    
    /**
     * @see android.app.Activity#onOptionsItemSelected(MenuItem item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_refresh:
            serverList.refresh();
            return true;
        case R.id.menu_add_server:
            Intent addServer = new Intent(this, AddServerActivity.class);
            startActivityForResult(addServer, AddServerActivity.ADD_SERVER_ACTIVITY_ID);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * @see android.app.Activity#onContextItemSelected(MenuItem item)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
      switch (item.getItemId()) {
      case R.id.context_copy:
    	  Server server = serverList.getItem(info.position);
    	  String address = server.address.toString() + ":" + server.port;
    	  ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	  clipboard.setText(address);
    	  Toast.makeText(this, address + " Copied", Toast.LENGTH_SHORT).show();
        return true;
      case R.id.context_delete:
    	deleteServer(info.position);
        return true;
      default:
        return super.onContextItemSelected(item);
      }
    }
    
    /**
     * Presents a dialog confirming server deletion
     * @param position index of server to delete
     */
    private void deleteServer(final int position) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_caution);
		builder.setPositiveButton(R.string.yes, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				serverList.remove(position);
				updateListView();
			}
		});
		builder.setNegativeButton(R.string.no, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();

    }

	/**
	 * Used to collect new server data from the AddServer Activity
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == AddServerActivity.ADD_SERVER_ACTIVITY_ID && resultCode == RESULT_OK) {
			String serverName = data.getStringExtra(Server.SERVER_NAME);
			String serverAddress = data.getStringExtra(Server.SERVER_ADDRESS);
			String serverPort = data.getStringExtra(Server.SERVER_PORT);
			
			Server newServer = new Server(serverName, serverAddress);
			newServer.port = Integer.parseInt(serverPort);
			getServerData(newServer);
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * Performs request for data from the server
	 * @param server Server object to populate with data
	 */
	private void getServerData(Server server) {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "Requesting Server Info", true);
		GetServerDataTask task = new GetServerDataTask(server, new GetServerDataTask.ServerDataResultHandler() {
			
			@Override
			public void onServerDataResult(final Server server, String result) {
				dialog.dismiss();
				if (result == null) {
					serverList.add(server);
					updateListView();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(MCServerTrackerActivity.this);
					builder.setMessage("Failed to contact server\n" + result);
					builder.setPositiveButton("Try Again", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getServerData(server);
						}
					});
					builder.setNegativeButton("Cancel", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					
					builder.create().show();
				}
			}
		});
		
		task.execute();
	}
    
    
  
}