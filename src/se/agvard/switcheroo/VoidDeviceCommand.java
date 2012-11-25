package se.agvard.switcheroo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

        GlobalSettings globalSettings = GlobalSettings.get();

        URL url = null;
        try {
            url = new URL("https", globalSettings.getHost(), globalSettings.getPort(), "?command="
                    + command + "&device=" + deviceId);
        } catch (MalformedURLException e) {
            return new RequestResult(RequestResult.MALFORMED_URL);
        }

        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            connection = Util.openHttpsURLConnection(url, globalSettings.getPassword());
            is = connection.getInputStream();
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
            if (connection != null) {
                connection.disconnect();
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