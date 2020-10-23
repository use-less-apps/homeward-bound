package homeward.bound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {

	public void onCreate(Bundle b) {
		super.onCreate(b);
		startService(new Intent(this, Service.class));
		finish();
	}
}
