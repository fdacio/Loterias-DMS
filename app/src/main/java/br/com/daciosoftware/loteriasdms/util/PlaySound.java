package br.com.daciosoftware.loteriasdms.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

/**
 * Created by Dácio Braga on 01/07/2016.
 */
public class PlaySound implements MediaPlayer.OnCompletionListener{
    private MediaPlayer mediaPlayer;

    public PlaySound(Context context, int soundResource) {
        mediaPlayer = MediaPlayer.create(context, soundResource);
        mediaPlayer.setOnCompletionListener(this);
    }

    public void play() {
        new AsyncTaskPlaySound().execute();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private class AsyncTaskPlaySound extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
