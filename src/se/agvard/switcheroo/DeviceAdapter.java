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

	private ArrayList<Device> mDevices;
	private LayoutInflater mLayoutInflator;

	public DeviceAdapter(Context context, ArrayList<Device> devices) {
		mLayoutInflator = LayoutInflater.from(context);
		mDevices = devices;
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
		return mDevices.get(position).id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflator.inflate(R.layout.device, parent,
					false);
		}

		((TextView) convertView.findViewById(R.id.name)).setText(mDevices
				.get(position).name);

		((Button) convertView.findViewById(R.id.on))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new VoidDeviceCommand().execute(mDevices.get(position).id, VoidDeviceCommand.COMMAND_ON);
					}
				});

		((Button) convertView.findViewById(R.id.off))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new VoidDeviceCommand().execute(mDevices.get(position).id, VoidDeviceCommand.COMMAND_OFF);
					}
				});

		return convertView;
	}

}
