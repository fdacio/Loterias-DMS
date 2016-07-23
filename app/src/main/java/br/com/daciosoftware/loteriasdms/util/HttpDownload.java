package br.com.daciosoftware.loteriasdms.util;

import android.renderscript.ScriptGroup;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dácio Braga on 22/07/2016.
 */
public class HttpDownload {

    public HttpDownload(){}

    public void downloadFile(String url, String outFile, Integer progress) throws IOException {
        URL urlParse = new URL(url);
        HttpURLConnection  conn = (HttpURLConnection) urlParse.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.connect();
        int lenghtOfFile = conn.getContentLength();
        InputStream input = conn.getInputStream();
        OutputStream output = new FileOutputStream(outFile);
        try{
            byte[] buffer = new byte[1024];
            int len;
            long total = 0;
            while((len = input.read(buffer))>0){
                output.write(buffer, 0, len);
                total += len;
                progress = (int)((total*100)/lenghtOfFile);
            }
        }finally {
            output.close();
            input.close();
            conn.disconnect();
        }
    }
}

