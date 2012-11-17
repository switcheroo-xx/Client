package se.agvard.switcheroo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter {

	// TODO Move to resources
	private static final int COLOR_ON = 0xff80ff80;
	private static final int COLOR_OFF = 0xffff8080;
	private static final int COLOR_DEFAULT = 0xffc0c0c0;

	private ArrayList<Device> mDevices;
	private LayoutInflater mLayoutInflator;
	private ShowErrorDialog mShowErrorDialog;

	public DeviceAdapter(Context context, ArrayList<Device> devices,
			ShowErrorDialog showErrorDialog) {
		mLayoutInflator = LayoutInflater.from(context);
		mDevices = devices;
		mShowErrorDialog = showErrorDialog;
	}

	@Override
	public int getCount() {
		return mDevices.size();
	}

	@Override
	public Object getItem(int position) {
		return mDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDevices.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflator.inflate(R.layout.device, parent,
					false);
		}

		final Device device = mDevices.get(position);

		((TextView) convertView.findViewById(R.id.name)).setText(mDevices.get(
				position).getName());

		// TODO Listeners in view inside adapter view

		Button buttonOn = (Button) convertView.findViewById(R.id.on);
		buttonOn.setTextColor(device.isOn() ? COLOR_ON : COLOR_DEFAULT);
		buttonOn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new VoidDeviceCommand() {
					@Override
					protected void onPostExecute(RequestResult result) {
						if (result.success()) {
							device.setOn(true);
							notifyDataSetChanged();
						} else {
							mShowErrorDialog.showErrorDialog(result
									.getErrorText());
						}
					};
				}.execute(device.getId(), VoidDeviceCommand.COMMAND_ON);
			}
		});

		Button buttonOff = (Button) convertView.findViewById(R.id.off);
		buttonOff.setTextColor(device.isOn() ? COLOR_DEFAULT : COLOR_OFF);
		buttonOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new VoidDeviceCommand() {
					@Override
					protected void onPostExecute(RequestResult result) {
						if (result.success()) {
							device.setOn(false);
							notifyDataSetChanged();
						} else {
							mShowErrorDialog.showErrorDialog(result
									.getErrorText());
						}
					};
				}.execute(device.getId(), VoidDeviceCommand.COMMAND_OFF);
			}
		});

		return convertView;
	}
}
