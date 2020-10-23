package homeward.bound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class Main extends Activity {

	public void onCreate(Bundle b) {
		super.onCreate(b);
		if (isAccessibilityEnabled()) {
			toggleService();
		} else {
			showDialog();
		}
	}

	void toggleService() {
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

	void showDialog() {
		new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
			.setIcon(R.drawable.house_anchor)
			.setCancelable(false)
			.setTitle("Enable this app in\n" + "'Accessibility Settings'")
			.setMessage(
				"Select Homeward Bound in Accessibilty Settings, " +
				"then enable 'Use service' and 'Allow'."
			)
			.setPositiveButton(
				"Open Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int ID) {
						openAccessibilitySettings();
					}
				}
			)
			.setNegativeButton(
				"Exit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int ID) {
						finish();
					}
				}
			)
			.create()
			.show();
	}

	void openAccessibilitySettings() {
		Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(i);
		finish();
	}
}
