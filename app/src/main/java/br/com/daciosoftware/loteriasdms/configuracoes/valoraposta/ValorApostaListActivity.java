package br.com.daciosoftware.loteriasdms.configuracoes.valoraposta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.configuracoes.ItemConfiguracao;
import br.com.daciosoftware.loteriasdms.configuracoes.ItemConfiguracaoEditListener;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class ValorApostaListActivity extends AppCompatActivity implements ItemConfiguracaoEditListener {

    private List<ItemConfiguracao> listValoresAposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valores_aposta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildList();
    }

    private void buildList() {
        ListView listViewValorAposta = (ListView) findViewById(R.id.listViewValorAposta);

        listValoresAposta = new ArrayList<>();

        listValoresAposta.add(new ItemConfiguracao(this.getResources().getString(R.string.mega_sena),
                String.format("6 dezenas: R$ %,.2f", new ValorApostaMegasenaDAO(this).getValor())));
        listValoresAposta.add(new ItemConfiguracao(this.getResources().getString(R.string.lotofacil),
                String.format("15 dezenas: R$ %,.2f", new ValorApostaLotofacilDAO(this).getValor())));
        listValoresAposta.add(new ItemConfiguracao(this.getResources().getString(R.string.quina),
                String.format("5 dezenas: R$ %,.2f", new ValorApostaQuinaDAO(this).getValor())));

        if (listViewValorAposta != null) {
            listViewValorAposta.setAdapter(new ValorApostaAdapter(this, listValoresAposta, this));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        Intent intent = new Intent(this, ValorApostaEditActivity.class);

        switch (position) {
            case 0:
                intent.putExtra(Constantes.TYPE_SORTEIO, TypeSorteio.MEGASENA);
                break;
            case 1:
                intent.putExtra(Constantes.TYPE_SORTEIO, TypeSorteio.LOTOFACIL);
                break;
            case 2:
                intent.putExtra(Constantes.TYPE_SORTEIO, TypeSorteio.QUINA);
                break;
        }
        startActivity(intent);

    }
}
