package br.com.daciosoftware.loteriasdms.configuracoes.valoraposta;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.ValorApostaDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;

/**
 * Created by fdacio on 01/02/17.
 */
public class ValorApostaQuinaDAO extends ValorApostaDAO {

    private Context context;

    public ValorApostaQuinaDAO(Context context) {
        this.context = context;
    }

    @Override
    public float getValor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, 0);
        return sharedPreferences.getFloat(Constantes.VALOR_APOSTA_QUINA, Constantes.VALOR_APOSTA_QUINA_DEFAULT);
    }

    @Override
    public void save(float valor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Constantes.VALOR_APOSTA_QUINA, valor);
        editor.commit();
    }

    @Override
    public String getHeader() {
        return this.context.getResources().getString(R.string.quina);
    }

    @Override
    public String getLabel() {
        return this.context.getResources().getString(R.string.label_valor_aposta);
    }

    @Override
    public void saveDefaulValues() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Constantes.VALOR_APOSTA_QUINA, Constantes.VALOR_APOSTA_QUINA_DEFAULT);
        editor.commit();

    }
}
