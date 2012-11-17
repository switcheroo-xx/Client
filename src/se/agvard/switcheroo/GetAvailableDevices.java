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
import android.util.Log;

abstract class GetAvailableDevices extends
		AsyncTask<Void, Void, ArrayList<Device>> {

	@Override
	protected ArrayList<Device> doInBackground(Void... params) {
		Log.v(Util.tag(this), "Background task started.");

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
			StringTokenizer st = new StringTokenizer(resLines.get(i + 3),
					"\t");

			int id = Integer.parseInt(st.nextToken());
			String name = st.nextToken();
			String status = st.nextToken();
			Log.v(Util.tag(this), "id <" + id + ">, name <" + name
					+ ">, status <" + status + ">");
			boolean on = status.equals("ON");
			devices.add(new Device(id, name, on));
		}

		Log.v(Util.tag(this), "Background task finished.");

		return devices;
	}

	abstract protected void onPostExecute(ArrayList<Device> result);
}