package homeward.bound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

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

	boolean isAccessibilityEnabled() {
		String s = Settings.Secure.getString(
			getApplicationContext().getContentResolver(),
			Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
		);
		return s != null && s.contains(getPackageName() +  "/" + Service.class.getName());
	}
}