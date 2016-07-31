package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Lotofacil extends Sorteio {


    @Override
    public String toString(){
        return "Lotofácil: Id:"+getId()+" Número:" +getNumero()+ " Data:" + MyDateUtil.calendarToDateBr(getData())+ " Ultina dezena: "+getD15();
    }
}
