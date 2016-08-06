package br.com.daciosoftware.loteriasdms.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import org.jsoup.select.Elements;

import java.io.IOException;
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
public class LotofacilDAO extends SorteioDAO {

    public LotofacilDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);
    }

    @Override
    public Lotofacil getInstanciaEntity() {
        return new Lotofacil();
    }

    @Override
    public Long save(Sorteio sorteio) throws SQLiteException {
        Lotofacil lotofacil = (Lotofacil) sorteio;

        ContentValues values = new ContentValues();
        values.put(ContractDatabase.Lotofacil.COLUNA_NUMERO, lotofacil.getNumero());
        values.put(ContractDatabase.Lotofacil.COLUNA_DATA, MyDateUtil.calendarToDateUS(lotofacil.getData()));
        values.put(ContractDatabase.Lotofacil.COLUNA_LOCAL, lotofacil.getLocal());

        values.put(ContractDatabase.Lotofacil.COLUNA_D1, lotofacil.getD1());
        values.put(ContractDatabase.Lotofacil.COLUNA_D2, lotofacil.getD2());
        values.put(ContractDatabase.Lotofacil.COLUNA_D3, lotofacil.getD3());
        values.put(ContractDatabase.Lotofacil.COLUNA_D4, lotofacil.getD4());
        values.put(ContractDatabase.Lotofacil.COLUNA_D5, lotofacil.getD5());
        values.put(ContractDatabase.Lotofacil.COLUNA_D6, lotofacil.getD6());

        values.put(ContractDatabase.Lotofacil.COLUNA_D7, lotofacil.getD7());
        values.put(ContractDatabase.Lotofacil.COLUNA_D8, lotofacil.getD8());
        values.put(ContractDatabase.Lotofacil.COLUNA_D9, lotofacil.getD9());
        values.put(ContractDatabase.Lotofacil.COLUNA_D10, lotofacil.getD10());
        values.put(ContractDatabase.Lotofacil.COLUNA_D11, lotofacil.getD11());
        values.put(ContractDatabase.Lotofacil.COLUNA_D12, lotofacil.getD12());
        values.put(ContractDatabase.Lotofacil.COLUNA_D13, lotofacil.getD13());
        values.put(ContractDatabase.Lotofacil.COLUNA_D14, lotofacil.getD14());
        values.put(ContractDatabase.Lotofacil.COLUNA_D15, lotofacil.getD15());

        if (lotofacil.getId() > 0) {
            String where = ContractDatabase.Lotofacil._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(lotofacil.getId())};
            return Long.valueOf(this.getDb().update(ContractDatabase.Lotofacil.NOME_TABELA, values, where, whereArgs));
        } else {
            return this.getDb().insertOrThrow(ContractDatabase.Lotofacil.NOME_TABELA, "", values);
        }
    }

    @Override
    public Long insertSorteioFromTrow(Elements tds) throws NumberFormatException, ParseException, IOException {

        int numero = Integer.parseInt(tds.get(0).text());
        if (findByNumber(numero) == null) {
            Calendar data = MyDateUtil.dateBrToCalendar(tds.get(1).text());
            int d1 = Integer.parseInt(tds.get(2).text());
            int d2 = Integer.parseInt(tds.get(3).text());
            int d3 = Integer.parseInt(tds.get(4).text());
            int d4 = Integer.parseInt(tds.get(5).text());
            int d5 = Integer.parseInt(tds.get(6).text());
            int d6 = Integer.parseInt(tds.get(7).text());
            int d7 = Integer.parseInt(tds.get(8).text());
            int d8 = Integer.parseInt(tds.get(9).text());
            int d9 = Integer.parseInt(tds.get(10).text());
            int d10 = Integer.parseInt(tds.get(11).text());
            int d11 = Integer.parseInt(tds.get(12).text());
            int d12 = Integer.parseInt(tds.get(13).text());
            int d13 = Integer.parseInt(tds.get(14).text());
            int d14 = Integer.parseInt(tds.get(15).text());
            int d15 = Integer.parseInt(tds.get(16).text());
            String local = tds.get(19).text() + " " + tds.get(20).text();

            Lotofacil lotofacil = getInstanciaEntity();
            lotofacil.setNumero(numero);
            lotofacil.setData(data);
            lotofacil.setLocal(local);
            lotofacil.setD1(d1);
            lotofacil.setD2(d2);
            lotofacil.setD3(d3);
            lotofacil.setD4(d4);
            lotofacil.setD5(d5);
            lotofacil.setD6(d6);
            lotofacil.setD7(d7);
            lotofacil.setD8(d8);
            lotofacil.setD9(d9);
            lotofacil.setD10(d10);
            lotofacil.setD11(d11);
            lotofacil.setD12(d12);
            lotofacil.setD13(d13);
            lotofacil.setD14(d14);
            lotofacil.setD15(d15);
            return save(lotofacil);
        } else {
            return null;
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
            int d7 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(8)));
            int d8 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(9)));
            int d9 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(10)));
            int d10 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(11)));
            int d11 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(12)));
            int d12 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(13)));
            int d13 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(14)));
            int d14 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(15)));
            int d15 = Integer.parseInt(MyHtmlParse.getTextTag(tds.get(16)));
            String local = MyHtmlParse.getTextTag(tds.get(19)) + " " + MyHtmlParse.getTextTag(tds.get(20));

            Lotofacil lotofacil = getInstanciaEntity();
            lotofacil.setNumero(numero);
            lotofacil.setData(data);
            lotofacil.setLocal(local);
            lotofacil.setD1(d1);
            lotofacil.setD2(d2);
            lotofacil.setD3(d3);
            lotofacil.setD4(d4);
            lotofacil.setD5(d5);
            lotofacil.setD6(d6);
            lotofacil.setD7(d7);
            lotofacil.setD8(d8);
            lotofacil.setD9(d9);
            lotofacil.setD10(d10);
            lotofacil.setD11(d11);
            lotofacil.setD12(d12);
            lotofacil.setD13(d13);
            lotofacil.setD14(d14);
            lotofacil.setD15(d15);
            return save(lotofacil);
        } else {
            return null;
        }
    }


    @Override
    public Lotofacil getEntity(Cursor c) {
        if (c.getCount() > 0) {

            Lotofacil lotofacil = getInstanciaEntity();
            lotofacil.setId(c.getInt(0));
            lotofacil.setNumero(c.getInt(1));
            try {
                lotofacil.setData(MyDateUtil.dateUSToCalendar(c.getString(2)));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            lotofacil.setLocal(c.getString(3));
            lotofacil.setD1(c.getInt(4));
            lotofacil.setD2(c.getInt(5));
            lotofacil.setD3(c.getInt(6));
            lotofacil.setD4(c.getInt(7));
            lotofacil.setD5(c.getInt(8));
            lotofacil.setD6(c.getInt(9));
            lotofacil.setD7(c.getInt(10));
            lotofacil.setD8(c.getInt(11));
            lotofacil.setD9(c.getInt(12));
            lotofacil.setD10(c.getInt(13));
            lotofacil.setD11(c.getInt(14));
            lotofacil.setD12(c.getInt(15));
            lotofacil.setD13(c.getInt(16));
            lotofacil.setD14(c.getInt(17));
            lotofacil.setD15(c.getInt(18));
            return lotofacil;
        } else {
            return null;
        }

    }

    @Override
    public Lotofacil sortDezenasCrescente(Sorteio sorteio) {

        Lotofacil lotofacilDezenasCrescente = (Lotofacil) sorteio;

        int[] arrayDezenas = sorteio.getDezenas();

        Arrays.sort(arrayDezenas);

        lotofacilDezenasCrescente.setD1(arrayDezenas[0]);
        lotofacilDezenasCrescente.setD2(arrayDezenas[1]);
        lotofacilDezenasCrescente.setD3(arrayDezenas[2]);
        lotofacilDezenasCrescente.setD4(arrayDezenas[3]);
        lotofacilDezenasCrescente.setD5(arrayDezenas[4]);
        lotofacilDezenasCrescente.setD6(arrayDezenas[5]);
        lotofacilDezenasCrescente.setD7(arrayDezenas[6]);
        lotofacilDezenasCrescente.setD8(arrayDezenas[7]);
        lotofacilDezenasCrescente.setD9(arrayDezenas[8]);
        lotofacilDezenasCrescente.setD10(arrayDezenas[9]);
        lotofacilDezenasCrescente.setD11(arrayDezenas[10]);
        lotofacilDezenasCrescente.setD12(arrayDezenas[11]);
        lotofacilDezenasCrescente.setD13(arrayDezenas[12]);
        lotofacilDezenasCrescente.setD14(arrayDezenas[13]);
        lotofacilDezenasCrescente.setD15(arrayDezenas[14]);

        return lotofacilDezenasCrescente;
    }

}
