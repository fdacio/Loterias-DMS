package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.confiraseujogo.ResultadoSeuJogoAdapter;
import br.com.daciosoftware.loteriasdms.confiraseujogo.SeuJogo;
import br.com.daciosoftware.loteriasdms.confiraseujogo.SorteioAcerto;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class DezenasMaisSorteadasActivity extends AppCompatActivity {

    private TypeSorteio typeSorteio;
    private ProgressDialog progressDialog;
    private List<MaisSorteada> listMaisSorteadas;
    private ListView listViewDezenasMaisSorteadas;

    private int qtdeDezenasSelecionadas = 18;
    private int qtdeMinimaDezenasSelecionadas = 0;
    private int qtdeMaximaDezenasSelecionadas = 0;
    private int totalDezenasPorJogo = 80;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dezenas_mais_sorteadas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);

        switch (typeSorteio) {
            case MEGASENA:
                qtdeMinimaDezenasSelecionadas = 6;
                qtdeMaximaDezenasSelecionadas = 15;
                totalDezenasPorJogo = 60;
                break;
            case LOTOFACIL:
                qtdeMinimaDezenasSelecionadas = 15;
                qtdeMaximaDezenasSelecionadas = 18;
                totalDezenasPorJogo = 25;
                break;
            case QUINA:
                qtdeMinimaDezenasSelecionadas = 5;
                qtdeMaximaDezenasSelecionadas = 7;
                totalDezenasPorJogo = 80;
                break;
        }

        listViewDezenasMaisSorteadas = (ListView) findViewById(R.id.listViewDezenasMaisSorteadas);
        listViewDezenasMaisSorteadas.setEmptyView(findViewById(R.id.emptyElement));

        Button btnGerarJogos = (Button) findViewById(R.id.btnGerarJogos);
        btnGerarJogos.setOnClickListener(new OnClickListenerGerarJogos(typeSorteio));

        listarMaisSorteadas(null);
        new StyleOfActivity(this, findViewById(R.id.layout_dezenas_mais_sorteadas)).setStyleInViews(typeSorteio);



    }

    private class Param{

    }

    private void listarMaisSorteadas(Param param) {

        progressDialog = ProgressDialog.show(this, "", "Aguarde...", true, false);

        new Thread() {

            public void run() {
                SorteioDAO sorteioDAO = SorteioDAO.getDAO(DezenasMaisSorteadasActivity.this, typeSorteio);
                listMaisSorteadas = getListMaisSortedas(sorteioDAO.sortListDezenasCrescente(sorteioDAO.listAll()));
                Message msg = new Message();
                msg.what = 100;
                handlerListSorteio.sendMessage(msg);
            }
        }.start();

    }

    private Handler handlerListSorteio = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                listViewDezenasMaisSorteadas.setAdapter(new DezenasMaisSorteadasAdapter(DezenasMaisSorteadasActivity.this, listMaisSorteadas));
            }
            progressDialog.dismiss();
        }
    };


    private int[] getArrayDezenasSelecionadas() {
        int[] arrayDezenasSelecionada = new int[qtdeDezenasSelecionadas];
        //Varre a lista das dezenas selecionadas e insere no arrayDezenasSelecionadas
        for (int i = 0; i < qtdeDezenasSelecionadas; i++) {
            arrayDezenasSelecionada[i] = 1;
        }
        Arrays.sort(arrayDezenasSelecionada);
        return arrayDezenasSelecionada;

    }

    private class OnClickListenerGerarJogos implements View.OnClickListener {
        private TypeSorteio typeSorteio;

        public OnClickListenerGerarJogos(TypeSorteio typeSorteio) {
            this.typeSorteio = typeSorteio;
        }

        @Override
        public void onClick(View v) {
            if (qtdeDezenasSelecionadas < qtdeMinimaDezenasSelecionadas) {
                Toast.makeText(DezenasMaisSorteadasActivity.this, "Selecione no mínimo " + String.valueOf(qtdeMinimaDezenasSelecionadas) + " dezenas", Toast.LENGTH_SHORT).show();
                return;
            }

            if (qtdeDezenasSelecionadas > qtdeMaximaDezenasSelecionadas) {
                Toast.makeText(DezenasMaisSorteadasActivity.this, "Selecione no máximo " + String.valueOf(qtdeMaximaDezenasSelecionadas) + " dezenas", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(DezenasMaisSorteadasActivity.this, GeraJogosActivity.class);
            intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
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


    private int getQtdeVezes(int dezena, int[] arrayDezenasSorteio) {
        int qtdeVezes = 0;
        for (int anArrayDezenasSorteio : arrayDezenasSorteio) {
            if (dezena == anArrayDezenasSorteio) {
                qtdeVezes++;
            }

        }
        return qtdeVezes;
    }

    private List<MaisSorteada> getListMaisSortedas(List<Sorteio> listSorteio) {
        List<MaisSorteada> listMaisSorteadas = new ArrayList<>();

        for (int dezena = 1; dezena <= totalDezenasPorJogo; dezena++) {
            for (Sorteio sorteio : listSorteio) {
                int[] arrayDezenasSorteio = sorteio.getDezenas();
                int qtdeVezes = getQtdeVezes(dezena, arrayDezenasSorteio);
                MaisSorteada maisSorteada = new MaisSorteada();
                maisSorteada.setDezena(dezena);
                maisSorteada.setQtdeVezes(qtdeVezes);
                listMaisSorteadas.add(maisSorteada);
            }
        }
        Collections.reverse(listMaisSorteadas);
        return listMaisSorteadas;
   }

}