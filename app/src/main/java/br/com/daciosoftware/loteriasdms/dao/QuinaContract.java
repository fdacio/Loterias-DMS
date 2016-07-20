package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.db.ContractDatabase;
import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class QuinaContract implements InterfaceContractDatabase {

    @Override
    public String getTableName() {
        return ContractDatabase.Quina.NOME_TABELA;
    }

    @Override
    public String[] getAllColumns() {
        return ContractDatabase.Quina.COLUNAS;
    }

    @Override
    public String[] getSaveColumns() {
        String[] saveColumns = {ContractDatabase.Megasena._ID,
                ContractDatabase.Quina.COLUNA_NUMERO,
                ContractDatabase.Quina.COLUNA_DATA,
                ContractDatabase.Quina.COLUNA_LOCAL,
                ContractDatabase.Quina.COLUNA_D1,
                ContractDatabase.Quina.COLUNA_D2,
                ContractDatabase.Quina.COLUNA_D3,
                ContractDatabase.Quina.COLUNA_D4,
                ContractDatabase.Quina.COLUNA_D5 };
        return saveColumns;
    }

    @Override
    public String getIdColumn() {
        return ContractDatabase.Megasena._ID;
    }
}
