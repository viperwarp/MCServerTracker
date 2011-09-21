package nz.co.pentacog.mctracker;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class MCServerTrackerActivity extends ListActivity {
	
	public static final int PACKET_REQUEST_CODE = 254;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);

    	  setListAdapter(new ServerListAdapter());

    	  ListView lv = getListView();
    	  lv.setTextFilterEnabled(false);
    	  lv.setCacheColorHint(Color.TRANSPARENT);
    	  lv.setBackgroundResource(R.drawable.dirt_tile);


    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.server_list_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_refresh:
            
            return true;
        case R.id.menu_add_server:
            Intent addServer = new Intent(this, AddServerActivity.class);
            this.startActivity(addServer);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
  
}