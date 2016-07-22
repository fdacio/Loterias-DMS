package br.com.daciosoftware.loteriasdms.processaarquivo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.filedialog.FileDialog;

public class ProcessaArquivoActivity extends AppCompatActivity {

    private EditText editTextArquivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processar_arquivo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View layout = (View) findViewById(R.id.layout_processar_arquivo);
        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this,layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);

        editTextArquivo = (EditText) findViewById(R.id.editTextArquivo);
        ImageButton imageButtonArquivo = (ImageButton) findViewById(R.id.imageButtonArquivo);
        Button buttonProcessarArquivo = (Button) findViewById(R.id.buttonProcessarArquivo);
        TextView textViewLinkBaixar = (TextView) findViewById(R.id.textViewLinkBaixar);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView textViewAguarde = (TextView) findViewById(R.id.textViewAguarde);


        imageButtonArquivo.setOnClickListener(new OnClickListenerButtonArquivo());
        buttonProcessarArquivo.setOnClickListener(new OnClickListenerButtonProcessarArquivo());

        textViewLinkBaixar.setText(Html.fromHtml("<a href="+getLinkBaixar(typeSorteio)+">"+getResources().getString(R.string.label_baixar_arquivo)+"</a> "));
        textViewLinkBaixar.setMovementMethod(LinkMovementMethod.getInstance());

        progressBar.setVisibility(View.INVISIBLE);
        textViewAguarde.setVisibility(View.INVISIBLE);


    }

    private class OnClickListenerButtonArquivo implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            FileDialog fileDialog = new FileDialog(ProcessaArquivoActivity.this, FileDialog.FileDialogType.OPEN_FILE);
            fileDialog.setFormaterFilter(new String[]{"html,htm"});
            fileDialog.show();
        }
    }

    private class OnClickListenerButtonProcessarArquivo implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String filePath = editTextArquivo.getText().toString();
            if(filePath.equals("")){
                new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, "Error", getResources().getString(R.string.error_informe_arquivo)).show();
                return;
            }

            File file = new File(filePath);
            if(!file.exists()){
                new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, "Error", getResources().getString(R.string.error_arquivo_invalido)).show();
                return;
            }

            //Aqui executar AsyncTask para processar o arquivo
        }
    }

    private String getLinkBaixar(TypeSorteio typeSorteio){
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.PREF_SHARED, MODE_PRIVATE);

        switch (typeSorteio){
            case MEGASENA:
                return sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA_DEFAULT);

            case LOTOFACIL:
                return sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL, Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL_DEFAULT);

            case QUINA:
                return sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_QUINA, Constantes.URL_ARQUIVO_RESULTADOS_QUINA_DEFAULT);

            default: return "www.cef.gov.br/loterias";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == FileDialog.FILE_DIALOG && requestCode == RESULT_OK){
            String filePath = intent.getStringExtra(FileDialog.RESULT_PATH);
            editTextArquivo.setText(filePath);
        }
    }

}
