package br.com.daciosoftware.loteriasdms.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dácio Braga on 25/07/2016.
 */
public class DownloadFile {

    public DownloadFile() {
    }

    /**
     *
     * @param url - Local do arquivo na web
     * @param outFile - Caminho completo ondo deve baixar o aquivo
     * @throws IOException
     */
    public void downloadFile(String url, String outFile) throws IOException{

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

            InputStream inputStream = conn.getInputStream();
            FileOutputStream fileOutput = new FileOutputStream(outFile);
            try {
                byte[] buffer = new byte[1024];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) != -1) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
            } finally {
                fileOutput.close();
                inputStream.close();
                conn.disconnect();
            }

    }

}
