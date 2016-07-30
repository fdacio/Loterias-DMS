package br.com.daciosoftware.loteriasdms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dácio Braga on 26/07/2016.
 */
public class HttpConnection {


    public static String getContentJSON(String url) throws IOException {

        URL urlParse = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlParse.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            conn.disconnect();
            return "{\"Status\":\"end\"}";
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String retorno = sb.toString();

            return "{\"Status\":\"OK\","+retorno.substring(1);

        }finally {
            conn.disconnect();
            in.close();
        }

    }


    public static String getContentHTML(String url) throws IOException {

        URL urlParse = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlParse.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();


        boolean redirect = false;
        if (responseCode != HttpURLConnection.HTTP_OK) {


            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                    || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                redirect = true;
            }
        }

        if (redirect) {
            String newUrl = conn.getHeaderField("Location");
            String cookies = conn.getHeaderField("Set-Cookie");
            conn = (HttpURLConnection) new URL(newUrl).openConnection();
            if (cookies != null) {
                conn.setRequestProperty("Cookie", cookies);
            }
        }


        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }finally {
            conn.disconnect();
            in.close();
        }

    }


}
