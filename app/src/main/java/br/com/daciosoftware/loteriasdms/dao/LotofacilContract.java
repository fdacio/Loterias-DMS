package br.com.daciosoftware.loteriasdms.dao;

import br.com.daciosoftware.loteriasdms.db.ContractDatabase;
import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class LotofacilContract implements InterfaceContractDatabase {

    @Override
    public String getTableName() {
        return ContractDatabase.Lotofacil.NOME_TABELA;
    }

    @Override
    public String[] getAllColumns() {
        return ContractDatabase.Lotofacil.COLUNAS;
    }

    @Override
    public String[] getSaveColumns() {
        String[] saveColumns = {ContractDatabase.Megasena._ID,
                ContractDatabase.Lotofacil.COLUNA_NUMERO,
                ContractDatabase.Lotofacil.COLUNA_DATA,
                ContractDatabase.Lotofacil.COLUNA_LOCAL,
                ContractDatabase.Lotofacil.COLUNA_D1,
                ContractDatabase.Lotofacil.COLUNA_D2,
                ContractDatabase.Lotofacil.COLUNA_D3,
                ContractDatabase.Lotofacil.COLUNA_D4,
                ContractDatabase.Lotofacil.COLUNA_D5,
                ContractDatabase.Lotofacil.COLUNA_D6,
                ContractDatabase.Lotofacil.COLUNA_D7,
                ContractDatabase.Lotofacil.COLUNA_D8,
                ContractDatabase.Lotofacil.COLUNA_D9,
                ContractDatabase.Lotofacil.COLUNA_D10,
                ContractDatabase.Lotofacil.COLUNA_D11,
                ContractDatabase.Lotofacil.COLUNA_D12,
                ContractDatabase.Lotofacil.COLUNA_D13,
                ContractDatabase.Lotofacil.COLUNA_D14,
                ContractDatabase.Lotofacil.COLUNA_D15};
        return saveColumns;
    }

    @Override
    public String getIdColumn() {
        return ContractDatabase.Lotofacil._ID;
    }
}
