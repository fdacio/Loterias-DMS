package br.com.daciosoftware.loteriasdms.pojo;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Lotofacil extends Sorteio {

    private final String nome = "LOTOFÁCIL";
    private final TypeSorteio typeSorteio = TypeSorteio.LOTOFACIL;
    private final int totalDezenas = 15;


    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public TypeSorteio getTypeSorteio() {
        return this.typeSorteio;
    }

    @Override
    public int getTotalDezenas() {
        return this.totalDezenas;
    }

    @Override
    public String toString() {
        return "Lotofácil: Id:" + getId() + " Concurso:" + getNumero() + " Data:" + MyDateUtil.calendarToDateBr(getData());
    }

}
