package br.com.daciosoftware.loteriasdms.pojo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;

import br.com.daciosoftware.loteriasdms.TypeSorteio;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public abstract class Sorteio implements Comparable<Sorteio>, Serializable {

    private long id;
    private int numero;
    private Calendar data;
    private String local;
    private int[] dezenas;

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

    public int[] getDezenas() {
        return this.dezenas;
    }

    public void setDezenas(int[] dezenas) {
        this.dezenas = dezenas;
    }

    @Override
    public int compareTo(@NonNull Sorteio another) {
        if(this.getNumero() > another.getNumero()) return 1;
        else if(this.getNumero() < another.getNumero()) return -1;
        else return 0;
    }

    public abstract int getTotalDezenas();

    public abstract String getNome();

    public abstract TypeSorteio getTypeSorteio();

}
