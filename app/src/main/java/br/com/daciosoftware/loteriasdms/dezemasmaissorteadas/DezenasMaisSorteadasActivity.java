package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class DezenasMaisSorteadasActivity extends AppCompatActivity {

    private int qtdeDezenasSelecionadas = 18;
    private int qtdeMinimaDezenasSelecionadas = 0;
    private int qtdeMaximaDezenasSelecionadas = 0;
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

        switch (typeSorteio){
            case MEGASENA:
                qtdeMinimaDezenasSelecionadas = 6;
                qtdeMaximaDezenasSelecionadas = 15;
                break;
            case LOTOFACIL:
                qtdeMinimaDezenasSelecionadas = 15;
                qtdeMaximaDezenasSelecionadas = 18;
                break;
            case QUINA:
                qtdeMinimaDezenasSelecionadas = 5;
                qtdeMinimaDezenasSelecionadas = 7;
                break;
        }

        ListView listViewDezenasMaisSorteadas = (ListView) findViewById(R.id.listViewDezenasMaisSorteadas);
        listViewDezenasMaisSorteadas.setEmptyView((TextView) findViewById(R.id.emptyElement));

        Button btnGerarJogos = (Button) findViewById(R.id.btnGerarJogos);
        btnGerarJogos.setOnClickListener(new OnClickListenerGerarJogos(typeSorteio));

    }

    private int[] getArrayDezenasSelecionadas(){
        int[] arrayDezenasSelecionada = new int[qtdeDezenasSelecionadas];
        //Varre a lista das dezenas selecionadas e insere no arrayDezenasSelecionadas
        for(int i=0; i<qtdeDezenasSelecionadas; i++){
            arrayDezenasSelecionada[i] = 1;
        }
        Arrays.sort(arrayDezenasSelecionada);
        return arrayDezenasSelecionada;

    }

    private class OnClickListenerGerarJogos implements View.OnClickListener{
        private TypeSorteio typeSorteio;
        public OnClickListenerGerarJogos(TypeSorteio typeSorteio){
            this.typeSorteio = typeSorteio;
        }
        @Override
        public void onClick(View v) {

            if(qtdeDezenasSelecionadas < qtdeMinimaDezenasSelecionadas){
                Toast.makeText( DezenasMaisSorteadasActivity.this, "Selecione no mínimo "+ String.valueOf(qtdeMinimaDezenasSelecionadas) + " dezenas", Toast.LENGTH_SHORT).show();
                return;
            }

            if(qtdeDezenasSelecionadas > qtdeMinimaDezenasSelecionadas){
                Toast.makeText( DezenasMaisSorteadasActivity.this, "Selecione no máximo "+ String.valueOf(qtdeMaximaDezenasSelecionadas) + " dezenas", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent  intent = new Intent(DezenasMaisSorteadasActivity.this, GeraJogosActivity.class);
            intent.putExtra(Constantes.TYPE_SORTEIO,typeSorteio);
            intent.putExtra(Constantes.DEZENAS_SELECIONADAS, getArrayDezenasSelecionadas());
            startActivity(intent);
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
