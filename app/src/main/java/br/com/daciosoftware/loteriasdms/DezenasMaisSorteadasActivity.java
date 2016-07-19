package br.com.daciosoftware.loteriasdms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.daciosoftware.loteriasdms.util.Constantes;

public class DezenasMaisSorteadasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dezenas_mais_sorteadas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View layout = (View) findViewById(R.id.layout_dezenas_mais_sorteadas);
        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this,layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);



        ListView listViewDezenasMaisSorteadas = (ListView) findViewById(R.id.listViewDezenasMaisSorteadas);
        listViewDezenasMaisSorteadas.setEmptyView((TextView) findViewById(R.id.emptyElement));


        Spinner spinnerQtdeDezenasPorJogo = (Spinner) findViewById(R.id.spinnerQtdeDezenas);
        ArrayAdapter<CharSequence> adapter;
        if(typeSorteio == TypeSorteio.MEGASENA) {
            adapter = ArrayAdapter.createFromResource(this, R.array.array_qtde_dezenas_por_jogo_megasena, android.R.layout.simple_spinner_item);
        }else if(typeSorteio == TypeSorteio.LOTOFACIL){
            adapter = ArrayAdapter.createFromResource(this, R.array.array_qtde_dezenas_por_jogo_lotofacil, android.R.layout.simple_spinner_item);
        }else{ //typeSorteio == TypeSorteio.QUINA
            adapter = ArrayAdapter.createFromResource(this, R.array.array_qtde_dezenas_por_jogo_quina, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQtdeDezenasPorJogo.setAdapter(adapter);

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
