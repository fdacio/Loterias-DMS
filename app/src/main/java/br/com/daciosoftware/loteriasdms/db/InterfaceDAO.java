package br.com.daciosoftware.loteriasdms.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Dácio Braga on 14/06/2016.
 */
public interface InterfaceDAO<E, L> {

    public L save(E entity) throws SQLiteException;
    public int delete(E entity) throws SQLiteException;
    public List<E> listAll();
    public E findById(L id);
    public E findByNumber(Integer number);
    public E findByDate(Calendar date);
    public List<E> findByDezenas(int... dezenas);
    public E getEntity(Cursor c);
    public int count();
}
