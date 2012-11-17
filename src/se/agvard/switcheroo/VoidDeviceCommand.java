package se.agvard.switcheroo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

/**
 * execute(int deviceId, String command)
 */
public class VoidDeviceCommand extends AsyncTask<Object, Void, RequestResult> {

	public static final String COMMAND_ON = "on";
	public static final String COMMAND_OFF = "off";

	@Override
	protected RequestResult doInBackground(Object... params) {
		String deviceId = ((Integer) params[0]).toString();
		String command = (String) params[1];

		if (!command.equals(COMMAND_ON) && !command.equals(COMMAND_OFF)) {
			throw new IllegalArgumentException();
		}

		URL url = null;
		try {
			url = new URL("http", "andreas.agvard.se", 1443, "?command="
					+ command + "&device=" + deviceId);
			System.out.println("ANDREAS " + url);
		} catch (MalformedURLException e) {
			return new RequestResult(RequestResult.MALFORMED_URL);
		}

		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e) {
			return new RequestResult(RequestResult.IO_OPEN);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// NOP
				}
			}
		}

		// TODO Do we need to read the data?
		/*
		 * BufferedReader in = null; final List<String> resLines = new
		 * LinkedList<String>(); try { in = new BufferedReader(new
		 * InputStreamReader(url.openStream()));
		 * 
		 * String inLine; while ((inLine = in.readLine()) != null) {
		 * resLines.add(inLine); } } catch (IOException e) {
		 * e.printStackTrace(); } finally { try { in.close(); } catch
		 * (IOException e) { // NOP } }
		 */

		return new RequestResult();
	}
}