package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.DialogNumber;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;
import br.com.daciosoftware.loteriasdms.util.MyFileUtil;
import br.com.daciosoftware.loteriasdms.util.ViewIdGenerator;

public class ConfiraSeuJogoActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 200;
    private static final int CROP_FROM_CAMERA = 201;
    private TypeSorteio typeSorteio;
    private List<String> fieldsValidate;
    private EditText editTextNumeroConcurso;
    private List<EditText> listaEditDezenas;
    private Uri uriSavedImage;
    private int qtdeEdit;


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

        ImageButton imageButtonDigitalizar = (ImageButton) findViewById(R.id.imageButtonDigitalizar);
        imageButtonDigitalizar.setOnClickListener(new DigitalizarOnClickListener());

        Button buttonConferir = (Button) findViewById(R.id.buttonConferir);
        buttonConferir.setOnClickListener(new ConferirOnClickListener());


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
        buttonNumberPiker.setText(String.valueOf(qtdeEdit) + " dezenas");
        buildEdits(qtdeEdit);

        new StyleOfActivity(this, findViewById(R.id.layout_confira_seu_jogo)).setStyleInViews(typeSorteio);
    }

    private class DialogNumberPickerOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DialogNumber dialogNumber = new DialogNumber(ConfiraSeuJogoActivity.this);
            dialogNumber.setOnValueChangeListener(new DialogNumberPickerOnValueChangeListener((Button) view));
            dialogNumber.setTitle("Quantidade de Dezenas");
            dialogNumber.setStartValue(qtdeEdit);
            dialogNumber.setMinValue(5);
            dialogNumber.setMaxValue(30);
            dialogNumber.show();
        }
    }

    private class DialogNumberPickerOnValueChangeListener implements NumberPicker.OnValueChangeListener {
        private Button button;
        public DialogNumberPickerOnValueChangeListener(Button button) {
            this.button = button;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            this.button.setText(String.valueOf(newVal) + " dezenas");
            qtdeEdit = newVal;
            buildEdits(newVal);
        }
    }

    private class ConferirOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validateForm()) {
                Intent intent = new Intent(ConfiraSeuJogoActivity.this, ResultadoSeuJogoActivity.class);
                intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
                try {
                    intent.putExtra(Constantes.SEU_JOGO, getSeuJogoFromForm());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                startActivity(intent);

            } else {
                new DialogBox(ConfiraSeuJogoActivity.this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.msg_validade_form), fieldsValidate.toString()).show();
            }

        }
    }

    private class DigitalizarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(MyFileUtil.getDefaultDirectoryApp(), "images");
                imagesFolder.mkdirs();
                MyFileUtil.removeFilesInDirectory(imagesFolder);
                File image = new File(imagesFolder, "image_" + MyDateUtil.timeToString() + ".jpg");
                uriSavedImage = Uri.fromFile(image);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            } catch (ActivityNotFoundException anfe) {
                //display an error message
                String errorMessage = "Seu dispositivo não suporta captura de imagens!";
                Toast toast = Toast.makeText(ConfiraSeuJogoActivity.this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
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
        for (int i = 0; i < qtdeEdit; i++) {
            TableRow.LayoutParams tableRowLayoutParam = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    col);
            col++;
            if(col > 4 && qtdeEdit != 6) col = 0;
            EditText edtDezena = new EditText(this);
            edtDezena.setId(ViewIdGenerator.generateViewId());
            edtDezena.setLayoutParams(tableRowLayoutParam);
            edtDezena.setInputType(InputType.TYPE_CLASS_NUMBER);
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


    private Sorteio getSeuJogoFromForm() throws ParseException {
        Sorteio seuJogo = SorteioDAO.getDAO(this, typeSorteio).getInstanciaEntity();

        seuJogo.setNumero(Integer.parseInt(editTextNumeroConcurso.getText().toString()));

        java.lang.reflect.Method methodSet = null;
        for (int i = 0; i < listaEditDezenas.size(); i++) {
            EditText edtDezena = listaEditDezenas.get(i);
            int dezena = Integer.parseInt(edtDezena.getText().toString());
            String methodName = "setD" + String.valueOf(i + 1);

            try {
                Class clazz = seuJogo.getClass().getSuperclass();
                methodSet = clazz.getMethod(methodName, Integer.TYPE);
            } catch (SecurityException | NoSuchMethodException e) {
            }

            if (methodSet != null) {
                try {
                    methodSet.invoke(seuJogo, dezena);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                }
            }
        }
        return seuJogo;
    }

    private boolean validateForm() {
        fieldsValidate = new ArrayList<>();
        String numero = editTextNumeroConcurso.getText().toString();

        if (numero.equals("")) {
            fieldsValidate.add("Número");
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
    public void onResume(){
        super.onResume();
        buildEdits(qtdeEdit);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        Rotina para caputar a leitura do código de barras(opcional, pois não retorna as dezenas da aposta)
         */
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                setOCRInForm(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        /*
        Caputura a foto e passa para o crop
         */
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            //Uri imageUri = (Uri) data.getExtras().get("data");

            performCrop();
        }


        /*
        Apos o crop feito, passa a imagem para o processo de OCR
         */
        if (requestCode == CROP_FROM_CAMERA && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap bmpCrop = extras.getParcelable("data");
            OCRTask ocr = new OCRTask(ConfiraSeuJogoActivity.this);
            ocr.execute(bmpCrop);
            try {
                setOCRInForm(ocr.get());
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }

    }

    private void performCrop() {

        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uriSavedImage, "image/*");
            cropIntent.putExtra("crop", "true");
            //valores zeros permite crop de forma livre
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_FROM_CAMERA);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Seu dispositivo não suporta ajuste imagens!";
            Toast toast = Toast.makeText(ConfiraSeuJogoActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
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
