package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.ViewIdGenerator;

public class ResultadoSeuJogoActivity extends AppCompatActivity {

    private TypeSorteio typeSorteio;
    private SeuJogo seuJogo;
    private List<TextView> listaTextViewDezenas;
    private ListView listViewResultadoSeuJogo;
    private TextView textViewNumeroConcurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_seu_jogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        seuJogo = (SeuJogo) getIntent().getSerializableExtra(Constantes.SEU_JOGO);

        textViewNumeroConcurso = (TextView) findViewById(R.id.textViewNumeroConcurso);
        textViewNumeroConcurso.setText(String.valueOf(seuJogo.getNumeroConcurso()));

        listViewResultadoSeuJogo = (ListView) findViewById(R.id.listViewResultadoSeuJogo);

        new StyleOfActivity(this, findViewById(R.id.layout_resultado_seu_jogo)).setStyleInViews(typeSorteio);

        buildTextViewDezenas(seuJogo);


        SorteioDAO sorteioDAO = SorteioDAO.getDAO(this, typeSorteio);
        Sorteio sorteio = sorteioDAO.getEntityDezenasCrescente(sorteioDAO.findByNumber(seuJogo.getNumeroConcurso()));
        List<SorteioAcerto> listSorteioAcerto = getListSorteioAcertos(seuJogo, sorteio);
        listViewResultadoSeuJogo.setAdapter(new ResultadoSeuJogoAdapter(this,listSorteioAcerto,typeSorteio));

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


    private void buildTextViewDezenas(SeuJogo seuJogo) {
        TableRow tableRow1 = (TableRow) findViewById(R.id.trow1);
        TableRow tableRow2 = (TableRow) findViewById(R.id.trow2);
        TableRow tableRow3 = (TableRow) findViewById(R.id.trow3);
        TableRow tableRow4 = (TableRow) findViewById(R.id.trow4);
        TableRow tableRow5 = (TableRow) findViewById(R.id.trow5);
        TableRow tableRow6 = (TableRow) findViewById(R.id.trow6);

        int[] arrayDezenas = seuJogo.getDezenas();


        int col = 0;
        for (int i = 0; i < arrayDezenas.length; i++) {
            TableRow.LayoutParams tableRowLayoutParam = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tableRowLayoutParam.column = col;
            tableRowLayoutParam.span = 1;
            col++;
            if(col > 4 && arrayDezenas.length != 6) col = 0;

            TextView textViewDezena = new TextView(this);
            textViewDezena.setId(ViewIdGenerator.generateViewId());
            textViewDezena.setLayoutParams(tableRowLayoutParam);
            textViewDezena.setTextSize(18);
            textViewDezena.setTypeface(null, Typeface.BOLD);
            textViewDezena.setText(String.valueOf(arrayDezenas[i]));

            if (i < 5) {
                tableRow1.addView(textViewDezena);
            } else if ((i == 5) && (arrayDezenas.length == 6)) {
                tableRow1.addView(textViewDezena);
            } else if (i >= 5 && i < 10) {
                tableRow2.addView(textViewDezena);
            } else if (i >= 10 && i < 15) {
                tableRow3.addView(textViewDezena);
            } else if (i >= 15 && i < 20) {
                tableRow4.addView(textViewDezena);
            } else if (i >= 20 && i < 25) {
                tableRow5.addView(textViewDezena);
            } else if (i >= 25 && i < 30) {
                tableRow6.addView(textViewDezena);
            }

        }
    }

    private int getQtdeMinimaAcertos(TypeSorteio typeSorteio){
        switch (typeSorteio) {
            case MEGASENA:
                return 4;
            case LOTOFACIL:
                return 11;
            case QUINA:
                return 2;
            default:
                return 0;
        }
    }


    private int getQtdeAcertos(int[] arrayDezenasSeuJogo, int[] arrayDezenasSorteio){
        int qtdeAcerto = 0;
        for(int i = 0; i < arrayDezenasSeuJogo.length; i++){
            for(int j = 0; j < arrayDezenasSorteio.length; j++){
                if(arrayDezenasSeuJogo[i] == arrayDezenasSorteio[j]){
                    qtdeAcerto++;
                }
            }
        }
        return qtdeAcerto;
    }

    private int[] getDezenasAcertos(int[] arrayDezenasSeuJogo, int[] arrayDezenasSorteio){
        int qtdeAcerto = getQtdeAcertos(arrayDezenasSeuJogo, arrayDezenasSorteio);
        int[] arrayDezenasAcertos = new int[qtdeAcerto];
        int k = 0;
        for(int i = 0; i < arrayDezenasSeuJogo.length; i++){
            for(int j = 0; j < arrayDezenasSorteio.length; j++){
                if(arrayDezenasSeuJogo[i] == arrayDezenasSorteio[j]){
                    arrayDezenasAcertos[k] = arrayDezenasSeuJogo[i];
                    k++;
                }
            }
        }
        return arrayDezenasAcertos;
    }

    private List<SorteioAcerto> getListSorteioAcertos(SeuJogo seuJogo, Sorteio sorteio){
        List<SorteioAcerto> listSorteioAcerto = new ArrayList<>();
        int[] arrayDezenasSeuJogo = seuJogo.getDezenas();
        int[] arrayDezenasSorteio = sorteio.getDezenas();
        int[] arrayDezenasAcertos = getDezenasAcertos(arrayDezenasSeuJogo,arrayDezenasSorteio);
        int qtdeAcerto = arrayDezenasAcertos.length;

        SorteioAcerto sorteioAcerto = new SorteioAcerto();
        sorteioAcerto.setSorteio(sorteio);
        sorteioAcerto.setQtdeAcertos(qtdeAcerto);
        sorteioAcerto.setDezenasAcertos(arrayDezenasAcertos);
        listSorteioAcerto.add(sorteioAcerto);

        return listSorteioAcerto;
    }

    private List<SorteioAcerto> getListSorteioAcertos(SeuJogo seuJogo, List<Sorteio> listSorteio){
        List<SorteioAcerto> listSorteioAcerto = new ArrayList<>();
        for(Sorteio sorteio: listSorteio) {
            int[] arrayDezenasSeuJogo = seuJogo.getDezenas();
            int[] arrayDezenasSorteio = sorteio.getDezenas();
            int[] arrayDezenasAcertos = getDezenasAcertos(arrayDezenasSeuJogo,arrayDezenasSorteio);
            int qtdeAcerto = arrayDezenasAcertos.length;

            if(qtdeAcerto >= getQtdeMinimaAcertos(typeSorteio)) {
                SorteioAcerto sorteioAcerto = new SorteioAcerto();
                sorteioAcerto.setSorteio(sorteio);
                sorteioAcerto.setQtdeAcertos(qtdeAcerto);
                sorteioAcerto.setDezenasAcertos(arrayDezenasAcertos);
                listSorteioAcerto.add(sorteioAcerto);
                listSorteioAcerto.add(sorteioAcerto);
            }
        }
        return listSorteioAcerto;
    }
}
