package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * Created by fdacio on 14/08/16.
 */
public class JogoGerado implements Comparable<JogoGerado> {
    private int[] dezenas;

    public JogoGerado(int[] dezenas) {
        this.dezenas = dezenas;
    }

    public int[] getDezenas() {
        return dezenas;
    }

    @Override
    public int compareTo(@NonNull JogoGerado another) {
        return this.toString().compareTo(another.toString());
    }

    @Override
    public boolean equals(Object aJogoGerado) {
        if (aJogoGerado instanceof JogoGerado) {
            JogoGerado another = (JogoGerado) aJogoGerado;
            int[] dezenas = this.getDezenas();
            int[] anotherDezenas = another.getDezenas();
            Arrays.sort(dezenas);
            Arrays.sort(anotherDezenas);
            for (int i = 0; i < dezenas.length; i++) {
                if (dezenas[i] != anotherDezenas[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String lista = "";
        int[] dezenas = this.getDezenas();
        for (int dezena : dezenas) {
            lista += String.format("%02d ", dezena);
        }
        return lista;
    }

}
