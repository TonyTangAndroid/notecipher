package info.guardianproject.notepadbot;

import android.app.Application;

public class App extends Application {

//	private CacheWordSettings mCWSettings;

	@Override
	public void onCreate() {
		super.onCreate();
//		mCWSettings = new CacheWordSettings(getApplicationContext());
//		mCWSettings.setEnableNotification(false);
		// mCWSettings.setNotificationIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,
		// NoteCipher.class), Intent.FLAG_ACTIVITY_NEW_TASK ));
	}

	// public CacheWordSettings getCWSettings() {
	// return mCWSettings;
	// }

}
