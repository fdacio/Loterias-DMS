package br.com.daciosoftware.loteriasdms.dao;

import android.database.sqlite.SQLiteException;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Dácio Braga on 14/06/2016.
 */
public interface InterfaceDAO<E, L> {

    L save(E entity) throws SQLiteException;
    int delete(E entity) throws SQLiteException;
    int deleteAll() throws SQLiteException;
    List<E> listAll();
    List<E> listAllDecrescente();
    List<E> dezenasCrescente(List<E> list);
    E findById(L id);
    E findByNumber(Integer number);
    E findFirst();
    E findLast();
    E findByDate(Calendar date);
    List<E> findByDezenas(int... dezenas);
    int count();
}
