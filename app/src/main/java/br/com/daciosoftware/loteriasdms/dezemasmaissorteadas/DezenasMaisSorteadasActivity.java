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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

public class DezenasMaisSorteadasActivity extends AppCompatActivity implements DezenasMaisSorteadasClickable {

    private TypeSorteio typeSorteio;
    private ProgressDialog progressDialog;
    private List<MaisSorteada> listMaisSorteadas;
    private ListView listViewDezenasMaisSorteadas;
    private TextView textViewFiltroPeriodo;
    private TextView textViewFiltroConcursos;
    private TextView textViewQtdeSelecionada;

    private int qtdeDezenasSelecionadas = 0;
    private int qtdeMinimaDezenasSelecionadas = 0;
    private int qtdeMaximaDezenasSelecionadas = 0;
    private int totalDezenasPorJogo = 0;

    public static final int FILTRO_DMS = 150;
    private Calendar data1;
    private Calendar data2;
    private int numeroConcursos;
    private String labelPeriodo;
    private String labelConcursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dezenas_mais_sorteadas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        listViewDezenasMaisSorteadas = (ListView) findViewById(R.id.listViewDezenasMaisSorteadas);
        listViewDezenasMaisSorteadas.setEmptyView(findViewById(R.id.emptyElement));

        textViewQtdeSelecionada = (TextView) findViewById(R.id.textViewQtdeSelecionada);
        Button btnGerarJogos = (Button) findViewById(R.id.buttonDezenasPorJogo);
        if (btnGerarJogos != null) {
            btnGerarJogos.setOnClickListener(new OnClickListenerGerarJogos(typeSorteio));
        }

        removerFiltro();
        listarMaisSorteadas(null);
        new StyleOfActivity(this, findViewById(R.id.layout_dezenas_mais_sorteadas)).setStyleInViews(typeSorteio);

    }

    @Override
    public void clicked() {
        qtdeDezenasSelecionadas = 0;
        for (MaisSorteada maisSorteada : listMaisSorteadas) {
            if (maisSorteada.isSelecionada()) {
                qtdeDezenasSelecionadas++;
            }
        }
        String label = String.format(getResources().getString(R.string.dezenas_selecionadas),qtdeDezenasSelecionadas);
        textViewQtdeSelecionada.setText(label);
    }

    private int[] getDezenasSelecionads(){
        int[] dezenasSelecionadas = new int[qtdeDezenasSelecionadas];
        int i = 0;
        for (MaisSorteada maisSorteada : listMaisSorteadas) {
            if (maisSorteada.isSelecionada()) {
                dezenasSelecionadas[i] = maisSorteada.getDezena();
                i++;
            }
        }

        return dezenasSelecionadas;
    }

    private void removerFiltro(){
        textViewFiltroPeriodo.setText(getResources().getString(R.string.todos));
        textViewFiltroConcursos.setText(getResources().getString(R.string.todos));
        data1 = null;
        data2 = null;
        numeroConcursos = 0;

    }

    private class Param {
        private Calendar data1;
        private Calendar data2;
        private int numeroConcursos;
        public Param(){ }

        public Calendar getData1() {
            return data1;
        }

        public void setData1(Calendar data1) {
            this.data1 = data1;
        }

        public Calendar getData2() {
            return data2;
        }

        public void setData2(Calendar data2) {
            this.data2 = data2;
        }

        public int getNumeroConcursos() {
            return numeroConcursos;
        }

        public void setNumeroConcursos(int numeroConcursos) {
            this.numeroConcursos = numeroConcursos;
        }
    }

    private void listarMaisSorteadas(final Param param) {

        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.calculando), true, false);

        new Thread() {

            public void run() {
                SorteioDAO sorteioDAO = SorteioDAO.getDAO(DezenasMaisSorteadasActivity.this, typeSorteio);
                List<Sorteio> listSorteio;

                if(param == null) {
                    listSorteio = sorteioDAO != null ? sorteioDAO.listAll() : null;
                }else{
                    if(param.getData1() != null && param.getData2() != null) {
                        listSorteio = sorteioDAO != null ? sorteioDAO.listBetweenDate(param.getData1(), param.getData2()) : null;
                    }else{
                        listSorteio = sorteioDAO != null ? sorteioDAO.listAll() : null;
                    }

                    if(param.getNumeroConcursos() > 0){
                        listSorteio = sorteioDAO != null ? sorteioDAO.listCountLast(param.getNumeroConcursos(), listSorteio) : null;
                    }
                }

                listMaisSorteadas = getListMaisSortedas(sorteioDAO != null ? sorteioDAO.sortListDezenasCrescente(listSorteio) : null);

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
                DezenasMaisSorteadasAdapter adapter = new DezenasMaisSorteadasAdapter(DezenasMaisSorteadasActivity.this, listMaisSorteadas, DezenasMaisSorteadasActivity.this);
                listViewDezenasMaisSorteadas.setAdapter(adapter);
            }
            progressDialog.dismiss();
        }
    };

    private class OnClickListenerGerarJogos implements View.OnClickListener {
        private TypeSorteio typeSorteio;

        public OnClickListenerGerarJogos(TypeSorteio typeSorteio) {
            this.typeSorteio = typeSorteio;
        }

        @Override
        public void onClick(View v) {
            if (qtdeDezenasSelecionadas < qtdeMinimaDezenasSelecionadas) {
                String msg = String.format(getResources().getString(R.string.selecione_minimo),qtdeMinimaDezenasSelecionadas);
                Toast.makeText(DezenasMaisSorteadasActivity.this, msg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (qtdeDezenasSelecionadas > qtdeMaximaDezenasSelecionadas) {
                String msg = String.format(getResources().getString(R.string.selecione_maximo),qtdeMaximaDezenasSelecionadas);
                Toast.makeText(DezenasMaisSorteadasActivity.this, msg, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(DezenasMaisSorteadasActivity.this, JogosGeradosActivity.class);
            intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
            intent.putExtra(Constantes.DEZENAS_SELECIONADAS, getDezenasSelecionads());
            intent.putExtra(Constantes.QTDE_MIN_DEZENAS_JOGO, qtdeMinimaDezenasSelecionadas);
            intent.putExtra(Constantes.QTDE_MAX_DEZENAS_JOGO, getDezenasSelecionads().length);
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
                if ((data1 != null) && (data2 != null)) {
                    intent.putExtra(Constantes.FILTRO_DMS_DATA1, data1.getTimeInMillis());
                    intent.putExtra(Constantes.FILTRO_DMS_DATA2, data2.getTimeInMillis());
                } else {
                    intent.putExtra(Constantes.FILTRO_DMS_DATA1, 0);
                    intent.putExtra(Constantes.FILTRO_DMS_DATA2, 0);
                }
                intent.putExtra(Constantes.FILTRO_DMS_CONCURSOS, numeroConcursos);
                startActivityForResult(intent, FILTRO_DMS);
                return true;

            case R.id.removerFiltro:
                removerFiltro();
                listarMaisSorteadas(null);
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
                        qtdeVezes++;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == FILTRO_DMS) && (resultCode == RESULT_OK)) {

            Bundle extras = data.getExtras();
            if (extras != null) {

                Param param = null;

                long timeInMillis1 = extras.getLong(Constantes.FILTRO_DMS_DATA1);
                long timeInMillis2 = extras.getLong(Constantes.FILTRO_DMS_DATA2);
                int  numeroConcursos = extras.getInt(Constantes.FILTRO_DMS_CONCURSOS);

                if(timeInMillis1 > 0 && timeInMillis2 > 0){
                    data1 = Calendar.getInstance();
                    data2 = Calendar.getInstance();
                    data1.setTimeInMillis(timeInMillis1);
                    data2.setTimeInMillis(timeInMillis2);
                    data2.set(Calendar.DAY_OF_MONTH, this.data2.getActualMaximum(Calendar.DAY_OF_MONTH));
                    String labelButtonData1 = MyDateUtil.calendarToShortDateBr(data1);
                    String labelButtonData2 = MyDateUtil.calendarToShortDateBr(data2);
                    labelPeriodo = labelButtonData1.toUpperCase() + " a " + labelButtonData2.toUpperCase();
                    param = new Param();
                    param.setData1(data1);
                    param.setData2(data2);
                }else{
                    data1 = null;
                    data2 = null;
                    labelPeriodo = getResources().getString(R.string.todos);
                }

                if (numeroConcursos > 0) {
                    this.numeroConcursos = numeroConcursos;
                    labelConcursos = String.format(getResources().getString(R.string.ultimos_concursos),numeroConcursos);
                    if (param == null)param = new Param();
                    param.setNumeroConcursos(numeroConcursos);

                } else {
                    this.numeroConcursos = 0;
                    labelConcursos = getResources().getString(R.string.todos);

                }
                listarMaisSorteadas(param);

            }
            textViewFiltroPeriodo.setText(labelPeriodo);
            textViewFiltroConcursos.setText(labelConcursos);

        }

    }

}