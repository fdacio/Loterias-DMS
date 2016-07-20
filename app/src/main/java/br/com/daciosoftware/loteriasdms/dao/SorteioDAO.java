package br.com.daciosoftware.loteriasdms.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.daciosoftware.loteriasdms.db.Database;
import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.db.InterfaceDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public abstract class SorteioDAO implements InterfaceDAO<Sorteio, Long> {

    private SQLiteDatabase db;

    private InterfaceContractDatabase contract;

    private String tableName;
    private String idColumn;
    private String[] allColumns;
    private String[] saveColumns;


    public SorteioDAO(Context context, InterfaceContractDatabase contract) {
         this.contract = contract;
        this.db = Database.getDatabase(context);
        setVariabelsContract();
    }


    public abstract Sorteio getInstancia();

    public void setVariabelsContract() {
        this.tableName = contract.getTableName();
        this.idColumn = contract.getIdColumn();
        this.allColumns = contract.getAllColumns();
        this.saveColumns = contract.getSaveColumns();
    }

    public Cursor getCursor(String where, String[] whereArgs) {
        return  db.query(this.tableName,
                this.allColumns,
                where,
                whereArgs,
                null,
                null,
                null);
    }

    @Override
    public Long save(Sorteio sorteio) throws SQLiteException {
        String[] saveColumns = this.saveColumns;
        ContentValues values = new ContentValues();
        values.put(saveColumns[1], sorteio.getNumero());
        values.put(saveColumns[2], DateUtil.calendarToDateUS(sorteio.getData()));
        values.put(saveColumns[3], sorteio.getLocal());

        values.put(saveColumns[4], sorteio.getD1());
        values.put(saveColumns[5], sorteio.getD2());
        values.put(saveColumns[6], sorteio.getD3());
        values.put(saveColumns[7], sorteio.getD4());
        values.put(saveColumns[8], sorteio.getD5());

        if (saveColumns.length > 9) {
            values.put(saveColumns[9], sorteio.getD6());
        }

        if(saveColumns.length > 10){
            values.put(saveColumns[10], sorteio.getD7());
            values.put(saveColumns[11], sorteio.getD8());
            values.put(saveColumns[12], sorteio.getD9());
            values.put(saveColumns[13], sorteio.getD10());
            values.put(saveColumns[14], sorteio.getD11());
            values.put(saveColumns[15], sorteio.getD12());
            values.put(saveColumns[16], sorteio.getD13());
            values.put(saveColumns[17], sorteio.getD14());
            values.put(saveColumns[18], sorteio.getD15());
        }


        return db.insertOrThrow(this.tableName, "", values);
    }

    @Override
    public int delete(Sorteio sorteio) throws SQLiteException {
        String where = this.idColumn + "=?";
        String[] whereArgs = new String[]{String.valueOf(sorteio.getId())};
        return db.delete(this.tableName, where, whereArgs);

    }

    @Override
    public List<Sorteio> listAll() {
        List<Sorteio> list = new ArrayList<>();
        try {
            Cursor cursor = getCursor(null, null);
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
        return null;
    }

    @Override
    public Sorteio findByDate(Calendar date) {
        return null;
    }

    @Override
    public List<Sorteio> findByDezenas(int... dezenas) {
        return null;
    }

    @Override
    public Sorteio getEntity(Cursor c) {
        if (c.getCount() > 0) {
            Sorteio sorteio = getInstancia();
            sorteio.setId(c.getInt(0));
            sorteio.setNumero(c.getInt(1));
            sorteio.setData(DateUtil.dateUSToCalendar(c.getString(2)));
            sorteio.setLocal(c.getString(3));
            sorteio.setD1(c.getInt(4));
            sorteio.setD2(c.getInt(5));
            sorteio.setD3(c.getInt(6));
            sorteio.setD4(c.getInt(7));
            sorteio.setD5(c.getInt(8));

            if (c.getColumnCount()> 9) {
                sorteio.setD6(c.getInt(9));
            }

            if (c.getColumnCount()> 10) {
                sorteio.setD7(c.getInt(10));
                sorteio.setD8(c.getInt(11));
                sorteio.setD9(c.getInt(12));
                sorteio.setD10(c.getInt(13));
                sorteio.setD11(c.getInt(14));
                sorteio.setD12(c.getInt(15));
                sorteio.setD13(c.getInt(16));
                sorteio.setD14(c.getInt(17));
                sorteio.setD15(c.getInt(18));
            }

            return sorteio;
        } else {
            return null;
        }

    }

    @Override
    public int count() {
        return listAll().size();
    }
}
