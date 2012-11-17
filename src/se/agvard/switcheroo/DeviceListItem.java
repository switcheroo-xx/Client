package se.agvard.switcheroo;

public interface DeviceListItem {

    enum Status {
        OFF, ON, INCONCLUSIVE
    }

    public String getLabel();

    public Status getStatus();

    public boolean isOn();

    public boolean isOff();

}
