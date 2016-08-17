package br.com.daciosoftware.loteriasdms.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.daciosoftware.loteriasdms.db.ContractDatabase;
import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;
import br.com.daciosoftware.loteriasdms.processaarquivo.MyHtmlParse;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class MegasenaDAO extends SorteioDAO {

    public MegasenaDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);

    }

    @Override
    public Megasena getInstanciaEntity() {
        return new Megasena();
    }

    @Override
    public Long save(Sorteio sorteio) throws SQLiteException {

        Megasena megasena = (Megasena) sorteio;

        ContentValues values = new ContentValues();
        values.put(ContractDatabase.Megasena.COLUNA_NUMERO, megasena.getNumero());
        values.put(ContractDatabase.Megasena.COLUNA_DATA, MyDateUtil.calendarToDateUS(megasena.getData()));
        values.put(ContractDatabase.Megasena.COLUNA_LOCAL, megasena.getLocal());

        values.put(ContractDatabase.Megasena.COLUNA_D1, megasena.getD1());
        values.put(ContractDatabase.Megasena.COLUNA_D2, megasena.getD2());
        values.put(ContractDatabase.Megasena.COLUNA_D3, megasena.getD3());
        values.put(ContractDatabase.Megasena.COLUNA_D4, megasena.getD4());
        values.put(ContractDatabase.Megasena.COLUNA_D5, megasena.getD5());
        values.put(ContractDatabase.Megasena.COLUNA_D6, megasena.getD6());

        if (megasena.getId() > 0) {
            String where = ContractDatabase.Megasena._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(megasena.getId())};
            return Long.valueOf(this.getDb().update(ContractDatabase.Megasena.NOME_TABELA, values, where, whereArgs));
        } else {
            return this.getDb().insertOrThrow(ContractDatabase.Megasena.NOME_TABELA, "", values);
        }
    }

    @Override
    public Long insertSorteioFromTrow(List<String> tds) throws NumberFormatException, ParseException {
        int numero = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(0)));
        if (findByNumber(numero) == null) {
            Calendar data = MyDateUtil.dateBrToCalendar(MyHtmlParse.getTextTag(tds.get(1)));
            int d1 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(2)));
            int d2 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(3)));
            int d3 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(4)));
            int d4 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(5)));
            int d5 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(6)));
            int d6 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(7)));
            String local = MyHtmlParse.getTextTag(tds.get(10)) + " " + MyHtmlParse.getTextTag(tds.get(11));

            Megasena megasena = getInstanciaEntity();
            megasena.setNumero(numero);
            megasena.setData(data);
            megasena.setLocal(local);
            megasena.setD1(d1);
            megasena.setD2(d2);
            megasena.setD3(d3);
            megasena.setD4(d4);
            megasena.setD5(d5);
            megasena.setD6(d6);
            return save(megasena);
        } else {
            return null;
        }

    }

    @Override
    public Megasena getEntity(Cursor c) {
        if (c.getCount() > 0) {
            Megasena megasena = getInstanciaEntity();
            megasena.setId(c.getInt(0));
            megasena.setNumero(c.getInt(1));
            try {
                megasena.setData(MyDateUtil.dateUSToCalendar(c.getString(2)));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            megasena.setLocal(c.getString(3));
            megasena.setD1(c.getInt(4));
            megasena.setD2(c.getInt(5));
            megasena.setD3(c.getInt(6));
            megasena.setD4(c.getInt(7));
            megasena.setD5(c.getInt(8));
            megasena.setD6(c.getInt(9));
            return megasena;
        } else {
            return null;
        }

    }

    @Override
    public Megasena sortDezenasCrescente(Sorteio sorteio) {

        Megasena megasenaDezenasCrescente = (Megasena) sorteio;

        int[] arrayDezendas =sorteio.getDezenas();

        Arrays.sort(arrayDezendas);

        megasenaDezenasCrescente.setD1(arrayDezendas[0]);
        megasenaDezenasCrescente.setD2(arrayDezendas[1]);
        megasenaDezenasCrescente.setD3(arrayDezendas[2]);
        megasenaDezenasCrescente.setD4(arrayDezendas[3]);
        megasenaDezenasCrescente.setD5(arrayDezendas[4]);
        megasenaDezenasCrescente.setD6(arrayDezendas[5]);

        return megasenaDezenasCrescente;
    }


}
