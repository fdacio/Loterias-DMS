package br.com.daciosoftware.loteriasdms.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.db.Database;
import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.util.DateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public abstract class SorteioDAO implements InterfaceDAO<Sorteio, Long> {

    private SQLiteDatabase db;
    private String tableName;
    private String idColumn;
    private String[] allColumns;

    protected SorteioDAO(Context context, InterfaceContractDatabase contract) {
        this.db = Database.getDatabase(context);
        this.tableName = contract.getTableName();
        this.idColumn = contract.getIdColumn();
        this.allColumns = contract.getAllColumns();
    }

    public SQLiteDatabase getDb() {
        return this.db;
    }


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

    public abstract Sorteio getEntityDezenasCrescente(Cursor c);

    public abstract Long insertSorteioFromTrow(Elements tds) throws NumberFormatException, ParseException, IOException;

    @Override
    public int delete(Sorteio sorteio) throws SQLiteException {
        String where = this.idColumn + "=?";
        String[] whereArgs = new String[]{String.valueOf(sorteio.getId())};
        return db.delete(this.tableName, where, whereArgs);
    }

    @Override
    public int deleteAll() throws SQLiteException {
        String where = this.idColumn + ">?";
        String[] whereArgs = new String[]{String.valueOf(0)};
        return db.delete(this.tableName, where, whereArgs);
    }

    @Override
    public List<Sorteio> listAll() {
        List<Sorteio> list = new ArrayList<>();
        try {
            String orderBy = allColumns[1] + " desc";
            Cursor cursor = getCursor(null, null, orderBy);
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

    @Override
    public List<Sorteio> listAllDezenasCrescente() {
        List<Sorteio> list = new ArrayList<>();
        try {
            String orderBy = allColumns[1] + " desc";
            Cursor cursor = getCursor(null, null, orderBy);
            if (cursor.moveToFirst()) {
                do {
                    Sorteio sorteio = getEntityDezenasCrescente(cursor);
                    list.add(sorteio);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            throw new RuntimeException();

        }
        return list;

    }

    @Override
    public Sorteio findById(Long id) {
        String where = this.idColumn + "=?";
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
        String where = this.allColumns[1] + "=?";
        String[] whereArgs = new String[]{String.valueOf(number)};
        Cursor cursor = getCursor(where, whereArgs);
        if (cursor.moveToFirst()) {
            return getEntity(cursor);

        } else {
            return null;
        }
    }

    @Override
    public Sorteio findByDate(Calendar date) {
        String where = this.allColumns[2] + "=?";
        String[] whereArgs = new String[]{DateUtil.calendarToDateUS(date)};
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


    public List<Sorteio> findByBetweenDate(Calendar date1, Calendar date2) {
        List<Sorteio> list = new ArrayList<>();
        try {
            String where = this.allColumns[2] + " between ? and ?";
            String[] whereArgs = new String[]{DateUtil.calendarToDateUS(date1), DateUtil.calendarToDateUS(date2)};
            String orderBy = allColumns[1] + " desc";
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


}
