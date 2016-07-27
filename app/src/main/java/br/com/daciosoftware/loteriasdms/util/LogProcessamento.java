package br.com.daciosoftware.loteriasdms.util;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Dácio Braga on 27/07/2016.
 */
public class LogProcessamento {

    private static final String pathFileLog = getDefaultDirectory("LoteriasDMS") + "/" + "log_processamento.txt";
    private static BufferedWriter writer;
    private static File fileLog;

    private LogProcessamento() {}

    public static void registraLog(String msg)throws IOException {
        if(fileLog == null){
            fileLog = new File(pathFileLog);
        }
        writer = new BufferedWriter(new FileWriter(fileLog));
        writer.write(timeToString(Calendar.getInstance())+": "+msg);
        writer.close();
    }


    private static String getDefaultDirectory(String defaultDirectory){
        String directory = Environment.getExternalStorageDirectory().getPath() + "/" +defaultDirectory;
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath();
    }


    private static String timeToString(Calendar data){
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault());
        return sdf.format(data.getTime());
    }

}
