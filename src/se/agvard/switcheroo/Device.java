package se.agvard.switcheroo;

class Device {

	private int mId;
	private String mName;
	private boolean mOn;

	Device(int id, String name, boolean on) {
		mId = id;
		mName = name;
		mOn = on;
	}

	public int getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public boolean isOn() {
		return mOn;
	}

	public void setOn(boolean on) {
		mOn = on;
	}
}