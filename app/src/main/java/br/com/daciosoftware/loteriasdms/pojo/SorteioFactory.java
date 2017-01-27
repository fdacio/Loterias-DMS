package br.com.daciosoftware.loteriasdms.pojo;

import br.com.daciosoftware.loteriasdms.TypeSorteio;

/**
 * Created by fdacio on 15/10/16.
 */
public class SorteioFactory {

    public static Sorteio getInstance(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return new Megasena();
            case QUINA:
                return new Quina();
            case LOTOFACIL:
                return new Lotofacil();
            default:
                return null;
        }
    }

}
