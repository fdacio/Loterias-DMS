package br.com.daciosoftware.loteriasdms.configuracoes.valoraposta;

import android.content.Context;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.configuracoes.ValorApostaDAO;

/**
 * Created by fdacio on 01/02/17.
 */
public class ValorApostaDAOFactory {

    public static ValorApostaDAO getInstance(Context context, TypeSorteio typeSorteio) {

        switch (typeSorteio) {
            case MEGASENA:
                return new ValorApostaMegasenaDAO(context);
            case LOTOFACIL:
                return new ValorApostaLotofacilDAO(context);
            case QUINA:
                return new ValorApostaQuinaDAO(context);
            default:
                return null;
        }
    }

}
