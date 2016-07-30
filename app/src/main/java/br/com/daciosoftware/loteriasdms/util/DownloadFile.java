package br.com.daciosoftware.loteriasdms.util;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dácio Braga on 25/07/2016.
 */
public class DownloadFile {

    private InputStream inputStream;
    private FileOutputStream fileOutputStream;
    private HttpURLConnection conn;


    public DownloadFile() {}

    /**
     *
     * @param url - Local do arquivo na web
     * @param outFile - Caminho completo ondo deve baixar o aquivo
     * @throws IOException
     */
    public void downloadFileBinary(String url, String outFile) throws IOException{

            URL urlParse = new URL(url);
            conn = (HttpURLConnection) urlParse.openConnection();
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

            inputStream = conn.getInputStream();
            fileOutputStream = new FileOutputStream(outFile);
            try {
                byte[] buffer = new byte[1024];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
            } finally {
                fileOutputStream.close();
                inputStream.close();
                conn.disconnect();
            }

    }

    public void closeResources() throws IOException{
        if(fileOutputStream != null)
            fileOutputStream.close();
        if(inputStream != null)
            inputStream.close();
        if(conn != null)
            conn.disconnect();
    }

}
