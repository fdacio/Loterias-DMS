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
public class QuinaDAO extends SorteioDAO {

    public QuinaDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);
    }

    @Override
    public Quina getInstanciaEntity() {
        return new Quina();
    }

    @Override
    public Long save(Sorteio sorteio) throws SQLiteException {

        Quina quina = (Quina) sorteio;

        ContentValues values = new ContentValues();
        values.put(ContractDatabase.Quina.COLUNA_NUMERO, quina.getNumero());
        values.put(ContractDatabase.Quina.COLUNA_DATA, MyDateUtil.calendarToDateUS(quina.getData()));
        values.put(ContractDatabase.Quina.COLUNA_LOCAL, quina.getLocal());

        values.put(ContractDatabase.Quina.COLUNA_D1, quina.getD1());
        values.put(ContractDatabase.Quina.COLUNA_D2, quina.getD2());
        values.put(ContractDatabase.Quina.COLUNA_D3, quina.getD3());
        values.put(ContractDatabase.Quina.COLUNA_D4, quina.getD4());
        values.put(ContractDatabase.Quina.COLUNA_D5, quina.getD5());

        if (quina.getId() > 0) {
            String where = ContractDatabase.Quina._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(quina.getId())};
            return Long.valueOf(this.getDb().update(ContractDatabase.Quina.NOME_TABELA, values, where, whereArgs));
        } else {
            return this.getDb().insertOrThrow(ContractDatabase.Quina.NOME_TABELA, "", values);
        }
    }

    @Override
    public Quina getEntity(Cursor c) {
        if (c.getCount() > 0) {
            Quina quina = getInstanciaEntity();
            quina.setId(c.getInt(0));
            quina.setNumero(c.getInt(1));
            try {
                quina.setData(MyDateUtil.dateUSToCalendar(c.getString(2)));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            quina.setLocal(c.getString(3));
            quina.setD1(c.getInt(4));
            quina.setD2(c.getInt(5));
            quina.setD3(c.getInt(6));
            quina.setD4(c.getInt(7));
            quina.setD5(c.getInt(8));
            return quina;
        } else {
            return null;
        }

    }

    @Override
    public Long insertSorteioFromTrow(List<String> tds) throws NumberFormatException, ParseException{
        int numero = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(0)));
        if (findByNumber(numero) == null) {
            Calendar data = MyDateUtil.dateBrToCalendar(MyHtmlParse.getTextTag(tds.get(1)));
            int d1 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(2)));
            int d2 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(3)));
            int d3 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(4)));
            int d4 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(5)));
            int d5 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(6)));
            String local = MyHtmlParse.getTextTag(tds.get(9)) + " " + MyHtmlParse.getTextTag(tds.get(10));

            Quina quina = getInstanciaEntity();
            quina.setNumero(numero);
            quina.setData(data);
            quina.setLocal(local);
            quina.setD1(d1);
            quina.setD2(d2);
            quina.setD3(d3);
            quina.setD4(d4);
            quina.setD5(d5);
            return save(quina);
        } else {
            return null;
        }

    }

    @Override
    public Quina sortDezenasCrescente(Sorteio sorteio) {
        Quina quinaDezenasCrescente = (Quina) sorteio;

        int[] arrayDezendas = sorteio.getDezenas();

        Arrays.sort(arrayDezendas);

        quinaDezenasCrescente.setD1(arrayDezendas[0]);
        quinaDezenasCrescente.setD2(arrayDezendas[1]);
        quinaDezenasCrescente.setD3(arrayDezendas[2]);
        quinaDezenasCrescente.setD4(arrayDezendas[3]);
        quinaDezenasCrescente.setD5(arrayDezendas[4]);

        return quinaDezenasCrescente;
    }


}
