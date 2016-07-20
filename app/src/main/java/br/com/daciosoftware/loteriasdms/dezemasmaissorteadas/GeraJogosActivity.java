package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class GeraJogosActivity extends AppCompatActivity {

    private int[] arrayDezenasSelecionda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gera_jogos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Seta o logo e o titulo do tipo do sorteio com o padrao de cores
         */
        View layout = (View) findViewById(R.id.layout_activity_gera_jogos);
        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this,layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);

        /**
         * Captura o array de dezenas selecionadas via Intente
         */
        arrayDezenasSelecionda = (int[]) getIntent().getSerializableExtra(Constantes.DEZENAS_SELECIONADAS);
        TextView textViewDezenasSelecionadas = (TextView) findViewById(R.id.textViewDezenasSelecionadas);
        textViewDezenasSelecionadas.setText(Arrays.toString(arrayDezenasSelecionda));

        /**
         *  Carrega o spinner com a quantidade de dezenas possíveis para geração do jogos
         *  conforme o tipo de sorteio
         */
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

        /**
         * Instancia o botão e registrar o evento onClick para geração de jogos
         */
        Button btnGerarJogos = (Button) findViewById(R.id.btnGerarJogos);
        btnGerarJogos.setOnClickListener(new OnClickListenerGerarJogos(typeSorteio));

        /*
        * Instancia a listview para exibir os jogos gerados
         */
        ListView listViewGeraJogos = (ListView) findViewById(R.id.listViewGeraJogos);
        listViewGeraJogos.setEmptyView((TextView) findViewById(R.id.emptyElement));

    }

    /*
    Classe que implementa o evento de onclick do botao gerar jogos
     */
    private class OnClickListenerGerarJogos implements View.OnClickListener{
        private TypeSorteio typeSorteio;
        public OnClickListenerGerarJogos(TypeSorteio typeSorteio){
            this.typeSorteio = typeSorteio;
        }
        @Override
        public void onClick(View v) {
           // Processar geração de jogos aqui
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


}
