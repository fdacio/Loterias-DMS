package br.com.daciosoftware.loteriasdms.pojo;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Quina extends Sorteio{

    private final String nome = "QUINA";
    private final TypeSorteio typeSorteio = TypeSorteio.QUINA;
    private int totalDezenas = 5;


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
        return "Quina: Id:" + getId() + " Número:" + getNumero() + " Data:" + MyDateUtil.calendarToDateBr(getData());
    }

}
