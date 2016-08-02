package br.com.daciosoftware.loteriasdms.confiraseujogo;

import java.io.Serializable;

/**
 * Created by Dácio Braga on 01/08/2016.
 */
public class SeuJogo implements Serializable{

    private int numeroConcurso;
    private int[] dezenas;

    public SeuJogo() {}

    public int getNumeroConcurso() {
        return numeroConcurso;
    }

    public void setNumeroConcurso(int numeroConcurso) {
        this.numeroConcurso = numeroConcurso;
    }

    public int[] getDezenas() {
        return dezenas;
    }

    public void setDezenas(int[] dezenas) {
        this.dezenas = dezenas;
    }
}
