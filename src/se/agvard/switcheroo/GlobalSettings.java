package se.agvard.switcheroo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GlobalSettings {

	private static final String SHARED_PREFERENCES_NAME = "global_settings";
	private static final String SHARED_PREFERENCES_KEY_HOST = "host";
	private static final String SHARED_PREFERENCES_KEY_PORT = "port";
	private static GlobalSettings mGlobalSettings;
	private String mHost;
	private int mPort;
	private SharedPreferences mSharedPreferences;

	private GlobalSettings(Context context) {
		mSharedPreferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		mHost = mSharedPreferences.getString(SHARED_PREFERENCES_KEY_HOST, "");
		mPort = mSharedPreferences.getInt(SHARED_PREFERENCES_KEY_PORT, 0);
	}

	public static void init(Context context) {
		if (mGlobalSettings == null) {
			mGlobalSettings = new GlobalSettings(context);
		}
	}

	public static GlobalSettings get() {
		if (mGlobalSettings == null) {
			throw new IllegalStateException();
		}

		return mGlobalSettings;
	}

	public String getHost() {
		return mHost;
	}

	public int getPort() {
		return mPort;
	}

	public void set(String host, int port) {
		mHost = host;
		mPort = port;
		
		Editor edit = mSharedPreferences.edit();
		edit.putString(SHARED_PREFERENCES_KEY_HOST, host);
		edit.putInt(SHARED_PREFERENCES_KEY_PORT, port);
		edit.commit();
	}

}
