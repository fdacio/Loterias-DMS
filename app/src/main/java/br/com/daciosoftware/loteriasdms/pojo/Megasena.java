package br.com.daciosoftware.loteriasdms.pojo;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Megasena extends Sorteio{

    private final String nome = "MEGA-SENA";
    private final TypeSorteio typeSorteio = TypeSorteio.MEGASENA;
    private final int totalDezenas = 6;

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
        return "Megasena: Id:" + getId() + " Número:" + getNumero() + " Data:" + MyDateUtil.calendarToDateBr(getData());
    }

}
