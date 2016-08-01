package br.com.daciosoftware.loteriasdms.dao;

import java.util.Arrays;

import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class Lotofacil extends Sorteio {

    @Override
    public String toString(){
        return "Lotofácil: Id:"+getId()+" Número:" +getNumero()+ " Data:" + MyDateUtil.calendarToDateBr(getData())+ " Ultina dezena: "+getD15();
    }

    @Override
    public int[] getDezenas() {

        int[] arrayDezendas = new int[15];

        arrayDezendas[0] = getD1();
        arrayDezendas[1] = getD2();
        arrayDezendas[2] = getD3();
        arrayDezendas[3] = getD4();
        arrayDezendas[4] = getD5();
        arrayDezendas[5] = getD6();
        arrayDezendas[6] = getD7();
        arrayDezendas[7] = getD8();
        arrayDezendas[8] = getD9();
        arrayDezendas[9] = getD10();
        arrayDezendas[10] = getD11();
        arrayDezendas[11] = getD12();
        arrayDezendas[12] = getD13();
        arrayDezendas[13] = getD14();
        arrayDezendas[14] = getD15();

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

        setD7(arrayDezenas[6]);
        setD8(arrayDezenas[7]);
        setD9(arrayDezenas[8]);
        setD10(arrayDezenas[9]);
        setD11(arrayDezenas[10]);
        setD12(arrayDezenas[11]);

        setD13(arrayDezenas[12]);
        setD14(arrayDezenas[13]);
        setD15(arrayDezenas[14]);
    }
}
