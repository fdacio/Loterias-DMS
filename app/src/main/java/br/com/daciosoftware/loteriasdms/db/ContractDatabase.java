package br.com.daciosoftware.loteriasdms.db;

import android.provider.BaseColumns;

public class ContractDatabase {

    public static final String NOME_BANCO = "loteriasdms.db";
    public static final int VERSAO = 1;

    public static abstract class Megasena implements BaseColumns {
        //nome da tabela
        public static final String NOME_TABELA = "megasena";

        // Colunas da tabela
        public static final String COLUNA_NUMERO = "numero";
        public static final String COLUNA_DATA = "data";
        public static final String COLUNA_LOCAL = "local";
        public static final String COLUNA_D1 = "d1";
        public static final String COLUNA_D2 = "d2";
        public static final String COLUNA_D3 = "d3";
        public static final String COLUNA_D4 = "d4";
        public static final String COLUNA_D5 = "d5";
        public static final String COLUNA_D6 = "d6";

        //Array das Colunas
        public static final String[] COLUNAS = {_ID, COLUNA_NUMERO, COLUNA_DATA, COLUNA_LOCAL, COLUNA_D1, COLUNA_D2, COLUNA_D3, COLUNA_D4, COLUNA_D5, COLUNA_D6};

        // Query de criação da tabela
        public static final String SQL_CRIAR_TABELA =
                "CREATE TABLE IF NOT EXISTS " + Megasena.NOME_TABELA + "(" +
                        Megasena._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Megasena.COLUNA_NUMERO + " INTEGER NOT NULL, " +
                        Megasena.COLUNA_DATA + " DATE NOT NULL, " +
                        Megasena.COLUNA_LOCAL + " TEXT NOT NULL, " +
                        Megasena.COLUNA_D1 + " INTEGER NOT NULL, " +
                        Megasena.COLUNA_D2 + " INTEGER NOT NULL, " +
                        Megasena.COLUNA_D3 + " INTEGER NOT NULL, " +
                        Megasena.COLUNA_D4 + " INTEGER NOT NULL, " +
                        Megasena.COLUNA_D5 + " INTEGER NOT NULL, " +
                        Megasena.COLUNA_D6 + " INTEGER NOT NULL, " +
                        "UNIQUE(" + Megasena.COLUNA_NUMERO + "));";

        public static final String SQL_DELETA_TABELA = "DROP TABLE IF EXISTS " + NOME_TABELA;
    }

    public static abstract class Lotofacil implements BaseColumns {
        //nome da tabela
        public static final String NOME_TABELA = "lotofacio";

        // Colunas da tabela
        public static final String COLUNA_NUMERO = "numero";
        public static final String COLUNA_DATA = "data";
        public static final String COLUNA_LOCAL = "local";
        public static final String COLUNA_D1 = "d1";
        public static final String COLUNA_D2 = "d2";
        public static final String COLUNA_D3 = "d3";
        public static final String COLUNA_D4 = "d4";
        public static final String COLUNA_D5 = "d5";
        public static final String COLUNA_D6 = "d6";
        public static final String COLUNA_D7 = "d7";
        public static final String COLUNA_D8 = "d8";
        public static final String COLUNA_D9 = "d9";
        public static final String COLUNA_D10 = "d10";
        public static final String COLUNA_D11 = "d11";
        public static final String COLUNA_D12 = "d12";
        public static final String COLUNA_D13 = "d13";
        public static final String COLUNA_D14 = "d14";
        public static final String COLUNA_D15 = "d15";

        //Array das Colunas
        public static final String[] COLUNAS = {_ID, COLUNA_NUMERO, COLUNA_DATA, COLUNA_LOCAL,
                COLUNA_D1, COLUNA_D2, COLUNA_D3, COLUNA_D4, COLUNA_D5,
                COLUNA_D6, COLUNA_D7, COLUNA_D8, COLUNA_D9, COLUNA_D10,
                COLUNA_D11, COLUNA_D12, COLUNA_D13, COLUNA_D14, COLUNA_D15};

        // Query de criação da tabela
        public static final String SQL_CRIAR_TABELA =
                "CREATE TABLE IF NOT EXISTS " + Lotofacil.NOME_TABELA + "(" +
                        Lotofacil._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Lotofacil.COLUNA_NUMERO + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_DATA + " DATE NOT NULL, " +
                        Lotofacil.COLUNA_LOCAL + " TEXT NOT NULL, " +
                        Lotofacil.COLUNA_D1 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D2 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D3 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D4 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D5 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D6 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D7 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D8 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D9 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D10 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D11 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D12 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D13 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D14 + " INTEGER NOT NULL, " +
                        Lotofacil.COLUNA_D15 + " INTEGER NOT NULL, " +
                        "UNIQUE(" + Lotofacil.COLUNA_NUMERO + "));";

        public static final String SQL_DELETA_TABELA = "DROP TABLE IF EXISTS " + NOME_TABELA;
    }

    public static abstract class Quina implements BaseColumns {
        //nome da tabela
        public static final String NOME_TABELA = "quina";

        // Colunas da tabela
        public static final String COLUNA_NUMERO = "numero";
        public static final String COLUNA_DATA = "data";
        public static final String COLUNA_LOCAL = "local";
        public static final String COLUNA_D1 = "d1";
        public static final String COLUNA_D2 = "d2";
        public static final String COLUNA_D3 = "d3";
        public static final String COLUNA_D4 = "d4";
        public static final String COLUNA_D5 = "d5";

        //Array das Colunas
        public static final String[] COLUNAS = {_ID, COLUNA_NUMERO, COLUNA_DATA, COLUNA_LOCAL, COLUNA_D1, COLUNA_D2, COLUNA_D3, COLUNA_D4, COLUNA_D5};

        // Query de criação da tabela
        public static final String SQL_CRIAR_TABELA =
                "CREATE TABLE IF NOT EXISTS " + Quina.NOME_TABELA + "(" +
                        Quina._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Quina.COLUNA_NUMERO + " INTEGER NOT NULL, " +
                        Quina.COLUNA_DATA + " DATE NOT NULL, " +
                        Quina.COLUNA_LOCAL + " TEXT NOT NULL, " +
                        Quina.COLUNA_D1 + " INTEGER NOT NULL, " +
                        Quina.COLUNA_D2 + " INTEGER NOT NULL, " +
                        Quina.COLUNA_D3 + " INTEGER NOT NULL, " +
                        Quina.COLUNA_D4 + " INTEGER NOT NULL, " +
                        Quina.COLUNA_D5 + " INTEGER NOT NULL, " +
                        "UNIQUE(" + Quina.COLUNA_NUMERO + "));";

        public static final String SQL_DELETA_TABELA = "DROP TABLE IF EXISTS " + NOME_TABELA;
    }


}
