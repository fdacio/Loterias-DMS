package br.com.daciosoftware.loteriasdms.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dácio Braga on 25/07/2016.
 */
public class MyFileUtil {

    public static final String NAME_DIR_APP = "LoteriasDMS";
    private static final float SIZE_KB = 1024.0f;

    /**
     * @param context contexto da aplicação
     * @param file    arquivo para obter o MimeType
     * @return
     */
    public static String getMimeType(Context context, File file) {
        Uri uri = Uri.fromFile(file);
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static String getDefaultDirectoryApp() {
        String directory = Environment.getExternalStorageDirectory().getPath() + "/" + NAME_DIR_APP;
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath();
    }

    public static float getSizeBytes(File file) {
        return file.length() / SIZE_KB;
    }

    public static float getSizeMBytes(File file) {
        return getSizeBytes(file) / SIZE_KB;
    }

    public static float getSizeGBytes(File file) {
        return getSizeMBytes(file) / SIZE_KB;
    }

    /**
     * @param directory diretorio que contem os arquivos a serem deletada
     */
    public static void removeFilesInDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File toDelete : files) {
                toDelete.delete();
            }
        }
    }

    /**
     * @param directory diretorio que contem os arquivos a serem deletada
     * @param mimeType  Tipo a ser deletado
     * @param context   contexto da aplicação
     */
    public static void removeFilesInDirectory(File directory, String mimeType, Context context) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File toDelete : files) {
                if (getMimeType(context, toDelete).equals(mimeType)) {
                    toDelete.delete();
                }
            }
        }
    }

}
