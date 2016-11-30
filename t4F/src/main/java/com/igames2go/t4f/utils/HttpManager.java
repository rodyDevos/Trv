
package com.igames2go.t4f.utils;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpManager {

    public static Object getResponse(String url, boolean returnsStream)
            throws NullPointerException, ClientProtocolException, IOException {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            Object response = null;

            HttpGet get = new HttpGet(url);

            // setProxy(httpClient);
            if (returnsStream) {
                HttpResponse httpResponse = httpClient.execute(get);
                return httpResponse.getEntity().getContent();
            } else {
                ResponseHandler<String> handlerString = new BasicResponseHandler();

                return httpClient.execute(get, handlerString);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public static void setProxy(DefaultHttpClient httpclient) {
        final String PROXY_IP = "";
        final int PROXY_PORT = 8080;

        /*
         * System.out.println("HOST, BEFORE: " +
         * httpclient.getParams().getParameter( ConnRoutePNames.DEFAULT_PROXY));
         */

        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(PROXY_IP, PROXY_PORT),
                new UsernamePasswordCredentials("", ""));

        HttpHost proxy = new HttpHost(PROXY_IP, PROXY_PORT);

        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                proxy);

        /*
         * System.out.println("HOST, AFTER: " +
         * httpclient.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY));
         */
    }

    public static String postData(String URL, String XML) {
        try {
            HttpURLConnection connection = null;
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestProperty("User-Agent", "Android");
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("SOAPAction",
                    "https://www.nsuok.edu/UpdateScore");

            StringBuffer requestBody = new StringBuffer();
            requestBody.append(XML);
            connection.setRequestProperty("Content-Length",
                    String.valueOf(requestBody.toString().length()));

            connection.connect();

            DataOutputStream dataOS = new DataOutputStream(
                    connection.getOutputStream());
            dataOS.writeBytes(requestBody.toString());

            dataOS.flush();
            dataOS.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStream in = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                StringBuffer data = new StringBuffer();
                int c;
                while ((c = isr.read()) != -1) {
                    data.append((char) c);
                }
                Log.i("response", data.toString());
                return new String(data.toString());
            } else {
                Log.e("responseCode",
                        responseCode + " " + connection.getResponseMessage());
                return null;
            }

        } catch (IOException e) {
            Log.d("IERROR", e.getMessage());
            return null;
        }

    }
}
