package br.com.daciosoftware.loteriasdms.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.db.contract.ContractDatabase;
import br.com.daciosoftware.loteriasdms.db.contract.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.pojo.Lotofacil;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.pojo.SorteioFactory;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class LotofacilDAO extends SorteioDAO {

    public LotofacilDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);
    }

    public Long save(Sorteio sorteio) throws SQLiteException {
        Lotofacil lotofacil = (Lotofacil) sorteio;
        ContentValues values = new ContentValues();
        values.put(ContractDatabase.Lotofacil.COLUNA_NUMERO, lotofacil.getNumero());
        values.put(ContractDatabase.Lotofacil.COLUNA_DATA, MyDateUtil.calendarToDateUS(lotofacil.getData()));
        values.put(ContractDatabase.Lotofacil.COLUNA_LOCAL, lotofacil.getLocal());
        values.put(ContractDatabase.Lotofacil.COLUNA_D1, lotofacil.getDezenas()[0]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D2, lotofacil.getDezenas()[1]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D3, lotofacil.getDezenas()[2]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D4, lotofacil.getDezenas()[3]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D5, lotofacil.getDezenas()[4]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D6, lotofacil.getDezenas()[5]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D7, lotofacil.getDezenas()[6]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D8, lotofacil.getDezenas()[7]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D9, lotofacil.getDezenas()[8]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D10, lotofacil.getDezenas()[9]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D11, lotofacil.getDezenas()[10]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D12, lotofacil.getDezenas()[11]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D13, lotofacil.getDezenas()[12]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D14, lotofacil.getDezenas()[13]);
        values.put(ContractDatabase.Lotofacil.COLUNA_D15, lotofacil.getDezenas()[14]);

        if (lotofacil.getId() > 0) {
            String where = ContractDatabase.Lotofacil._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(lotofacil.getId())};
            return Long.valueOf(this.getDb().update(ContractDatabase.Lotofacil.NOME_TABELA, values, where, whereArgs));
        } else {
            return this.getDb().insertOrThrow(ContractDatabase.Lotofacil.NOME_TABELA, "", values);
        }
    }

    public Sorteio getEntity(Cursor c) {
        if (c.getCount() > 0) {
            Sorteio sorteio = SorteioFactory.getInstance(TypeSorteio.LOTOFACIL);
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
