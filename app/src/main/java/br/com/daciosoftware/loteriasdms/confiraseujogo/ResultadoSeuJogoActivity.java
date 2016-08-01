package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
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
    private Sorteio seuJogo;
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
        seuJogo = (Sorteio) getIntent().getSerializableExtra(Constantes.SEU_JOGO);

        textViewNumeroConcurso = (TextView) findViewById(R.id.textViewNumeroConcurso);
        textViewNumeroConcurso.setText(seuJogo.getNumero());
        listViewResultadoSeuJogo = (ListView) findViewById(R.id.listViewResultadoSeuJogo);

        new StyleOfActivity(this, findViewById(R.id.layout_resultado_seu_jogo)).setStyleInViews(typeSorteio);

        buildTextViewDezenas(seuJogo);

        SorteioDAO sorteioDAO = SorteioDAO.getDAO(this, typeSorteio);
        List<SorteioAcerto> listSorteioAcerto = getListSorteioAcertos(seuJogo, sorteioDAO.findByNumber(seuJogo.getNumero()));
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


    private void buildTextViewDezenas(Sorteio seuJogo) {
        TableRow tableRow1 = (TableRow) findViewById(R.id.trow1);
        TableRow tableRow2 = (TableRow) findViewById(R.id.trow2);
        TableRow tableRow3 = (TableRow) findViewById(R.id.trow3);
        int qtdeTextView;
        switch (typeSorteio) {
            case MEGASENA:
                qtdeTextView = 6;
                break;
            case LOTOFACIL:
                qtdeTextView = 15;
                break;
            case QUINA:
                qtdeTextView = 5;
                break;
            default:
                qtdeTextView = 0;
                break;
        }

        listaTextViewDezenas = new ArrayList<>();
        java.lang.reflect.Method methodGet = null;
        for (int i = 0; i < qtdeTextView; i++) {
            TableRow.LayoutParams tableRowLayoutParam = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    i);
            TextView textViewDezena = new TextView(this);
            textViewDezena.setId(ViewIdGenerator.generateViewId());
            textViewDezena.setLayoutParams(tableRowLayoutParam);
            textViewDezena.setInputType(InputType.TYPE_CLASS_NUMBER);

            String methodName = "getD" + String.valueOf(i + 1);
            try {
                methodGet = seuJogo.getClass().getMethod(methodName);
            } catch (SecurityException | NoSuchMethodException e) {
            }

            if (methodGet != null) {
                try {
                    Integer valorDezena = (Integer) methodGet.invoke(seuJogo);
                    textViewDezena.setText(String.valueOf(valorDezena.intValue()));
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                }
            }

            listaTextViewDezenas.add(textViewDezena);
            if (i < 5) {
                tableRow1.addView(textViewDezena);
            } else if ((i == 5) && (qtdeTextView == 6)) {
                tableRow1.addView(textViewDezena);
            } else if (i >= 5 && i < 10) {
                tableRow2.addView(textViewDezena);
            } else {
                tableRow3.addView(textViewDezena);
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

    private List<SorteioAcerto> getListSorteioAcertos(Sorteio seuJogo, Sorteio sorteio){
        List<SorteioAcerto> listSorteioAcerto = new ArrayList<>();
        int[] arrayDezenasSeuJogo = seuJogo.getDezenas();
        int[] arrayDezenasSorteio = sorteio.getDezenas();
        int qtdeAcerto = getQtdeAcertos(arrayDezenasSeuJogo, arrayDezenasSorteio);
        SorteioAcerto sorteioAcerto = new SorteioAcerto();
        sorteioAcerto.setNumero(sorteio.getNumero());
        sorteioAcerto.setData(sorteio.getData());
        Arrays.sort(arrayDezenasSorteio);
        sorteioAcerto.setDezenas(arrayDezenasSorteio);
        sorteioAcerto.setQtdeAcertos(qtdeAcerto);
        listSorteioAcerto.add(sorteioAcerto);
        return listSorteioAcerto;
    }

    private List<SorteioAcerto> getListSorteioAcertos(Sorteio seuJogo, List<Sorteio> listSorteio){
        List<SorteioAcerto> listSorteioAcerto = new ArrayList<>();
        for(Sorteio sorteio: listSorteio) {
            int[] arrayDezenasSeuJogo = seuJogo.getDezenas();
            int[] arrayDezenasSorteio = sorteio.getDezenas();
            int qtdeAcerto = getQtdeAcertos(arrayDezenasSeuJogo, arrayDezenasSorteio);

            if(qtdeAcerto >= getQtdeMinimaAcertos(typeSorteio)) {
                SorteioAcerto sorteioAcerto = new SorteioAcerto();
                sorteioAcerto.setNumero(sorteio.getNumero());
                sorteioAcerto.setData(sorteio.getData());
                Arrays.sort(arrayDezenasSorteio);
                sorteioAcerto.setDezenas(arrayDezenasSorteio);
                sorteioAcerto.setQtdeAcertos(qtdeAcerto);
                listSorteioAcerto.add(sorteioAcerto);
            }
        }
        return listSorteioAcerto;
    }
}
