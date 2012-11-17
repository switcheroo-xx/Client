package se.agvard.switcheroo;

import java.util.Iterator;
import java.util.LinkedList;

public class Room implements DeviceListItem {

    private String mLabel;

    private LinkedList<Device> mDevices;

    public Room(String label, LinkedList<Device> devices) {
        mLabel = label;
        mDevices = devices;
    }

    public String getLabel() {
        return mLabel;
    }

    @Override
    public Status getStatus() {
        Iterator<Device> iterator = mDevices.iterator();

        // Either all has the same status or status is inconclusive
        Status status = iterator.next().getStatus();
        while (iterator.hasNext()) {
            if (iterator.next().getStatus() != status) {
                return Status.INCONCLUSIVE;
            }
        }

        return status;
    }

    // TODO Might be better not to expose this directly
    public LinkedList<Device> getDevices() {
        return mDevices;
    }

    @Override
    public boolean isOn() {
        return getStatus() == Status.ON;
    }

    @Override
    public boolean isOff() {
        return getStatus() == Status.OFF;
    }
}
