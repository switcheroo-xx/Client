package se.agvard.switcheroo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;

/**
 * execute(int deviceId, String command)
 */
class VoidDeviceCommand extends AsyncTask<Object, Void, Void> {

	public static final String COMMAND_ON = "on";
	public static final String COMMAND_OFF = "off";

	@Override
	protected Void doInBackground(Object... params) {
		String deviceId = ((Integer) params[0]).toString();
		String command = (String) params[1];

		if (!command.equals(COMMAND_ON) && !command.equals(COMMAND_OFF)) {
			throw new IllegalArgumentException();
		}
		
		URL url = null;
		try {
			url = new URL("http://andreas.agvard.se:1443/?command=" + command
					+ "&device=" + deviceId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// TODO Do we need to read the data?
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

		return null;
	}
}