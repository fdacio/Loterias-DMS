package br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.ItemConfiguracao;
import br.com.daciosoftware.loteriasdms.configuracoes.ItemConfiguracaoEditListener;
import br.com.daciosoftware.loteriasdms.configuracoes.TypeUrl;
import br.com.daciosoftware.loteriasdms.configuracoes.UrlEditActivity;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class ArquivoResultadoUrlListActivity extends AppCompatActivity implements ItemConfiguracaoEditListener {

    private List<ItemConfiguracao> listUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildList();
    }


    private void buildList() {
        ListView listViewUrls = (ListView) findViewById(R.id.listViewUrls);

        listUrls = new ArrayList<>();

        listUrls.add(new ItemConfiguracao(this.getResources().getString(R.string.mega_sena),
                new ArquivoResultadoUrlMegasenaDAO(this).getUrl()));
        listUrls.add(new ItemConfiguracao(this.getResources().getString(R.string.lotofacil),
                new ArquivoResultadoUrlLotofacilDAO(this).getUrl()));
        listUrls.add(new ItemConfiguracao(this.getResources().getString(R.string.quina),
                new ArquivoResultadoUrlQuinaDAO(this).getUrl()));

        if (listViewUrls != null) {
            listViewUrls.setAdapter(new ArquivoResultadoUrlAdapter(this, listUrls, this));
        }
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

    @Override
    public void onResume() {
        super.onResume();
        buildList();
    }

    @Override
    public void editClicked(int position) {

        Intent intent = new Intent(this, UrlEditActivity.class);

        switch (position) {
            case 0:
                intent.putExtra(Constantes.URLS, TypeUrl.MEGASENA);
                break;
            case 1:
                intent.putExtra(Constantes.URLS, TypeUrl.LOTOFACIL);
                break;
            case 2:
                intent.putExtra(Constantes.URLS, TypeUrl.QUINA);
                break;
        }
        startActivity(intent);
    }

}
