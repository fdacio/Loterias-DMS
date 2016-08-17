package br.com.daciosoftware.loteriasdms.confiraseujogo;

import br.com.daciosoftware.loteriasdms.dao.Sorteio;

/**
 * Created by Dácio Braga on 01/08/2016.
 */
public class SorteioAcerto implements Comparable<SorteioAcerto>{

    private Sorteio sorteio;
    private int qtdeAcertos;
    private int[] dezenasAcertos;

    public SorteioAcerto() {}

    public Sorteio getSorteio() {
        return sorteio;
    }

    public void setSorteio(Sorteio sorteio) {
        this.sorteio = sorteio;
    }

    public int getQtdeAcertos() {
        return qtdeAcertos;
    }

    public void setQtdeAcertos(int qtdeAcertos) {
        this.qtdeAcertos = qtdeAcertos;
    }

    public int[] getDezenasAcertos() {
        return dezenasAcertos;
    }

    public void setDezenasAcertos(int[] dezenasAcertos) {
        this.dezenasAcertos = dezenasAcertos;
    }

    @Override
    public int compareTo(SorteioAcerto another) {
        return new Integer(qtdeAcertos).compareTo(another.getQtdeAcertos());
    }
}
