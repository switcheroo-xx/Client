package se.agvard.switcheroo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class SwitcherooActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.v(Util.tag(this), "onCreate");

		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getAvailableDevices();
			}
		});
		
		getAvailableDevices();
	}

	private void getAvailableDevices() {
		new GetAvailableDevices() {
			@Override
			protected void onPostExecute(ArrayList<Device> devices) {
				ListView listView = (ListView) findViewById(R.id.list);
				DeviceAdapter adapter = new DeviceAdapter(getBaseContext(),
						devices);
				listView.setAdapter(adapter);
			};
		}.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		findViewById(R.id.button).setOnClickListener(null);
	}

}