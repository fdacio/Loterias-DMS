package br.com.daciosoftware.loteriasdms.sorteio;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAOFactory;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.pojo.SorteioFactory;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.DialogDate;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;
import br.com.daciosoftware.loteriasdms.util.ViewIdGenerator;

public class SorteioEditActivity extends AppCompatActivity {

    private Sorteio sorteio;
    private TypeSorteio typeSorteio;
    private List<String> fieldsValidate;
    private EditText editTextNumero;
    private Button buttonData;
    private EditText editTextLocal;
    private SorteioDAO sorteioDAO;
    private List<EditText> listaEditDezenas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        sorteioDAO = SorteioDAOFactory.getInstance(getApplicationContext(), typeSorteio);

        editTextNumero = (EditText) findViewById(R.id.editTextNumero);
        buttonData = (Button) findViewById(R.id.buttonData);
        editTextLocal = (EditText) findViewById(R.id.editTextLocal);

        //Setar data do dia no botao
        Calendar dataAtual = Calendar.getInstance();
        buttonData.setText(MyDateUtil.calendarToDateBr(dataAtual));

        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogDate(buttonData).show();
            }
        });

        new StyleOfActivity(this, findViewById(R.id.layout_activity_sorteio_edit)).setStyleInViews(typeSorteio);

        buildEdits();
        loadSorteioInForm();
    }


    private void buildEdits() {
        TableRow tableRow1 = (TableRow) findViewById(R.id.trow1);
        TableRow tableRow2 = (TableRow) findViewById(R.id.trow2);
        TableRow tableRow3 = (TableRow) findViewById(R.id.trow3);
        int qtdeEdit;
        switch (typeSorteio) {
            case MEGASENA:
                qtdeEdit = 6;
                break;
            case LOTOFACIL:
                qtdeEdit = 15;
                break;
            case QUINA:
                qtdeEdit = 5;
                break;
            default:
                qtdeEdit = 0;
                break;
        }

        listaEditDezenas = new ArrayList<>();
        for (int i = 0; i < qtdeEdit; i++) {
            TableRow.LayoutParams tableRowLayoutParam = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    i);
            EditText edtDezena = new EditText(this);
            edtDezena.setId(ViewIdGenerator.generateViewId());
            edtDezena.setLayoutParams(tableRowLayoutParam);
            edtDezena.setInputType(InputType.TYPE_CLASS_NUMBER);
            listaEditDezenas.add(edtDezena);

            if (i < 5 && tableRow1 != null) {
                tableRow1.addView(edtDezena);
            } else if ((i == 5) && (qtdeEdit == 6) && tableRow1 != null) {
                tableRow1.addView(edtDezena);
            } else if (i >= 5 && i < 10 && tableRow2 != null) {
                tableRow2.addView(edtDezena);
            } else if(tableRow3 != null){
                tableRow3.addView(edtDezena);
            }

        }
    }

    private void loadSorteioInForm() {
        Long id;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong(Constantes.ID_INSERT_UPDATE);
            this.sorteio = sorteioDAO.findById(id);
            if (this.sorteio != null) {
                editTextNumero.setText(String.valueOf(this.sorteio.getNumero()));
                buttonData.setText(MyDateUtil.calendarToDateBr(this.sorteio.getData()));
                editTextLocal.setText(this.sorteio.getLocal());
                int[] dezenas = this.sorteio.getDezenas();
                for (int i = 0; i < listaEditDezenas.size(); i++) {
                    EditText edtDezena = listaEditDezenas.get(i);
                    edtDezena.setText(String.valueOf(dezenas[i]));
                }
            }
        }
    }

    private void setSorteioFromForm() throws ParseException {
        if (this.sorteio == null) {
            this.sorteio = SorteioFactory.getInstance(typeSorteio);
        }
        int numero = Integer.parseInt(editTextNumero.getText().toString());
        Calendar data = MyDateUtil.dateBrToCalendar(buttonData.getText().toString());
        String local = editTextLocal.getText().toString();

        this.sorteio.setNumero(numero);
        this.sorteio.setData(data);
        this.sorteio.setLocal(local);

        int[] dezenas = new int[listaEditDezenas.size()];

        for (int i = 0; i < listaEditDezenas.size(); i++) {
            EditText edtDezena = listaEditDezenas.get(i);
            int dezena = Integer.parseInt(edtDezena.getText().toString());
            dezenas[i] = dezena;
        }
        this.sorteio.setDezenas(dezenas);
    }


    private boolean validateForm() {
        fieldsValidate = new ArrayList<>();
        String numero = editTextNumero.getText().toString();
        String local = editTextLocal.getText().toString();

        if(numero.equals("")){
            fieldsValidate.add("Número");
        }

        if(local.equals("")){
            fieldsValidate.add("Local");
        }

        for (int i = 0; i < listaEditDezenas.size(); i++) {
            EditText edtDezena = listaEditDezenas.get(i);
            if(edtDezena.getText().toString().equals("")){
                fieldsValidate.add("Dezena "+(i+1));
            }
        }

        return fieldsValidate.size() <= 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                if (validateForm()) {
                    try {
                        setSorteioFromForm();
                        sorteioDAO.save(this.sorteio);
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } catch (SQLException | ParseException e) {
                        new DialogBox(this, DialogBox.DialogBoxType.INFORMATION, "Error", e.getMessage()).show();
                    }
                } else {
                    new DialogBox(this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.msg_validade_form), this.fieldsValidate.toString()).show();
                }
                return true;
            case R.id.desfazer:
                loadSorteioInForm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
