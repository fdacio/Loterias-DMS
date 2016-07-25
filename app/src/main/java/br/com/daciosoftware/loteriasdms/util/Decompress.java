package br.com.daciosoftware.loteriasdms.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Dácio Braga on 22/07/2016.
 */
public class Decompress {
    private String _zipFile;
    private String _location;
    private String _htmFileName;
    private Context _context;

    public Decompress(Context context, String zipFile, String location, String htmlFileName) {
        _context = context;
        _zipFile = zipFile;
        _location = location;
        _htmFileName = htmlFileName;

        _dirChecker("");
    }

    public String unzip() throws IOException {
        String fileReturn = null;
        FileInputStream fin = new FileInputStream(_zipFile);
        ZipInputStream zin = new ZipInputStream(fin);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null ) {
            if (ze.isDirectory()) {
                _dirChecker(ze.getName());
            } else {
                String mimeType = getMimeType(new File(ze.getName()));
                //Log.i(Constantes.CATEGORIA, " mimeType: "+mimeType);
                if(mimeType.toLowerCase().contains("htm")) {
                    String fileOutputName = _location + _htmFileName;
                    //Log.i(Constantes.CATEGORIA, "File unzip:"+fileOutputName);
                    FileOutputStream fout = new FileOutputStream(fileOutputName);

                    int c;
                    while ((c = zin.read())!=-1) {
                        //Log.i(Constantes.CATEGORIA, "zin.read():"+c);
                        fout.write(c);
                    }
                    fileReturn = fileOutputName;
                    zin.closeEntry();
                    fout.close();
                }
            }

        }
        zin.close();
        fin.close();
        return fileReturn;

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }


    private String getMimeType(File file) {
        Uri uri = Uri.fromFile(file);
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = _context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

}
