package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.util.DateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Lotofacil extends Sorteio {


    @Override
    public String toString(){
        return "Lotofácil: Id:"+getId()+" Número:" +getNumero()+ " Data:" + DateUtil.calendarToDateBr(getData())+ " Ultina dezena: "+getD15();
    }
}
