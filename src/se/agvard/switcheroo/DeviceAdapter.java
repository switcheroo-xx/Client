package se.agvard.switcheroo;

import java.util.ArrayList;
import java.util.LinkedList;

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
	private static final int BACKGROUND_ROOM = 0x20ffffff;
	private static final int BACKGROUND_DEVICE = 0x00000000;

	private LayoutInflater mLayoutInflator;
	private ShowErrorDialog mShowErrorDialog;
	private ArrayList<DeviceListItem> mDeviceListItems;

	public DeviceAdapter(Context context,
			ArrayList<DeviceListItem> deviceListItems,
			ShowErrorDialog showErrorDialog) {
		mLayoutInflator = LayoutInflater.from(context);
		mDeviceListItems = deviceListItems;
		mShowErrorDialog = showErrorDialog;
	}

	@Override
	public int getCount() {
		return mDeviceListItems.size();
	}

	@Override
	public DeviceListItem getItem(int position) {
		return mDeviceListItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflator.inflate(R.layout.device, parent,
					false);
		}

		final DeviceListItem item = mDeviceListItems.get(position);

		TextView label = ((TextView) convertView.findViewById(R.id.label));
		Button buttonOn = (Button) convertView.findViewById(R.id.on);
		Button buttonOff = (Button) convertView.findViewById(R.id.off);

		// TODO Listeners in view inside adapter view

		if (item instanceof Room) {
			convertView.setBackgroundColor(BACKGROUND_ROOM);
		} else {
			convertView.setBackgroundColor(BACKGROUND_DEVICE);
		}

		label.setText(item.getLabel());

		buttonOn.setTextColor(item.isOn() ? COLOR_ON : COLOR_DEFAULT);
		buttonOn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setOn(item, true);
			}
		});

		buttonOff.setTextColor(item.isOff() ? COLOR_OFF : COLOR_DEFAULT);
		buttonOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setOn(item, false);
			}
		});

		return convertView;
	}

	protected void setOn(final DeviceListItem item, final boolean on) {
		LinkedList<Device> devices;

		// TODO Might be better to handle this differently
		if (item instanceof Room) {
			Room r = (Room) item;
			devices = r.getDevices();
		} else {
			devices = new LinkedList<Device>();
			devices.add((Device) item);
		}

		for (final Device device : devices) {
			new VoidDeviceCommand() {
				@Override
				protected void onPostExecute(RequestResult result) {
					if (result.success()) {
						device.setOn(on);
						notifyDataSetChanged();
					} else {
						mShowErrorDialog.showErrorDialog(result.getErrorText());
					}
				};
			}.execute(device.getId(), on ? VoidDeviceCommand.COMMAND_ON
					: VoidDeviceCommand.COMMAND_OFF);
		}
	}
}
