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
			showDialog(
				"Homeward Bound is already running.",
				"Unlocking the phone should always start at the home screen." + "\n\n" +
				"To revert, open Homeward Bound in Accessibility Settings " +
				"(look for the above icon) " +
				"and toggle the 'Use service' switch."
			);
		} else {
			showDialog(
				"Enable this app in\n" + "'Accessibility Settings'",
				"Select Homeward Bound (look for the above icon) in Accessibilty Settings, " +
				"then enable the 'Use service' switch."
			);
		}
	}

	boolean isAccessibilityEnabled() {
		String s = Settings.Secure.getString(
			getApplicationContext().getContentResolver(),
			Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
		);
		return s != null && s.contains(getPackageName() +  "/" + Service.class.getName());
	}

	void showDialog(String title, String message) {
		new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
			.setIcon(R.drawable.house_anchor)
			.setCancelable(false)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton(
				"Open Accessibility Settings",
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
		i.setFlags(
			Intent.FLAG_ACTIVITY_NEW_TASK |
			Intent.FLAG_ACTIVITY_CLEAR_TOP |
			Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
		);
		startActivity(i);
		finish();
	}
}
