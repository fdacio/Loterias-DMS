package br.com.daciosoftware.loteriasdms.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.db.Database;
import br.com.daciosoftware.loteriasdms.db.contract.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.pojo.SorteioFactory;
import br.com.daciosoftware.loteriasdms.processaarquivo.MyHtmlParse;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public abstract class SorteioDAO {

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

    public abstract Long save(Sorteio sorteio) throws SQLiteException;

    public abstract Sorteio getEntity(Cursor c);

    public int delete(Sorteio sorteio) throws SQLiteException {
        String where = this.colunaID + "=?";
        String[] whereArgs = new String[]{String.valueOf(sorteio.getId())};
        return db.delete(this.tableName, where, whereArgs);
    }

    public int deleteAll() throws SQLiteException {
        String where = this.colunaID + ">?";
        String[] whereArgs = new String[]{String.valueOf(0)};
        return db.delete(this.tableName, where, whereArgs);

    }

    public List<Sorteio> listAll() {
        List<Sorteio> listSorteio = new ArrayList<>();
        try {
            Cursor cursor = getCursor(null, null);
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

    public List<Sorteio> listAllDecrescente() {
        List<Sorteio> list = listAll();
        Collections.reverse(list);
        return list;
    }

    public List<Sorteio> sortListDezenasCrescente(List<Sorteio> list) {
        List<Sorteio> listDezenasCrescente = new ArrayList<>();
        for(Sorteio sorteio: list){
            Sorteio sorteioDezenasCrescente = sortDezenasCrescente(sorteio);
            listDezenasCrescente.add(sorteioDezenasCrescente);
        }
         return listDezenasCrescente;

    }

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

    public Sorteio findFirst() {
        String[] columns = new String[]{"min(" + this.colunaNumero + ")"};
        Cursor cursor = getCursor(columns, null, null);
        if (cursor.moveToFirst()) {
            int number = cursor.getInt(0);
            return findByNumber(number);
        } else {
            return null;
        }
    }

    public Sorteio findLast() {
        String[] columns = new String[]{"max(" + this.colunaNumero + ")"};
        Cursor cursor = getCursor(columns, null, null);
        if (cursor.moveToFirst()) {
            int number = cursor.getInt(0);
            return findByNumber(number);
        } else {
            return null;
        }
    }

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

    private Cursor getCursor(String where, String[] whereArgs) {
        return db.query(this.tableName,
                this.allColumns,
                where,
                whereArgs,
                null,
                null,
                null);
    }

    private Cursor getCursor(String where, String[] whereArgs, String orderBy) {
        return db.query(this.tableName,
                this.allColumns,
                where,
                whereArgs,
                null,
                null,
                orderBy);
    }

    private Cursor getCursor(String[] columns, String where, String[] whereArgs) {
        return db.query(this.tableName,
                columns,
                where,
                whereArgs,
                null,
                null,
                null);
    }

    public List<Sorteio> listEntreDatas(Calendar date1, Calendar date2) {
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

    public List<Sorteio> listQtdeConcursos(int qtde, List<Sorteio> listSorteio) {
        List<Sorteio> list = new ArrayList<>();
        Collections.reverse(listSorteio);
        int i = 0;
        for(Sorteio sorteio: listSorteio){
            list.add(sorteio);
            i++;
            if (i == qtde) break;
        }

        return list;
    }

    public Long insertSorteioFromTrow(List<String> tds, TypeSorteio typeSorteio) throws NumberFormatException, ParseException {
        Sorteio sorteio = SorteioFactory.getInstance(typeSorteio);
        int numero = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(0)));
        if (findByNumber(numero) == null) {
            Calendar data = MyDateUtil.dateBrToCalendar(MyHtmlParse.getTextTag(tds.get(1)));
            int[] dezenas = new int[sorteio.getTotalDezenas()];
            for (int i = 0; i < dezenas.length; i++) {
                dezenas[i] = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(i + 2)));
            }
            String local = MyHtmlParse.getTextTag(tds.get(19)) + " " + MyHtmlParse.getTextTag(tds.get(20));

            sorteio.setNumero(numero);
            sorteio.setData(data);
            sorteio.setLocal(local);
            sorteio.setDezenas(dezenas);
            return save(sorteio);
        } else {
            return null;
        }
    }

    public Sorteio sortDezenasCrescente(Sorteio sorteio) {
        int[] arrayDezenas = sorteio.getDezenas();
        Arrays.sort(arrayDezenas);
        sorteio.setDezenas(arrayDezenas);
        return sorteio;
    }


}
