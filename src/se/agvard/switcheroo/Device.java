package se.agvard.switcheroo;

class Device implements DeviceListItem {

	private final int mId;
	private final String mLabel;
	private boolean mOn;

	Device(int id, String label, boolean on) {
		mId = id;
		mLabel = label;
		mOn = on;
	}

	public int getId() {
		return mId;
	}

	public String getLabel() {
		return mLabel;
	}

	public Status getStatus() {
		return mOn ? Status.ON : Status.OFF;
	}

	public void setOn(boolean on) {
		mOn = on;
	}

	@Override
	public boolean isOn() {
		return mOn;
	}

	@Override
	public boolean isOff() {
		return !mOn;
	}
}