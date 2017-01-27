package br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.UrlDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;

/**
 * Created by fdacio on 31/08/16.
 */
public class ArquivoResultadoUrlMegasenaDAO extends UrlDAO {

    private Context context;

    public ArquivoResultadoUrlMegasenaDAO(Context context) {
        this.context = context;
    }

    @Override
    public String getUrl() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, 0);
        return sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA_DEFAULT);
    }

    @Override
    public void save(String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, url);
        editor.commit();

    }

    @Override
    public String getHeader() {
        return this.context.getResources().getString(R.string.mega_sena);
    }

    @Override
    public String getLabel() {
        return this.context.getResources().getString(R.string.label_url_arquivo_resultados);
    }

    @Override
    public void saveDefaulValues() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA_DEFAULT);
        editor.commit();
    }
}
