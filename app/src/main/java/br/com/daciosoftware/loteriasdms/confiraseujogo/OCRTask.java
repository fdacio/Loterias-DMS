package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.MyFileUtil;

/**
 * Created by Dácio Braga on 30/07/2016.
 */
public class OCRTask extends AsyncTask<Bitmap, String, String> {

    private Context context;
    private ProgressDialog progressDialog;
    private Bitmap bmp;


    public OCRTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        bmp = params[0];

        /*
        Tratamento do bpm
        */
        //bmp = BITMAP_RESIZER(bmp,bmp.getWidth(),bmp.getHeight());
        //bmp = convertToGrayscale(bmp);
        //bmp = RemoveNoise(bmp);

        bmp = resizeBitmap(bmp, bmp.getWidth(), bmp.getHeight() / 5);

        String datapath = MyFileUtil.getDefaultDirectoryApp() + "/tesseract/";
        checkFile(new File(datapath + "tessdata/"));
        String language = "eng";

        TessBaseAPI baseApi = new TessBaseAPI();
        //baseApi.init("/storage/sdcard0/tesseract/", "eng");
        baseApi.init(datapath, language);
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*  ()_+=-[]}{" + ";:'\"\\|~`,./<>?");
        baseApi.setDebug(true);
        baseApi.setImage(bmp);

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        return recognizedText;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        String msg = context.getResources().getString(R.string.processando_imagem);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setOnCancelListener(new cancelTaskOCR(this));
        progressDialog.show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String retorno) {
        progressDialog.dismiss();
    }


    private class cancelTaskOCR implements DialogInterface.OnCancelListener {

        private AsyncTask task;

        public cancelTaskOCR(AsyncTask task) {
            this.task = task;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            new DialogBox(context,
                    DialogBox.DialogBoxType.QUESTION,
                    "Processamento",
                    "Deseja cancelar o processo?",
                    new DialogInterface.OnClickListener() {//Resposta SIM do DialogBox Question
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            task.cancel(true);
                        }
                    },
                    new DialogInterface.OnClickListener() {//Resposta NÃO do DialogBox Question
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            progressDialog.show();
                        }
                    }

            ).show();
        }
    }

    /**
     * Métodos para configurar diretório e arquivos do Tess
     */

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists() && dir.mkdirs()) {
            String datafilepath = dir.getAbsolutePath() + "/eng.traineddata";
            copyFiles(datafilepath);
        }
        //The directory exists, but there is no data file in it
        if (dir.exists()) {
            String datafilepath = dir.getAbsolutePath() + "/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(datafilepath);
            }
        }
    }

    private void copyFiles(String datafilepath) {
        try {
            //location we want the file to be at

            //get access to AssetManager
            AssetManager assetManager = context.getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(datafilepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Metodos para tratar a imagem
     */

    private Bitmap adjustRotate(Bitmap bmpOriginal, String pathImage) {

        Bitmap bmpWithRotateAdjust = bmpOriginal;

        try {
            ExifInterface exif = new ExifInterface(pathImage);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                int w = bmpOriginal.getWidth();
                int h = bmpOriginal.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                bmpWithRotateAdjust = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, mtx, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpWithRotateAdjust.copy(Bitmap.Config.ARGB_8888, true);
    }

    public static Bitmap convertToGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public Bitmap RemoveNoise(Bitmap bmap) {
        for (int x = 0; x < bmap.getWidth(); x++) {
            for (int y = 0; y < bmap.getHeight(); y++) {
                int pixel = bmap.getPixel(x, y);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                if (R < 162 && G < 162 && B < 162)
                    bmap.setPixel(x, y, Color.BLACK);
            }
        }
        for (int x = 0; x < bmap.getWidth(); x++) {
            for (int y = 0; y < bmap.getHeight(); y++) {
                int pixel = bmap.getPixel(x, y);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                if (R > 162 && G > 162 && B > 162)
                    bmap.setPixel(x, y, Color.WHITE);
            }
        }
        return bmap;
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }
}

