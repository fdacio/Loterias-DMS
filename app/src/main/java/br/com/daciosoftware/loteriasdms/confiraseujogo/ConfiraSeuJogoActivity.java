package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.DialogNumber;
import br.com.daciosoftware.loteriasdms.util.NumberPickerDialog;
import br.com.daciosoftware.loteriasdms.util.ViewIdGenerator;

public class ConfiraSeuJogoActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 200;
    private TypeSorteio typeSorteio;
    private List<String> fieldsValidate;
    private EditText editTextNumeroConcurso;
    private List<EditText> listaEditDezenas;
    private int qtdeEdit;
    private int qtdeMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confira_seu_jogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);

        editTextNumeroConcurso = (EditText) findViewById(R.id.editTextNumeroConcurso);

        Button buttonNumberPiker = (Button) findViewById(R.id.buttonNumberPiker);
        buttonNumberPiker.setOnClickListener(new DialogNumberPickerOnClickListener());

        Button buttonConferir = (Button) findViewById(R.id.buttonConferir);
        buttonConferir.setOnClickListener(new ConferirOnClickListener());

        switch (typeSorteio) {
            case MEGASENA:
                qtdeEdit = 6;
                qtdeMin = 6;
                break;
            case LOTOFACIL:
                qtdeEdit = 15;
                qtdeMin = 15;
                break;
            case QUINA:
                qtdeEdit = 5;
                qtdeMin = 5;
                break;
            default:
                qtdeEdit = 0;
                break;
        }
        String label = String.format(getResources().getString(R.string.qtde_dezenas),qtdeEdit);
        buttonNumberPiker.setText(label);
        buildEdits(qtdeEdit);

        new StyleOfActivity(this, findViewById(R.id.layout_confira_seu_jogo)).setStyleInViews(typeSorteio);
    }


    private class DialogNumberPickerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            DialogNumber dialogNumber = new DialogNumber((Button)view,
                    R.layout.dialog_number_picker,
                    new OnNumberSetListener((Button)view)
            );
            dialogNumber.setTitle(getResources().getString(R.string.quantidade_dezenas));
            dialogNumber.setStartValue(qtdeEdit);
            dialogNumber.setMinValue(qtdeMin);
            dialogNumber.setMaxValue(30);
            dialogNumber.show();
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
            String label = String.format(getResources().getString(R.string.qtde_dezenas),number);
            textView.setText(label);
            qtdeEdit = number;
            buildEdits(number);
        }
    }


    private class ConferirOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validateForm()) {
                int numero = Integer.valueOf(editTextNumeroConcurso.getText().toString());
                if (SorteioDAO.getDAO(ConfiraSeuJogoActivity.this, typeSorteio).findByNumber(Integer.valueOf(numero)) == null) {
                    new DialogBox(ConfiraSeuJogoActivity.this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.title_activity_confira_seu_jogo), "Concuros " + numero + " não encontrado").show();
                    return;
                }

                Intent intent = new Intent(ConfiraSeuJogoActivity.this, ResultadoSeuJogoActivity.class);
                intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
                intent.putExtra(Constantes.SEU_JOGO, getSeuJogoFromForm());

                startActivity(intent);

            } else {
                new DialogBox(ConfiraSeuJogoActivity.this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.msg_validade_form), fieldsValidate.toString()).show();
            }

        }
    }

    private void buildEdits(int qtdeEdit) {
        TableRow tableRow1 = (TableRow) findViewById(R.id.trow1);
        TableRow tableRow2 = (TableRow) findViewById(R.id.trow2);
        TableRow tableRow3 = (TableRow) findViewById(R.id.trow3);
        TableRow tableRow4 = (TableRow) findViewById(R.id.trow4);
        TableRow tableRow5 = (TableRow) findViewById(R.id.trow5);
        TableRow tableRow6 = (TableRow) findViewById(R.id.trow6);
        tableRow1.removeAllViews();
        tableRow2.removeAllViews();
        tableRow3.removeAllViews();
        tableRow4.removeAllViews();
        tableRow5.removeAllViews();
        tableRow6.removeAllViews();

        listaEditDezenas = new ArrayList<>();
        int col = 0;
        EditText editTextFocu = null;
        for (int i = 0; i < qtdeEdit; i++) {
            TableRow.LayoutParams tableRowLayoutParam = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tableRowLayoutParam.column = col;
            tableRowLayoutParam.span = 1;

            col++;
            if (col > 4 && qtdeEdit != 6) col = 0;
            EditText edtDezena = new EditText(this);
            edtDezena.setId(ViewIdGenerator.generateViewId());
            edtDezena.setLayoutParams(tableRowLayoutParam);
            edtDezena.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtDezena.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            if (editTextFocu != null) {
                editTextFocu.addTextChangedListener(new EditDezenaNextFocus(edtDezena));
            }
            editTextFocu = edtDezena;

            listaEditDezenas.add(edtDezena);


            if (i < 5) {
                tableRow1.addView(edtDezena);
            } else if ((i == 5) && (qtdeEdit == 6)) {
                tableRow1.addView(edtDezena);
            } else if (i >= 5 && i < 10) {
                tableRow2.addView(edtDezena);
            } else if (i >= 10 && i < 15) {
                tableRow3.addView(edtDezena);
            } else if (i >= 15 && i < 20) {
                tableRow4.addView(edtDezena);
            } else if (i >= 20 && i < 25) {
                tableRow5.addView(edtDezena);
            } else if (i >= 25 && i < 30) {
                tableRow6.addView(edtDezena);
            }

        }
    }


    private class EditDezenaNextFocus implements TextWatcher {

        private EditText nextEdit;

        private EditDezenaNextFocus(EditText nextEdit) {
            this.nextEdit = nextEdit;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 2) {
                this.nextEdit.requestFocus();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private SeuJogo getSeuJogoFromForm() {
        SeuJogo seuJogo = new SeuJogo();
        seuJogo.setNumeroConcurso(Integer.valueOf(editTextNumeroConcurso.getText().toString()));

        int[] arrayDezenas = new int[listaEditDezenas.size()];
        for (int i = 0; i < listaEditDezenas.size(); i++) {
            EditText edtDezena = listaEditDezenas.get(i);
            int dezena = Integer.valueOf(edtDezena.getText().toString());
            arrayDezenas[i] = dezena;
        }
        seuJogo.setDezenas(arrayDezenas);

        return seuJogo;
    }

    private boolean validateForm() {
        fieldsValidate = new ArrayList<>();
        String numero = editTextNumeroConcurso.getText().toString();

        if (numero.equals("")) {
            fieldsValidate.add("Concurso");
        }

        for (int i = 0; i < listaEditDezenas.size(); i++) {
            EditText edtDezena = listaEditDezenas.get(i);
            if (edtDezena.getText().toString().equals("")) {
                fieldsValidate.add("Dezena " + (i + 1));
            }
        }


        if (fieldsValidate.size() > 0) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        Rotina para caputar a leitura do código de barras(opcional, pois não retorna as dezenas da aposta)
         */
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Intent intent = new Intent(ConfiraSeuJogoActivity.this, OCRResultFormActivity.class);
                intent.putExtra(Constantes.TEXTO_OCR, result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        /*
        Caputura a foto e passa pro OCR
         */
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            OCRTask ocr = new OCRTask(ConfiraSeuJogoActivity.this);
            ocr.execute(bmp);
            try {
                setOCRInForm(ocr.get());
            } catch (InterruptedException | ExecutionException ignored) {
            }

        }

    }

    private void setOCRInForm(String textoOCR) {

        char[] sequencia = textoOCR.toCharArray();

        String novoTextoOCR = "";
        for (int i = 0; i < textoOCR.length(); i++) {
            if (((int) sequencia[i] != 10) && (sequencia[i] != ' ')) {
                novoTextoOCR += "" + sequencia[i];
            }
        }
        Intent intent = new Intent(ConfiraSeuJogoActivity.this, OCRResultFormActivity.class);
        intent.putExtra(Constantes.TEXTO_OCR, novoTextoOCR);
        startActivity(intent);

        /*
        String textoForSplit = "";
        char ch;
        for (int i = 0; i < novoTextoOCR.length(); i++) {
            ch = novoTextoOCR.charAt(i);
            textoForSplit += ch;
            if (i % 2 != 0) {
                textoForSplit += '-';
            }
        }


        EditText editTextOCR = (EditText) findViewById(R.id.editTextoOCR);
        editTextOCR.setText(textoForSplit);
        String[] arrayDezenasOCR = textoForSplit.split("-");
        for (int i = 0; i < arrayDezenasOCR.length; i++) {
            if (i < listaEditDezenas.size()) {
                EditText editText = listaEditDezenas.get(i);
                if (editText != null) {
                    editText.setText(arrayDezenasOCR[i]);
                }
            }
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confira_jogo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            case R.id.tirarFoto:
                try {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } catch (ActivityNotFoundException anfe) {
                    String errorMessage = "Seu dispositivo não suporta captura de imagens!";
                    Toast toast = Toast.makeText(ConfiraSeuJogoActivity.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;

            case R.id.lerCodigoBarras:
                IntentIntegrator integrator = new IntentIntegrator(ConfiraSeuJogoActivity.this);
                integrator.setPrompt("Posicione o leitor no código de barras");
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
