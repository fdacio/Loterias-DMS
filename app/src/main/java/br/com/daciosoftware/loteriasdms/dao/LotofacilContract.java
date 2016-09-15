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
    public String getIdColumn() {
        return ContractDatabase.Lotofacil._ID;
    }
}
