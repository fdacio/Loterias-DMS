package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogDate;
import br.com.daciosoftware.loteriasdms.util.DialogNumber;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

public class DezenasMaisSorteadasFiltroActivity extends AppCompatActivity {

    private TypeSorteio typeSorteio;
    private int numeroConcuros;
    private Calendar data1;
    private Calendar data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dezenas_mais_sorteadas_filtro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_dezenas_mais_sorteadas_filtro));
        toolbar.setSubtitle(getResources().getString(R.string.subtitle_activity_dezenas_mais_sorteadas_filtro));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);


        final Button buttonData1 = (Button) findViewById(R.id.buttonData1);
        final Button buttonData2 = (Button) findViewById(R.id.buttonData2);
        Button buttonConcuros = (Button) findViewById(R.id.buttonConcursos);
        Button buttonCancelar = (Button) findViewById(R.id.buttonCancelar);
        Button buttonOK = (Button) findViewById(R.id.buttonOK);

        if (buttonData1 != null) {
            buttonData1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DialogDate((Button) v, true).show();
                }
            });
        }

        if (buttonData2 != null) {
            buttonData2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DialogDate((Button) v, true).show();
                }
            });
        }

        if (buttonConcuros != null) {
            buttonConcuros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogNumber dialogNumber = new DialogNumber(DezenasMaisSorteadasFiltroActivity.this);
                    dialogNumber.setOnClickOKListener(new DialogNumberPickerOnClickOKListener(dialogNumber, (Button) view));
                    dialogNumber.setTitle("Quantidade de Concursos");
                    dialogNumber.setStartValue(numeroConcuros>0?numeroConcuros:2);
                    dialogNumber.setMinValue(2);
                    dialogNumber.setMaxValue(4000);
                    dialogNumber.show();
                }
            });
        }

        if (buttonCancelar != null) {
            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (buttonOK != null) {
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        data1 = MyDateUtil.dateShortBrToCalendar(buttonData1.getText().toString());
                        data2 = MyDateUtil.dateShortBrToCalendar(buttonData2.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(DezenasMaisSorteadasFiltroActivity.this, "Data1: " +MyDateUtil.calendarToDateBr(data1),Toast.LENGTH_SHORT).show();
                    Toast.makeText(DezenasMaisSorteadasFiltroActivity.this, "Data2: " +MyDateUtil.calendarToDateBr(data2),Toast.LENGTH_SHORT).show();

                    getIntent().putExtra(Constantes.FILTRO_DMS_DATA1,data1.getTimeInMillis());
                    getIntent().putExtra(Constantes.FILTRO_DMS_DATA2,data2.getTimeInMillis());
                    getIntent().putExtra(Constantes.FILTRO_DMS_CONCURSOS,numeroConcuros);
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        }

        data1 = Calendar.getInstance();
        data2 = Calendar.getInstance();
        data1.setTimeInMillis(getIntent().getExtras().getLong(Constantes.FILTRO_DMS_DATA1,0));
        data2.setTimeInMillis(getIntent().getExtras().getLong(Constantes.FILTRO_DMS_DATA2,0));
        numeroConcuros = getIntent().getExtras().getInt(Constantes.FILTRO_DMS_CONCURSOS, 0);

        if(((data1 == null)&&(data2 == null))||((data1.get(Calendar.YEAR)<1990)&&(data2.get(Calendar.YEAR)<1990))){
            buttonData1.setText("Todos");
            buttonData2.setText("Todos");
        }else{
            buttonData1.setText(MyDateUtil.calendarToShortDateBr(data1));
            buttonData2.setText(MyDateUtil.calendarToShortDateBr(data2));
        }

        if(numeroConcuros == 0){
            buttonConcuros.setText("Todos");
        }else{
            buttonConcuros.setText(String.valueOf(numeroConcuros)+" últimos concuros");
        }
        new StyleOfActivity(this, findViewById(R.id.layout_dezenas_mais_sorteadas_filtro)).setStyleInViews(typeSorteio);

    }

    /**
     * Classe interna para implementar o numero de concuros
     */
    private class DialogNumberPickerOnClickOKListener implements DialogInterface.OnClickListener {
        private DialogNumber dialogNumber;
        private Button button;

        public DialogNumberPickerOnClickOKListener(DialogNumber dialogNumber, Button button) {
            this.dialogNumber = dialogNumber;
            this.button = button;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            numeroConcuros = dialogNumber.getValueReturn();
            this.button.setText(String.valueOf(numeroConcuros) + " últimos concursos");
            dialog.dismiss();
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
