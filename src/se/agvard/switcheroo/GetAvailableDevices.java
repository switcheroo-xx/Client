package se.agvard.switcheroo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import se.agvard.switcheroo.GetAvailableDevices.GetAvailableDevicesResult;
import android.os.AsyncTask;
import android.util.Log;

class GetAvailableDevices extends AsyncTask<Void, Void, GetAvailableDevicesResult> {

    public class GetAvailableDevicesResult extends RequestResult {
        public ArrayList<DeviceListItem> deviceListItems;

        private GetAvailableDevicesResult(String errorText) {
            super(errorText);
        }

        public GetAvailableDevicesResult(ArrayList<DeviceListItem> deviceListItems) {
            super();
            this.deviceListItems = deviceListItems;
        }
    }

    @Override
    protected GetAvailableDevicesResult doInBackground(Void... params) {
        Log.v(Util.tag(this), "Background task started.");

        URL url = null;
        try {
            GlobalSettings globalSettings = GlobalSettings.get();
            url = new URL("http", globalSettings.getHost(), globalSettings.getPort(),
                    "?command=list");
        } catch (MalformedURLException e) {
            return new GetAvailableDevicesResult(RequestResult.MALFORMED_URL);
        }

        BufferedReader in = null;
        final List<String> resLines = new LinkedList<String>();
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inLine;
            while ((inLine = in.readLine()) != null) {
                resLines.add(inLine);
            }
        } catch (IOException e) {
            return new GetAvailableDevicesResult(RequestResult.IO_OPEN);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // NOP
            }
        }

        // Get number of available devices from server
        int lastIndexOfSpace = resLines.get(2).lastIndexOf(" ");
        final int nbrOfDevices = Integer.parseInt(resLines.get(2).substring(lastIndexOfSpace + 1));

        // Get device information from server
        HashMap<String, LinkedList<Device>> roomMap = new HashMap<String, LinkedList<Device>>();
        for (int i = 0; i < nbrOfDevices; ++i) {
            // Parse device
            StringTokenizer st = new StringTokenizer(resLines.get(i + 3), "\t");
            int id = Integer.parseInt(st.nextToken());
            String roomAndName = st.nextToken();
            String roomLabel = roomAndName.substring(0, roomAndName.indexOf(": "));
            String deviceLabel = roomAndName.substring(roomAndName.indexOf(": ") + 2);
            String status = st.nextToken();
            boolean on = status.equals("ON");
            Device device = new Device(id, deviceLabel, on);

            Log.v(Util.tag(this), "id <" + id + ">, room and name <" + roomAndName + ">, status <"
                    + status + ">");

            // room -> id
            LinkedList<Device> roomIds = roomMap.get(roomLabel);
            if (roomIds == null) {
                roomIds = new LinkedList<Device>();
                roomMap.put(roomLabel, roomIds);
            }
            roomIds.add(device);
        }

        // Create list of rooms and devices
        ArrayList<DeviceListItem> deviceListItems = new ArrayList<DeviceListItem>();
        for (String roomLabel : roomMap.keySet()) {
            LinkedList<Device> devices = roomMap.get(roomLabel);
            deviceListItems.add(new Room(roomLabel, devices));
            for (Device device : devices) {
                deviceListItems.add(device);
            }
        }

        Log.v(Util.tag(this), "Background task finished. ");

        return new GetAvailableDevicesResult(deviceListItems);
    }
}