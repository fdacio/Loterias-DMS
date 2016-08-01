package br.com.daciosoftware.loteriasdms.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public abstract class Sorteio implements Comparable<Sorteio>, Serializable {

    private long id;
    private int numero;
    private Calendar data;
    private String local;
    private int d1, d2, d3, d4, d5,
            d6, d7, d8, d9, d10,
            d11, d12, d13, d14, d15;

    protected Sorteio() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getD1() {
        return d1;
    }

    public void setD1(int d1) {
        this.d1 = d1;
    }

    public int getD2() {
        return d2;
    }

    public void setD2(int d2) {
        this.d2 = d2;
    }

    public int getD3() {
        return d3;
    }

    public void setD3(int d3) {
        this.d3 = d3;
    }

    public int getD4() {
        return d4;
    }

    public void setD4(int d4) {
        this.d4 = d4;
    }

    public int getD5() {
        return d5;
    }

    public void setD5(int d5) {
        this.d5 = d5;
    }

    public int getD6() {
        return d6;
    }

    public void setD6(int d6) {
        this.d6 = d6;
    }

    public int getD7() {
        return d7;
    }

    public void setD7(int d7) {
        this.d7 = d7;
    }

    public int getD8() {
        return d8;
    }

    public void setD8(int d8) {
        this.d8 = d8;
    }

    public int getD9() {
        return d9;
    }

    public void setD9(int d9) {
        this.d9 = d9;
    }

    public int getD10() {
        return d10;
    }

    public void setD10(int d10) {
        this.d10 = d10;
    }

    public int getD11() {
        return d11;
    }

    public void setD11(int d11) {
        this.d11 = d11;
    }

    public int getD12() {
        return d12;
    }

    public void setD12(int d12) {
        this.d12 = d12;
    }

    public int getD13() {
        return d13;
    }

    public void setD13(int d13) {
        this.d13 = d13;
    }

    public int getD14() {
        return d14;
    }

    public void setD14(int d14) {
        this.d14 = d14;
    }

    public int getD15() {
        return d15;
    }

    public void setD15(int d15) {
        this.d15 = d15;
    }

    @Override
    public int compareTo(Sorteio another) {
        return new Integer(numero).compareTo(another.getNumero());
    }

    public abstract int[] getDezenas();
    public abstract void setDezenas(int[] arrayDezenas);
}
