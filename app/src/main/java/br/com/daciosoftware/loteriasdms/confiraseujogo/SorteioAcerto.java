package br.com.daciosoftware.loteriasdms.confiraseujogo;

import br.com.daciosoftware.loteriasdms.dao.Sorteio;

/**
 * Created by Dácio Braga on 01/08/2016.
 */
public class SorteioAcerto extends Sorteio {
    private int qtdeAcertos;

    public int getQtdeAcertos() {
        return qtdeAcertos;
    }

    public void setQtdeAcertos(int qtdeAcertos) {
        this.qtdeAcertos = qtdeAcertos;
    }
}
