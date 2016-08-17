package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.support.annotation.NonNull;

/**
 * Created by Dácio Braga on 05/08/2016.
 */
public class MaisSorteada implements Comparable<MaisSorteada>{

    private int dezena;
    private int qtdeVezes;
    private boolean selecionada;

    public MaisSorteada(){}

    public int getDezena() {
        return dezena;
    }

    public void setDezena(int dezena) {
        this.dezena = dezena;
    }

    public int getQtdeVezes() {
        return qtdeVezes;
    }

    public void setQtdeVezes(int qtdeVezes) {
        this.qtdeVezes = qtdeVezes;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }

    @Override
    public int compareTo(@NonNull MaisSorteada another) {
        if(this.getQtdeVezes() > another.getQtdeVezes()) return 1;
        else if(this.getQtdeVezes() < another.getQtdeVezes()) return -1;
        else return 0;
        //return new Integer(qtdeVezes).compareTo(new Integer(another.getQtdeVezes()));
    }
}
