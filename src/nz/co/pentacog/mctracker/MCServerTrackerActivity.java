package nz.co.pentacog.mctracker;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MCServerTrackerActivity extends ListActivity {
	
	public static final int PACKET_REQUEST_CODE = 254;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);

    	  setListAdapter(new ServerListAdapter());

    	  ListView lv = getListView();
    	  lv.setTextFilterEnabled(true);

//    	  lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				Toast.makeText(getApplicationContext(), "This is a toast!", Toast.LENGTH_SHORT).show();
//			}
//    	  });

    }
  
}