package br.com.daciosoftware.loteriasdms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.daciosoftware.loteriasdms.db.contract.ContractDatabase;

public class SQLiteHelperDatabase extends SQLiteOpenHelper {

    public SQLiteHelperDatabase(Context context) {
        super(context, ContractDatabase.NOME_BANCO, null, ContractDatabase.VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContractDatabase.Megasena.SQL_CRIAR_TABELA);
        db.execSQL(ContractDatabase.Lotofacil.SQL_CRIAR_TABELA);
        db.execSQL(ContractDatabase.Quina.SQL_CRIAR_TABELA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        if(novaVersao > versaoAntiga){
            db.execSQL(ContractDatabase.Megasena.SQL_DELETA_TABELA);
            db.execSQL(ContractDatabase.Lotofacil.SQL_DELETA_TABELA);
            db.execSQL(ContractDatabase.Quina.SQL_DELETA_TABELA);
            onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if(!db.isReadOnly()){
            //Ativar foreign key constraint
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }


}
