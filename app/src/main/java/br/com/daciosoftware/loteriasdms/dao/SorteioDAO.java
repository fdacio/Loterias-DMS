package br.com.daciosoftware.loteriasdms.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.db.Database;
import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public abstract class SorteioDAO implements InterfaceDAO<Sorteio, Long> {

    private SQLiteDatabase db;
    private String tableName;
    private String[] allColumns;
    private String colunaID;
    private String colunaNumero;
    private String colunaData;

    protected SorteioDAO(Context context, InterfaceContractDatabase contract) {
        this.db = Database.getDatabase(context);
        this.tableName = contract.getTableName();
        this.colunaID = contract.getIdColumn();
        this.allColumns = contract.getAllColumns();
        this.colunaNumero = contract.getAllColumns()[1];
        this.colunaData = contract.getAllColumns()[2];
    }

    public SQLiteDatabase getDb() {
        return this.db;
    }


    /**
     * Padrão Factory
     * @param context
     * @param typeSorteio
     * @return
     */
    public static SorteioDAO getDAO(Context context, TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return new MegasenaDAO(context, new MegasenaContract());

            case LOTOFACIL:
                return new LotofacilDAO(context, new LotofacilContract());

            case QUINA:
                return new QuinaDAO(context, new QuinaContract());

            default:
                return null;
        }

    }


    public abstract Sorteio getInstanciaEntity();

    public abstract Long save(Sorteio sorteio) throws SQLiteException;

    public abstract Sorteio getEntity(Cursor c);

    public abstract Sorteio sortDezenasCrescente(Sorteio sorteio);

    public abstract Long insertSorteioFromTrow(List<String> tds) throws ParseException;

    @Override
    public int delete(Sorteio sorteio) throws SQLiteException {
        String where = this.colunaID + "=?";
        String[] whereArgs = new String[]{String.valueOf(sorteio.getId())};
        return db.delete(this.tableName, where, whereArgs);
    }

    @Override
    public int deleteAll() throws SQLiteException {
        String where = this.colunaID + ">?";
        String[] whereArgs = new String[]{String.valueOf(0)};
        return db.delete(this.tableName, where, whereArgs);

    }

    @Override
    public List<Sorteio> listAll() {
        List<Sorteio> listSorteio = new ArrayList<>();
        try {
            Cursor cursor = getCursor(null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Sorteio sorteio = getEntity(cursor);
                    listSorteio.add(sorteio);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            throw new RuntimeException();

        }
        Collections.sort(listSorteio);
        return listSorteio;
    }


    @Override
    public List<Sorteio> listAllDecrescente() {
        List<Sorteio> list = listAll();
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<Sorteio> sortListDezenasCrescente(List<Sorteio> list) {
        List<Sorteio> listDezenasCrescente = new ArrayList<>();
        for(Sorteio sorteio: list){
            Sorteio sorteioDezenasCrescente = sortDezenasCrescente(sorteio);
            listDezenasCrescente.add(sorteioDezenasCrescente);
        }
         return listDezenasCrescente;

    }

    @Override
    public Sorteio findById(Long id) {
        String where = this.colunaID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        Cursor cursor = getCursor(where, whereArgs);
        if (cursor.moveToFirst()) {
            return getEntity(cursor);

        } else {
            return null;
        }
    }

    @Override
    public Sorteio findByNumber(Integer number) {
        String where = this.colunaNumero + "=?";
        String[] whereArgs = new String[]{String.valueOf(number)};
        Cursor cursor = getCursor(where, whereArgs);
        if (cursor.moveToFirst()) {
            return getEntity(cursor);

        } else {
            return null;
        }
    }


    @Override
    public Sorteio findFirst() {
        if(count()>0)
            return listAll().get(0);
        else
            return null;
    }


    @Override
    public Sorteio findLast() {
        if(count()>0)
            return listAll().get(count()-1);
        else
            return null;
    }

    @Override
    public Sorteio findByDate(Calendar date) {
        String where = this.colunaData + "=?";
        String[] whereArgs = new String[]{MyDateUtil.calendarToDateUS(date)};
        Cursor cursor = getCursor(where, whereArgs);
        if (cursor.moveToFirst()) {
            return getEntity(cursor);

        } else {
            return null;
        }


    }

    @Override
    public List<Sorteio> findByDezenas(int... dezenas) {
        return null;
    }


    @Override
    public int count() {
        return listAll().size();
    }


    public Cursor getCursor(String where, String[] whereArgs) {
        return db.query(this.tableName,
                this.allColumns,
                where,
                whereArgs,
                null,
                null,
                null);
    }

    public Cursor getCursor(String where, String[] whereArgs, String orderBy) {
        return db.query(this.tableName,
                this.allColumns,
                where,
                whereArgs,
                null,
                null,
                orderBy);
    }


    public List<Sorteio> listBetweenDate(Calendar date1, Calendar date2) {
        List<Sorteio> list = new ArrayList<>();
        try {
            String where = this.colunaData + " between ? and ?";
            String[] whereArgs = new String[]{MyDateUtil.calendarToDateUS(date1), MyDateUtil.calendarToDateUS(date2)};
            String orderBy = this.colunaNumero + " desc";
            Cursor cursor = getCursor(where, whereArgs, orderBy);
            if (cursor.moveToFirst()) {
                do {
                    Sorteio sorteio = getEntity(cursor);
                    list.add(sorteio);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return list;
    }


    public List<Sorteio> listCountLast(int count, List<Sorteio> listSorteio) {
        List<Sorteio> list = new ArrayList<>();
        Collections.reverse(listSorteio);
        int i = 0;
        for(Sorteio sorteio: listSorteio){
            list.add(sorteio);
            i++;
            if(i == count) break;
        }

        return list;
    }


}
