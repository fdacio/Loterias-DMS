package br.com.daciosoftware.loteriasdms.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dácio Braga on 30/07/2016.
 */
public class DeviceInformation {
    private DeviceInformation() {}

    public static boolean isNetwork(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }
}
