package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Quina extends Sorteio{

    @Override
    public String toString(){
        return "Quina: Id:"+getId()+" Número:" +getNumero()+ " Data:" + MyDateUtil.calendarToDateBr(getData())+ " Ultina dezena: "+getD5();
    }

}
