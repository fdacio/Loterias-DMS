package br.com.daciosoftware.loteriasdms.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by Dácio Braga on 25/07/2016.
 */
public class FileUtil {

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


    public static String getDefaultDirectory(String defaultDirectory){
        String directory = Environment.getExternalStorageDirectory().getPath() + "/" +defaultDirectory;
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath();
    }
}
