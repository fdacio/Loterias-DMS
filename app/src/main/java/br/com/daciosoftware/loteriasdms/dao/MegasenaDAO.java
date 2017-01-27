package br.com.daciosoftware.loteriasdms.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.db.contract.ContractDatabase;
import br.com.daciosoftware.loteriasdms.db.contract.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.pojo.Megasena;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.pojo.SorteioFactory;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class MegasenaDAO extends SorteioDAO {

    public MegasenaDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);
    }

    public Long save(Sorteio sorteio) throws SQLiteException {
        Megasena megasena = (Megasena) sorteio;
        ContentValues values = new ContentValues();
        values.put(ContractDatabase.Megasena.COLUNA_NUMERO, megasena.getNumero());
        values.put(ContractDatabase.Megasena.COLUNA_DATA, MyDateUtil.calendarToDateUS(megasena.getData()));
        values.put(ContractDatabase.Megasena.COLUNA_LOCAL, megasena.getLocal());
        values.put(ContractDatabase.Megasena.COLUNA_D1, megasena.getDezenas()[0]);
        values.put(ContractDatabase.Megasena.COLUNA_D2, megasena.getDezenas()[1]);
        values.put(ContractDatabase.Megasena.COLUNA_D3, megasena.getDezenas()[2]);
        values.put(ContractDatabase.Megasena.COLUNA_D4, megasena.getDezenas()[3]);
        values.put(ContractDatabase.Megasena.COLUNA_D5, megasena.getDezenas()[4]);
        values.put(ContractDatabase.Megasena.COLUNA_D6, megasena.getDezenas()[5]);
        if (megasena.getId() > 0) {
            String where = ContractDatabase.Megasena._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(megasena.getId())};
            return Long.valueOf(this.getDb().update(ContractDatabase.Megasena.NOME_TABELA, values, where, whereArgs));
        } else {
            return this.getDb().insertOrThrow(ContractDatabase.Megasena.NOME_TABELA, "", values);
        }
    }

    public Sorteio getEntity(Cursor c) {
        if (c.getCount() > 0) {
            Sorteio sorteio = SorteioFactory.getInstance(TypeSorteio.MEGASENA);
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
