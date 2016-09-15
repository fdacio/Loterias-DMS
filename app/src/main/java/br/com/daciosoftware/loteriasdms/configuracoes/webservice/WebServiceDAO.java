package br.com.daciosoftware.loteriasdms.configuracoes.webservice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.util.Constantes;

/**
 * Created by fdacio on 01/09/16.
 */
public class WebServiceDAO {
    private Context context;

    public WebServiceDAO(Context context) {
        this.context = context;
    }

    public List<WebService> listAll() {
        List<WebService> list = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, 0);
        String ulrWebService = sharedPreferences.getString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
        WebService webService = new WebService(ulrWebService);
        list.add(webService);
        return list;
    }

    public void save(WebService webService) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constantes.URL_WEB_SERVICE, webService.getUrlWebService());
        editor.commit();
    }

    public void saveDefaulValues() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
        editor.commit();
    }


}
