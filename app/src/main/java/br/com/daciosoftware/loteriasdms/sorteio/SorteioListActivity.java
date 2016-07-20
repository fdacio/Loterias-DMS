package br.com.daciosoftware.loteriasdms.sorteio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.LotofacilContract;
import br.com.daciosoftware.loteriasdms.dao.LotofacilDAO;
import br.com.daciosoftware.loteriasdms.dao.MegasenaContract;
import br.com.daciosoftware.loteriasdms.dao.MegasenaDAO;
import br.com.daciosoftware.loteriasdms.dao.QuinaContract;
import br.com.daciosoftware.loteriasdms.dao.QuinaDAO;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class SorteioListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View layout = (View) findViewById(R.id.layout_sorteio_list);
        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this,layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);

        ListView listViewSorteio = (ListView) findViewById(R.id.listViewSorteio);
        listViewSorteio.setEmptyView(findViewById(R.id.emptyElement));
        listViewSorteio.setAdapter(getSorteioListAdapter(typeSorteio));

        styleTypeSorteio.setStyleFloatingActionButton(typeSorteio);
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private SorteioListAdapter getSorteioListAdapter(TypeSorteio typeSorteio){
        SorteioDAO sorteioDAO ;
        switch (typeSorteio){
            case MEGASENA: sorteioDAO = new MegasenaDAO(getApplicationContext(),new MegasenaContract());
                break;
            case LOTOFACIL: sorteioDAO = new LotofacilDAO(getApplicationContext(),new LotofacilContract());
                break;
            case QUINA: sorteioDAO = new QuinaDAO(getApplicationContext(),new QuinaContract());
                break;
            default: return null;
        }
        return new SorteioListAdapter(this,sorteioDAO.listAll(),typeSorteio);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
