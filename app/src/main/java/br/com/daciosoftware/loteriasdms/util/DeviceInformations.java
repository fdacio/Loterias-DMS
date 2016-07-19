package br.com.daciosoftware.loteriasdms.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Dácio Braga on 11/06/2016.
 */
public class DeviceInformations {

    public enum InformationType{SCREEN, BLUETOOTH, GPS}
    private WindowManager wm;
    private DisplayMetrics metrics;

    public DeviceInformations(Context context, InformationType it ){
        switch (it) {
            case SCREEN:
                wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                metrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(metrics);
                break;

            case BLUETOOTH:
                //Aqui verificação de blue
                break;

            case GPS:
                //Aqui GPS
                break;
        }

    }

    public int getScreenDensity(){
        if(metrics != null) {
            return metrics.densityDpi;
        }else{
            return 0;
        }
    }

    public int getScreenHeight() {
        if(metrics != null) {
            return metrics.heightPixels;
        }else{
            return 0;
        }
    }

    public int getScreenWidth() {
        if(metrics != null) {
            return metrics.widthPixels;
        }else{
            return 0;
        }
    }


    public String getScreenName(){
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        int screenDensity = getScreenDensity();
        String name = screenWidth + "x" + screenHeight + "-" + screenDensity + "dpi";
        return name;

    }
}
