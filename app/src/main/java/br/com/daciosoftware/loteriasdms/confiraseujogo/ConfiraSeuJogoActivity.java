package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.ViewIdGenerator;

public class ConfiraSeuJogoActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 200;
    private static final int CROP_FROM_CAMERA = 201;
    private EditText editTextResult;
    private TypeSorteio typeSorteio;
    private List<EditText> listaEditDezenas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confira_seu_jogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View layout = findViewById(R.id.layout_confira_seu_jogo);
        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this, layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);

        EditText edtNumeroConcurso = (EditText) findViewById(R.id.editTextNumeroConcurso);
        Button buttonConferir = (Button) findViewById(R.id.buttonConferir);
        styleTypeSorteio.setStyleButton(typeSorteio);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        styleTypeSorteio.setStyleFloatingActionButton(typeSorteio);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
        }

        buildEdits();
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

            if (i < 5) {
                tableRow1.addView(edtDezena);
            } else if ((i == 5) && (qtdeEdit == 6)) {
                tableRow1.addView(edtDezena);
            } else if (i >= 5 && i < 10) {
                tableRow2.addView(edtDezena);
            } else {
                tableRow3.addView(edtDezena);
            }

        }
    }

    private class OnClickListenerConferir implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //Arqui executar a conferencia, passando o numero do
            //concurso e as dezenas para outra Activity que
            //conterá uma listView com os resultados

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri bmpUri;

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                editTextResult.setText(result.getContents());
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }


        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            //ImageView imageViewCroped = (ImageView) findViewById(R.id.imageViewCroped);
            //imageViewCroped.setImageBitmap(photo);

            OCRTask ocr = new OCRTask(ConfiraSeuJogoActivity.this);
            ocr.execute(photo);
            try {
                editTextResult.setText(ocr.get());
            } catch (InterruptedException | ExecutionException ignored) {
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
