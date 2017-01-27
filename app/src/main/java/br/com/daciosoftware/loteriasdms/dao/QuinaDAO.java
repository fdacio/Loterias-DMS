package br.com.daciosoftware.loteriasdms.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.db.contract.ContractDatabase;
import br.com.daciosoftware.loteriasdms.db.contract.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.pojo.Quina;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.pojo.SorteioFactory;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class QuinaDAO extends SorteioDAO {

    public QuinaDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);
    }

    public Long save(Sorteio sorteio) throws SQLiteException {
        Quina quina = (Quina) sorteio;
        ContentValues values = new ContentValues();
        values.put(ContractDatabase.Quina.COLUNA_NUMERO, quina.getNumero());
        values.put(ContractDatabase.Quina.COLUNA_DATA, MyDateUtil.calendarToDateUS(quina.getData()));
        values.put(ContractDatabase.Quina.COLUNA_LOCAL, quina.getLocal());
        values.put(ContractDatabase.Quina.COLUNA_D1, quina.getDezenas()[0]);
        values.put(ContractDatabase.Quina.COLUNA_D2, quina.getDezenas()[1]);
        values.put(ContractDatabase.Quina.COLUNA_D3, quina.getDezenas()[2]);
        values.put(ContractDatabase.Quina.COLUNA_D4, quina.getDezenas()[3]);
        values.put(ContractDatabase.Quina.COLUNA_D5, quina.getDezenas()[4]);

        if (quina.getId() > 0) {
            String where = ContractDatabase.Quina._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(quina.getId())};
            return Long.valueOf(this.getDb().update(ContractDatabase.Quina.NOME_TABELA, values, where, whereArgs));
        } else {
            return this.getDb().insertOrThrow(ContractDatabase.Quina.NOME_TABELA, "", values);
        }
    }

    public Sorteio getEntity(Cursor c) {
        if (c.getCount() > 0) {
            Sorteio sorteio = SorteioFactory.getInstance(TypeSorteio.QUINA);
            sorteio.setId(c.getInt(0));
            sorteio.setNumero(c.getInt(1));
            try {
                sorteio.setData(MyDateUtil.dateUSToCalendar(c.getString(2)));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            sorteio.setLocal(c.getString(3));
            int[] dezenas = new int[sorteio.getTotalDezenas()];
            for (int i = 0; i < dezenas.length; i++) {
                dezenas[i] = c.getInt(i + 4);
            }
            sorteio.setDezenas(dezenas);
            return sorteio;
        } else {
            return null;
        }

    }

}
