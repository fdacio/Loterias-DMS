package br.com.daciosoftware.loteriasdms.db.contract;

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
    public String getIdColumn() {
        return ContractDatabase.Megasena._ID;
    }
}
