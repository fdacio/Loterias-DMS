package br.com.daciosoftware.loteriasdms.dao;

import android.content.Context;

import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class LotofacilDAO extends SorteioDAO {

    public LotofacilDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);
    }

    @Override
    public Lotofacil getInstancia() {
        return new Lotofacil();
    }
}
