package homeward.bound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Intent i = new Intent(this, Service.class);
		if (shouldStopService()) {
			stopService(i);
		} else {
			startService(i);
		}
		finish();
	}

	boolean shouldStopService() {
		Bundle b = getIntent().getExtras();
		return b != null && b.getBoolean(Service.keyToStop, false);
	}
}