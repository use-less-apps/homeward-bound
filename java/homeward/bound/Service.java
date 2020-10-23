package homeward.bound;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

public class Service extends android.app.Service {

	static final int notificationID = 1;
	static final String notificationChannelID = "homeward-bound";

	public IBinder onBind(Intent i) {
		return null;
	}

	public int onStartCommand(Intent i, int flags, int startId) {
		return super.onStartCommand(i, flags, startId);
	}

	final EventReceiver r = new EventReceiver();
	ResolveInfo ri;

	public void onCreate() {
		ri = getDefaultAppLauncher();
		r.register();
	}

	public void onDestroy() {
		r.unregister();
	}

	ResolveInfo getDefaultAppLauncher() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		return getPackageManager().resolveActivity(i, PackageManager.MATCH_DEFAULT_ONLY);
	}

	class EventReceiver extends BroadcastReceiver {
		// If the screen is turned on with lock button, but is followed
		// by a fingerprint or passcode to unlock, the order will be
		// "screen on" followed by "user present".  Confusingly, if
		// face-unlock or fingreprint unlock are used, then the order
		// is "user present" followed by "screen on" (at least 500
		// milliseconds later). So, aggressively go home in all cases.
		public void onReceive(Context c, Intent i) {
			switch (i.getAction()) {
			case Intent.ACTION_SCREEN_OFF:
				ri = getDefaultAppLauncher();
			case Intent.ACTION_SCREEN_ON:
			case Intent.ACTION_USER_PRESENT:
				goHome();
			}
		}

		void register() {
			for (String s : new String[]{Intent.ACTION_SCREEN_OFF, Intent.ACTION_SCREEN_ON, Intent.ACTION_USER_PRESENT }) {
				registerReceiver(this, new IntentFilter(s));
			}
		}

		public void unregister() {
			unregisterReceiver(this);
		}
	}

	void goHome() {
		Intent i = Intent.makeMainActivity(
			new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name)
		);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  		Service.this.startActivity(i); 
	}
}
