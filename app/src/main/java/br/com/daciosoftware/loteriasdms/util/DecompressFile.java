package br.com.daciosoftware.loteriasdms.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Dácio Braga on 22/07/2016.
 */
public class DecompressFile {
    private ZipInputStream zin;
    private FileOutputStream fout;


    public DecompressFile() {}

    public String unzip(String zipFile, String outFile) throws IOException {
        zin = new ZipInputStream(new FileInputStream(zipFile));
        fout = new FileOutputStream(outFile);
        ZipEntry ze;
        String fileReturn = null;
        try {
            while ((ze = zin.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String fileName = ze.getName().toLowerCase();
                    if (fileName.contains("htm")) {
                        int c;
                        while ((c = zin.read()) != -1) {
                            fout.write(c);
                        }
                        fileReturn = outFile;
                    }
                }
            }
        } finally {
            zin.closeEntry();
            fout.close();
            zin.close();
        }

        return fileReturn;

    }


    public void closeResources() throws IOException{
        if(zin != null) {
            zin.close();
            zin.closeEntry();
        }

        if(fout != null)
            fout.close();
    }

}
