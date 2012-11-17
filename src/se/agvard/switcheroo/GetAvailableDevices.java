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

import android.os.AsyncTask;

abstract class GetAvailableDevices extends
		AsyncTask<Void, Void, ArrayList<Device>> {

	@Override
	protected ArrayList<Device> doInBackground(Void... params) {
		URL url = null;
		try {
			url = new URL("http://andreas.agvard.se:1443/?command=list");
		} catch (MalformedURLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		} finally {
			try {
				in.close();
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
			StringTokenizer st = new StringTokenizer(resLines.get(i + 3) + i,
					"\t");
			Device device = new Device();
			device.id = Integer.parseInt(st.nextToken());
			device.name = st.nextToken();
			devices.add(device);
		}

		return devices;
	}

	abstract protected void onPostExecute(ArrayList<Device> result);
}