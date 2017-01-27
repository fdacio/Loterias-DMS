package br.com.daciosoftware.loteriasdms.configuracoes;

import android.content.Context;

import br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados.ArquivoResultadoUrlLotofacilDAO;
import br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados.ArquivoResultadoUrlMegasenaDAO;
import br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados.ArquivoResultadoUrlQuinaDAO;
import br.com.daciosoftware.loteriasdms.configuracoes.webservice.WebServiceDAO;

/**
 * Created by fdacio on 23/01/17.
 */
public class UrlDAOFactory {

    public static UrlDAO getInstance(Context context, TypeUrl typeUrl) {

        switch (typeUrl) {
            case WEBSERVICE:
                return new WebServiceDAO(context);
            case MEGASENA:
                return new ArquivoResultadoUrlMegasenaDAO(context);
            case LOTOFACIL:
                return new ArquivoResultadoUrlLotofacilDAO(context);
            case QUINA:
                return new ArquivoResultadoUrlQuinaDAO(context);
            default:
                return null;
        }
    }
}
