package br.com.daciosoftware.loteriasdms.configuracoes.urls;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;

/**
 * Created by fdacio on 31/08/16.
 */
public class UrlsDAO {

    private Context context;

    public UrlsDAO(Context context) {
        this.context = context;
    }

    public List<Urls> listAll() {
        List<Urls> listUrls = new ArrayList<>();
        listUrls.add(getUrls(TypeSorteio.MEGASENA));
        listUrls.add(getUrls(TypeSorteio.LOTOFACIL));
        listUrls.add(getUrls(TypeSorteio.QUINA));
        return listUrls;
    }


    private Urls getUrls(TypeSorteio typeSorteio) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, 0);
        switch (typeSorteio) {
            case MEGASENA:
                return new Urls(typeSorteio,
                        context.getResources().getString(R.string.mega_sena),
                        sharedPreferences.getString(Constantes.URL_RESULTADOS_MEGASENA, Constantes.URL_RESULTADOS_MEGASENA_DEFAULT),
                        sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA_DEFAULT));

            case LOTOFACIL:
                return new Urls(typeSorteio,
                        context.getResources().getString(R.string.lotofacil),
                        sharedPreferences.getString(Constantes.URL_RESULTADOS_LOTOFACIL, Constantes.URL_RESULTADOS_LOTOFACIL_DEFAULT),
                        sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL, Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL_DEFAULT));

            case QUINA:
                return new Urls(typeSorteio,
                        context.getResources().getString(R.string.quina),
                        sharedPreferences.getString(Constantes.URL_RESULTADOS_QUINA, Constantes.URL_RESULTADOS_QUINA_DEFAULT),
                        sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_QUINA, Constantes.URL_ARQUIVO_RESULTADOS_QUINA_DEFAULT));

            default:
                return null;
        }

    }

    public void save(Urls urls) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (urls.getTypeSorteio()) {
            case MEGASENA:
                editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, urls.getUrlArquivo());
                editor.putString(Constantes.URL_RESULTADOS_MEGASENA, urls.getUrlResultado());
                break;
            case LOTOFACIL:
                editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL, urls.getUrlArquivo());
                editor.putString(Constantes.URL_RESULTADOS_LOTOFACIL, urls.getUrlResultado());
                break;

            case QUINA:
                editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_QUINA, urls.getUrlArquivo());
                editor.putString(Constantes.URL_RESULTADOS_QUINA, urls.getUrlResultado());
                break;
        }

        editor.commit();
    }

    public void saveDefaulValues() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA_DEFAULT);
        editor.putString(Constantes.URL_RESULTADOS_MEGASENA, Constantes.URL_RESULTADOS_MEGASENA_DEFAULT);
        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL, Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL_DEFAULT);
        editor.putString(Constantes.URL_RESULTADOS_LOTOFACIL, Constantes.URL_RESULTADOS_LOTOFACIL_DEFAULT);
        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_QUINA, Constantes.URL_ARQUIVO_RESULTADOS_QUINA_DEFAULT);
        editor.putString(Constantes.URL_RESULTADOS_QUINA, Constantes.URL_RESULTADOS_QUINA_DEFAULT);

        editor.commit();
    }

}
