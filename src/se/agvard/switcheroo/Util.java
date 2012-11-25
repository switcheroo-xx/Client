package se.agvard.switcheroo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Base64;

public class Util {

    public static String tag(Object o) {
        return o.getClass().getName();
    }

    /** Create a very trusting ssl context */
    private static SSLContext createDefaultSSLContext() {
        SSLContext ctx = null;

        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            ctx.init(null, new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } }, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return ctx;
    }

    /** Create a very trusting hostname verifier */
    private static HostnameVerifier createDefaultHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    private static String createDefaultAuthorizationHeader(String password)
            throws UnsupportedEncodingException {
        return "Basic "
                + Base64.encodeToString(("" + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
    }

    public static HttpsURLConnection openHttpsURLConnection(URL url, String password)
            throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", createDefaultAuthorizationHeader(password));
        connection.setHostnameVerifier(createDefaultHostnameVerifier());
        connection.setSSLSocketFactory(createDefaultSSLContext().getSocketFactory());
        return connection;
    }
}
