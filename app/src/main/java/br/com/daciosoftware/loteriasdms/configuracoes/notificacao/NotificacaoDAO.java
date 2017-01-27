package br.com.daciosoftware.loteriasdms.configuracoes.notificacao;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.daciosoftware.loteriasdms.util.Constantes;

/**
 * Created by fdacio on 21/01/17.
 */
public class NotificacaoDAO {

    private Context context;

    public NotificacaoDAO(Context context) {
        this.context = context;
    }

    public boolean isNotificar() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, 0);
        return sharedPreferences.getBoolean(Constantes.NOTIFICAR_SORTEIO, Constantes.NOTIFICAR_SORTEIO_DEFAULT);

    }

    public void save(boolean isNotificar) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constantes.NOTIFICAR_SORTEIO, isNotificar);
        editor.apply();
    }

    public void saveDefaulValues() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constantes.NOTIFICAR_SORTEIO, Constantes.NOTIFICAR_SORTEIO_DEFAULT);
        editor.apply();
    }


}
