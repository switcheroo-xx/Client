package se.agvard.switcheroo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class SwitcherooActivity extends Activity {

	private View mButtonRefresh;
	private ListView mListDevices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mButtonRefresh = findViewById(R.id.button_refresh);
		mButtonRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getAvailableDevices();
			}
		});

		mListDevices = (ListView) findViewById(R.id.list_devices);

		getAvailableDevices();
	}

	private void getAvailableDevices() {
		mListDevices.setAdapter(null);
		new GetAvailableDevices() {
			@Override
			protected void onPostExecute(GetAvailableDevicesResult result) {
				// Check if activity has been destroyed
				if (mListDevices == null) {
					return;
				}

				if (result.success()) {
					DeviceAdapter adapter = new DeviceAdapter(getBaseContext(),
							result.devices);
					mListDevices.setAdapter(adapter);
				} else {
					// TODO Handle gracefully
					throw new RuntimeException(result.getErrorText());
				}
			};
		}.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mButtonRefresh.setOnClickListener(null);
		mListDevices.setAdapter(null);

		mButtonRefresh = null;
		mListDevices = null;
	}

}