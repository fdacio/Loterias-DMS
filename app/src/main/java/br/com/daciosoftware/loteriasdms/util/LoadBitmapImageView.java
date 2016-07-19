package br.com.daciosoftware.loteriasdms.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Dácio Braga on 29/06/2016.
 */
public class LoadBitmapImageView {

    private ImageView imageView;
    private String pathFile;

    public LoadBitmapImageView(ImageView imageView, String pathFile) {
        this.imageView = imageView;
        this.pathFile = pathFile;
    }

    public boolean loadBitmap() {

        File imageFile = new File(this.pathFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

        if (imageFile.exists()) {
            try {
                Bitmap myBitmap = BitmapFactory.decodeFile(this.pathFile, options);
                this.imageView.setImageBitmap(myBitmap);
                return true;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
