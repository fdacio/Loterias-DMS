package br.com.daciosoftware.loteriasdms.db;

/**
 * Created by Dácio Braga on 04/07/2016.
 */
public interface InterfaceContractDatabase {
    public String getTableName();
    public String[] getAllColumns();
    public String[] getSaveColumns();
    public String getIdColumn();
}
