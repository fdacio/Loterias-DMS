package br.com.daciosoftware.loteriasdms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Dácio Braga on 22/07/2016.
 */
public class Decompress {
    private String _zipFile;
    private String _location;

    public Decompress(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }

    public void unzip() {
        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
