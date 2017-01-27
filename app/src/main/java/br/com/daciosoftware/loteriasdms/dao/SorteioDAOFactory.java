package br.com.daciosoftware.loteriasdms.dao;

import android.content.Context;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.db.contract.LotofacilContract;
import br.com.daciosoftware.loteriasdms.db.contract.MegasenaContract;
import br.com.daciosoftware.loteriasdms.db.contract.QuinaContract;

/**
 * Created by fdacio on 15/10/16.
 */
public class SorteioDAOFactory {

    /**
     * Padrão Factory
     *
     * @param context     - Contexto da Aplicação
     * @param typeSorteio - Tipo de Sorteio
     * @return DAO para o sorteio do tipo typeSorteio
     */
    public static SorteioDAO getInstance(Context context, TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return new MegasenaDAO(context, new MegasenaContract());

            case LOTOFACIL:
                return new LotofacilDAO(context, new LotofacilContract());

            case QUINA:
                return new QuinaDAO(context, new QuinaContract());

            default:
                return null;
        }

    }

}
