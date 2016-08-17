package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogDate;
import br.com.daciosoftware.loteriasdms.util.DialogNumber;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;
import br.com.daciosoftware.loteriasdms.util.NumberPickerDialog;

public class DezenasMaisSorteadasFiltroActivity extends AppCompatActivity {

    private Calendar data1;
    private Calendar data2;
    private int numeroConcursos;
    private String labelButtonData1;
    private String labelButtonData2;
    private String labelButtonConcursos;
    private int maxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dezenas_mais_sorteadas_filtro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.title_activity_dezenas_mais_sorteadas_filtro));
            toolbar.setSubtitle(getResources().getString(R.string.subtitle_activity_dezenas_mais_sorteadas_filtro));
            setSupportActionBar(toolbar);
        }
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setParamFromIntent();

        Button buttonData1 = (Button) findViewById(R.id.buttonData1);
        if(buttonData1 != null){
            buttonData1.setText(labelButtonData1);
            buttonData1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DialogDate((Button) v, true).show();
                }
            });

        }

        Button buttonData2 = (Button) findViewById(R.id.buttonData2);
        if(buttonData2 != null){
            buttonData2.setText(labelButtonData2);
            buttonData2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DialogDate((Button) v, true).show();
                }
            });
        }

        Button buttonConcursos = (Button) findViewById(R.id.buttonConcursos);
        if (buttonConcursos != null) {
            buttonConcursos.setText(labelButtonConcursos);
            buttonConcursos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogNumber dialogNumber = new DialogNumber((Button)view,
                            R.layout.dialog_number_picker,
                            new OnNumberSetListener((Button)view)
                    );

                    dialogNumber.setTitle(getResources().getString(R.string.quantidade_concursos));
                    dialogNumber.setStartValue(numeroConcursos>0?numeroConcursos:2);
                    dialogNumber.setMinValue(2);
                    dialogNumber.setMaxValue(maxValue);
                    dialogNumber.show();
                }
            });
        }

        Button buttonCancelar = (Button) findViewById(R.id.buttonCancelar);
        if (buttonCancelar != null) {
            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        Button buttonOK = (Button) findViewById(R.id.buttonOK);
        if (buttonOK != null) {
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long timeInMillis1 = 0;
                    long timeInMillis2 = 0;
                    setLabelDateFromButtons();
                    try {
                        data1 = MyDateUtil.dateShortBrToCalendar(labelButtonData1);
                        data2 = MyDateUtil.dateShortBrToCalendar(labelButtonData2);
                        timeInMillis1 = data1.getTimeInMillis();
                        timeInMillis2 = data2.getTimeInMillis();
                    } catch (ParseException ignored) {}
                    Intent intent = new Intent();
                    intent.putExtra(Constantes.FILTRO_DMS_DATA1,timeInMillis1);
                    intent.putExtra(Constantes.FILTRO_DMS_DATA2,timeInMillis2);
                    intent.putExtra(Constantes.FILTRO_DMS_CONCURSOS, numeroConcursos);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        new StyleOfActivity(this, findViewById(R.id.layout_dezenas_mais_sorteadas_filtro)).setStyleInViews(typeSorteio);

        SorteioDAO sorteioDAO = SorteioDAO.getDAO(DezenasMaisSorteadasFiltroActivity.this, typeSorteio);

        maxValue = (sorteioDAO.findLast()!= null)?sorteioDAO.findLast().getNumero():2;

    }

    private void setLabelDateFromButtons(){
        Button buttonData1 = (Button) findViewById(R.id.buttonData1);
        if(buttonData1 != null) {
            labelButtonData1 = buttonData1.getText().toString();
        }
        Button buttonData2 = (Button) findViewById(R.id.buttonData2);
        if(buttonData2 != null) {
            labelButtonData2 = buttonData2.getText().toString();
        }
    }

    private void setParamFromIntent(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long timeInMillis1 = extras.getLong(Constantes.FILTRO_DMS_DATA1);
            long timeInMillis2 = extras.getLong(Constantes.FILTRO_DMS_DATA2);
            int  numeroConcuros = extras.getInt(Constantes.FILTRO_DMS_CONCURSOS);

            if(timeInMillis1 > 0){
                data1 = Calendar.getInstance();
                data1.setTimeInMillis(timeInMillis1);
                labelButtonData1 = MyDateUtil.calendarToShortDateBr(data1);
            }else{
                data1 = null;
                labelButtonData1 = getResources().getString(R.string.todos);
            }

            if(timeInMillis2 > 0){
                data2 = Calendar.getInstance();
                data2.setTimeInMillis(timeInMillis2);
                labelButtonData2 = MyDateUtil.calendarToShortDateBr(data2);
            }else{
                data2 = null;
                labelButtonData2 = getResources().getString(R.string.todos);
            }


            if (numeroConcuros > 0) {
                this.numeroConcursos = numeroConcuros;
                labelButtonConcursos = String.format(getResources().getString(R.string.ultimos_concursos),numeroConcuros);

            } else {
                this.numeroConcursos = 0;
                labelButtonConcursos = getResources().getString(R.string.todos);

            }
        }

    }

     /**
     * Classe interna para implementar o retorno do DialogNumber
      *
     */
     private class OnNumberSetListener implements NumberPickerDialog.OnNumberSetListener {

         private TextView textView;
         public OnNumberSetListener(TextView textView){
             this.textView = textView;

         }

         @Override
         public void onNumberSet(int number) {
             String label = String.format(getResources().getString(R.string.ultimos_concursos),number);
             textView.setText(label);
             numeroConcursos = number;
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
