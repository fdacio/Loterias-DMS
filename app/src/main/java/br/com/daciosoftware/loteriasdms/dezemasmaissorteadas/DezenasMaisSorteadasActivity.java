package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

public class DezenasMaisSorteadasActivity extends AppCompatActivity implements DezenasMaisSorteadasClickable{

    private TypeSorteio typeSorteio;
    private ProgressDialog progressDialog;
    private List<MaisSorteada> listMaisSorteadas;
    private ListView listViewDezenasMaisSorteadas;
    private DezenasMaisSorteadasAdapter adapter;
    private TextView textViewFiltroPeriodo;
    private TextView textViewFiltroConcursos;
    private TextView textViewQtdeSelecionada;

    private int qtdeDezenasSelecionadas = 18;
    private int qtdeMinimaDezenasSelecionadas = 0;
    private int qtdeMaximaDezenasSelecionadas = 0;
    private int totalDezenasPorJogo = 80;

    public static final int FILTRO_DMS = 150;
    private Calendar data1Filter = null;
    private Calendar data2Filter = null;
    private int numeroConcursosFilter = 0;


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

        textViewFiltroPeriodo = (TextView) findViewById(R.id.textViewFiltroPeriodo);
        textViewFiltroConcursos = (TextView) findViewById(R.id.textViewFiltroConcursos);

        textViewFiltroPeriodo.setText("Todos");
        textViewFiltroConcursos.setText("Todos");

        listViewDezenasMaisSorteadas = (ListView) findViewById(R.id.listViewDezenasMaisSorteadas);
        listViewDezenasMaisSorteadas.setEmptyView(findViewById(R.id.emptyElement));

        textViewQtdeSelecionada = (TextView) findViewById(R.id.textViewQtdeSelecionada);
        Button btnGerarJogos = (Button) findViewById(R.id.btnGerarJogos);
        btnGerarJogos.setOnClickListener(new OnClickListenerGerarJogos(typeSorteio));



        listarMaisSorteadas(null);
        new StyleOfActivity(this, findViewById(R.id.layout_dezenas_mais_sorteadas)).setStyleInViews(typeSorteio);

    }

    @Override
    public void clicked() {
        qtdeDezenasSelecionadas = 0;
        for(MaisSorteada maisSorteada: listMaisSorteadas){
            if(maisSorteada.isSelecionada()){
                qtdeDezenasSelecionadas++;
            }
        }
        textViewQtdeSelecionada.setText(qtdeDezenasSelecionadas+" dezenas selecionadas");
    }


    private class Param{

    }

    private void listarMaisSorteadas(Param param) {

        progressDialog = ProgressDialog.show(this, "", "Calculando. Aguarde...", true, false);

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
                adapter = new DezenasMaisSorteadasAdapter(DezenasMaisSorteadasActivity.this, listMaisSorteadas, DezenasMaisSorteadasActivity.this);
                listViewDezenasMaisSorteadas.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dezenas_mais_sorteadas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.filtrar:
                Intent intent = new Intent(DezenasMaisSorteadasActivity.this, DezenasMaisSorteadasFiltroActivity.class);
                intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
                if((data1Filter != null)&&(data2Filter != null)) {
                    intent.putExtra(Constantes.FILTRO_DMS_DATA1, data1Filter.getTimeInMillis());
                    intent.putExtra(Constantes.FILTRO_DMS_DATA2, data2Filter.getTimeInMillis());
                }else{
                    intent.putExtra(Constantes.FILTRO_DMS_DATA1, data1Filter);
                    intent.putExtra(Constantes.FILTRO_DMS_DATA2, data2Filter);
                }
                intent.putExtra(Constantes.FILTRO_DMS_CONCURSOS, numeroConcursosFilter);
                startActivityForResult(intent, FILTRO_DMS);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<MaisSorteada> getListMaisSortedas(List<Sorteio> listSorteio) {
        List<MaisSorteada> listMaisSorteadas = new ArrayList<>();
        int qtdeVezes = 0;
        for (int dezena = 1; dezena <= totalDezenasPorJogo; dezena++) {
            for (Sorteio sorteio : listSorteio) {
                int[] arrayDezenasSorteio = sorteio.getDezenas();
                for (int anArrayDezenasSorteio : arrayDezenasSorteio) {
                    if (dezena == anArrayDezenasSorteio) {
                        qtdeVezes ++;
                    }
                }

            }
            MaisSorteada maisSorteada = new MaisSorteada();
            maisSorteada.setDezena(dezena);
            maisSorteada.setQtdeVezes(qtdeVezes);
            listMaisSorteadas.add(maisSorteada);
            qtdeVezes = 0;
        }
        Collections.sort(listMaisSorteadas);
        Collections.reverse(listMaisSorteadas);
        return listMaisSorteadas;
   }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if((requestCode == FILTRO_DMS)&&(resultCode == RESULT_OK)){
            data1Filter = Calendar.getInstance();
            data2Filter = Calendar.getInstance();
            data1Filter.setTimeInMillis(data.getExtras().getLong(Constantes.FILTRO_DMS_DATA1,0));
            data2Filter.setTimeInMillis(data.getExtras().getLong(Constantes.FILTRO_DMS_DATA2,0));
            numeroConcursosFilter = data.getExtras().getInt(Constantes.FILTRO_DMS_CONCURSOS, 0);
            if((data1Filter == null)&&(data2Filter == null)){
                textViewFiltroPeriodo.setText("Todos");
            }else{
                textViewFiltroPeriodo.setText(MyDateUtil.calendarToShortDateBr(data1Filter)+" a "+MyDateUtil.calendarToShortDateBr(data2Filter));
            }

            if(numeroConcursosFilter == 0){
                textViewFiltroConcursos.setText("Todos");
            }else{
                textViewFiltroConcursos.setText(String.valueOf(numeroConcursosFilter)+" últimos concuros");
            }

        }

    }

}