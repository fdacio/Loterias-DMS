package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.db.ContractDatabase;
import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class MegasenaContract implements InterfaceContractDatabase {

    @Override
    public String getTableName() {
        return ContractDatabase.Megasena.NOME_TABELA;
    }

    @Override
    public String[] getAllColumns() {
        return ContractDatabase.Megasena.COLUNAS;
    }

    @Override
    public String[] getSaveColumns() {
        String[] saveColumns = {ContractDatabase.Megasena._ID,
                ContractDatabase.Megasena.COLUNA_NUMERO,
                ContractDatabase.Megasena.COLUNA_DATA,
                ContractDatabase.Megasena.COLUNA_LOCAL,
                ContractDatabase.Megasena.COLUNA_D1,
                ContractDatabase.Megasena.COLUNA_D2,
                ContractDatabase.Megasena.COLUNA_D3,
                ContractDatabase.Megasena.COLUNA_D4,
                ContractDatabase.Megasena.COLUNA_D5,
                ContractDatabase.Megasena.COLUNA_D6 };
        return saveColumns;
    }

    @Override
    public String getIdColumn() {
        return ContractDatabase.Megasena._ID;
    }

}
