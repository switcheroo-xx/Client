package se.agvard.switcheroo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import se.agvard.switcheroo.GetAvailableDevices.GetAvailableDevicesResult;
import android.os.AsyncTask;
import android.util.Log;

class GetAvailableDevices extends
		AsyncTask<Void, Void, GetAvailableDevicesResult> {

	public class GetAvailableDevicesResult extends RequestResult {
		public ArrayList<Device> devices;

		private GetAvailableDevicesResult(String errorText) {
			super(errorText);
		}

		private GetAvailableDevicesResult(ArrayList<Device> devices) {
			super();
			this.devices = devices;
		}
	}

	@Override
	protected GetAvailableDevicesResult doInBackground(Void... params) {
		Log.v(Util.tag(this), "Background task started.");

		URL url = null;
		try {
			GlobalSettings globalSettings = GlobalSettings.get();
			url = new URL("http", globalSettings.getHost(),
					globalSettings.getPort(), "?command=list");
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

		// Get available devices
		int lastIndexOfSpace = resLines.get(2).lastIndexOf(" ");
		final int nbrOfDevices = Integer.parseInt(resLines.get(2).substring(
				lastIndexOfSpace + 1));

		ArrayList<Device> devices = new ArrayList<Device>(nbrOfDevices);
		for (int i = 0; i < nbrOfDevices; ++i) {
			StringTokenizer st = new StringTokenizer(resLines.get(i + 3), "\t");

			int id = Integer.parseInt(st.nextToken());
			String name = st.nextToken();
			String status = st.nextToken();
			Log.v(Util.tag(this), "id <" + id + ">, name <" + name
					+ ">, status <" + status + ">");
			boolean on = status.equals("ON");
			devices.add(new Device(id, name, on));
		}

		Log.v(Util.tag(this), "Background task finished.");

		return new GetAvailableDevicesResult(devices);
	}
}