package br.com.daciosoftware.loteriasdms.configuracoes.webservice;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.UrlDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;

/**
 * Created by fdacio on 01/09/16.
 */
public class WebServiceDAO extends UrlDAO {

    private Context context;


    public WebServiceDAO(Context context) {
        this.context = context;
    }


    @Override
    public String getUrl() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, 0);
        return sharedPreferences.getString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
    }

    @Override
    public void save(String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constantes.URL_WEB_SERVICE, url);
        editor.commit();
    }

    @Override
    public String getHeader() {
        return context.getResources().getString(R.string.web_service);
    }

    @Override
    public String getLabel() {
        return context.getResources().getString(R.string.label_url_web_service);
    }

    public void saveDefaulValues() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
        editor.commit();
    }

}