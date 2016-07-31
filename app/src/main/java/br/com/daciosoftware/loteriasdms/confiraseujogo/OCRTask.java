package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import br.com.daciosoftware.loteriasdms.util.DialogBox;

/**
 * Created by Dácio Braga on 30/07/2016.
 */
public class OCRTask extends AsyncTask<Bitmap, String, String> {

    private Context context;
    private boolean running = true;
    private String msg = "Processando Documento.\nAguarde...";
    private ProgressDialog progressDialog;


    public OCRTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        Bitmap bmp = params[0];
        /*
        Tratamento do bpm
        */

        //bmp = BITMAP_RESIZER(bmp,bmp.getWidth(),bmp.getHeight());
       // bmp = convertToGrayscale(bmp);
        //bmp = RemoveNoise(bmp);

        TessBaseAPI baseApi = new TessBaseAPI();

        baseApi.init("/storage/sdcard0/tesseract/", "eng");
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,"1234567890");
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST,"!@#$%^&*  ()_+=-[]}{" +";:'\"\\|~`,./<>?");
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
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setOnCancelListener(new cancelTaskOCR(this));
        progressDialog.show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(context, "Processamento cancelado!", Toast.LENGTH_SHORT).show();
        running = false;

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
     * Metodos para tratar a imagem
     */


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

    public Bitmap BITMAP_RESIZER(Bitmap bitmap, int newWidth, int newHeight) {
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

