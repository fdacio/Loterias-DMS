package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Megasena extends Sorteio{


    @Override
    public String toString(){
        return "Megasena: Id:"+getId()+" Número:" +getNumero()+ " Data:" + MyDateUtil.calendarToDateBr(getData())+ " Ultina dezena: "+getD6();
    }

    @Override
    public int[] getDezenas() {
        int[] arrayDezendas = new int[6];

        arrayDezendas[0] = getD1();
        arrayDezendas[1] = getD2();
        arrayDezendas[2] = getD3();
        arrayDezendas[3] = getD4();
        arrayDezendas[4] = getD5();
        arrayDezendas[5] = getD6();

        return arrayDezendas;

    }

    @Override
    public void setDezenas(int[] arrayDezenas) {
        setD1(arrayDezenas[0]);
        setD2(arrayDezenas[1]);
        setD3(arrayDezenas[2]);
        setD4(arrayDezenas[3]);
        setD5(arrayDezenas[4]);
        setD6(arrayDezenas[5]);
    }
}
