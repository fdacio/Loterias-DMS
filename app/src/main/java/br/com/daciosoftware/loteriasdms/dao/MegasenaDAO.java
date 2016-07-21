package br.com.daciosoftware.loteriasdms.dao;

import android.content.Context;

import br.com.daciosoftware.loteriasdms.db.InterfaceContractDatabase;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class MegasenaDAO extends SorteioDAO {

    public MegasenaDAO(Context context, InterfaceContractDatabase contract) {
        super(context, contract);

    }

    @Override
    public Megasena getInstanciaEntity() {
        return new Megasena();
    }
}
