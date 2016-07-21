package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.util.DateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Megasena extends Sorteio{


    @Override
    public String toString(){
        return "Megasena: Id:"+getId()+" Número:" +getNumero()+ " Data:" + DateUtil.calendarToDateBr(getData())+ " Ultina dezena: "+getD6();
    }
}
