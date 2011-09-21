/**
 * 
 */
package nz.co.pentacog.mctracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author Affian
 *
 */
public class AddServerActivity extends Activity {

	public static final int ADD_SERVER_ACTIVITY_ID = 10;
	
	/**
	 * 
	 */
	public AddServerActivity() {
		//Empty Constructor
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.add_server_layout);
	}
	
	public void submit(View view) {
		TextView serverName = (TextView) this.findViewById(R.id.serverNameEdit);
		TextView serverAddress = (TextView) this.findViewById(R.id.serverAddressEdit);
		TextView serverPort = (TextView) this.findViewById(R.id.serverPortEdit);
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra(Server.SERVER_NAME, serverName.getText().toString());
		resultIntent.putExtra(Server.SERVER_ADDRESS, serverAddress.getText().toString());
		resultIntent.putExtra(Server.SERVER_PORT, serverPort.getText().toString());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
	
	

}
