package br.com.daciosoftware.loteriasdms.processaarquivo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.HttpDownload;
import br.com.daciosoftware.loteriasdms.util.filedialog.FileDialog;

public class ProcessaArquivoActivity extends AppCompatActivity {

    private EditText editTextArquivo;
    private TypeSorteio typeSorteio;
    private boolean isSelecionarArquivo = false;
    private boolean isBaixarArquivo = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processar_arquivo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View layout = (View) findViewById(R.id.layout_processar_arquivo);
        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this, layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);

        editTextArquivo = (EditText) findViewById(R.id.editTextArquivo);
        ImageButton imageButtonArquivo = (ImageButton) findViewById(R.id.imageButtonArquivo);
        Button buttonProcessarArquivo = (Button) findViewById(R.id.buttonProcessarArquivo);
        TextView textViewLinkBaixar = (TextView) findViewById(R.id.textViewLinkBaixar);

        imageButtonArquivo.setOnClickListener(new OnClickListenerButtonArquivo());
        buttonProcessarArquivo.setOnClickListener(new OnClickListenerButtonProcessarArquivo());
        textViewLinkBaixar.setOnClickListener(new OnClickListenerBaixaArquivo());

    }

    private class OnClickListenerButtonArquivo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            isSelecionarArquivo = true;
            FileDialog fileDialog = new FileDialog(ProcessaArquivoActivity.this, FileDialog.FileDialogType.OPEN_FILE);
            fileDialog.setFormaterFilter(new String[]{"html", "htm"});
            fileDialog.show();
        }
    }

    private class OnClickListenerBaixaArquivo implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            isBaixarArquivo = true;
            FileDialog fileDialog = new FileDialog(ProcessaArquivoActivity.this, FileDialog.FileDialogType.SELECT_DIR);
            fileDialog.show();

        }
    }

    /**
     * Processamento do Arquivo
     */
    private class OnClickListenerButtonProcessarArquivo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String filePath = editTextArquivo.getText().toString();
            if (filePath.equals("")) {
                new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, "Error", getResources().getString(R.string.error_informe_arquivo)).show();
                return;
            }

            File file = new File(filePath);
            if (!file.exists()) {
                new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, "Error", getResources().getString(R.string.error_arquivo_invalido)).show();
                return;
            }

            //Aqui executar AsyncTask para processar o arquivo
        }
    }

    private String getLinkBaixar(TypeSorteio typeSorteio) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREF, MODE_PRIVATE);

        switch (typeSorteio) {
            case MEGASENA:
                return sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA_DEFAULT);

            case LOTOFACIL:
                return sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL, Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL_DEFAULT);

            case QUINA:
                return sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_QUINA, Constantes.URL_ARQUIVO_RESULTADOS_QUINA_DEFAULT);

            default:
                return "www.cef.gov.br/loterias";
        }
    }

    private String getNomeArquivoZip(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return "megasena.zip";

            case LOTOFACIL:
                return "lotofacio.zip";

            case QUINA:
                return "quina.zip";

            default:
                return "arquivo.zip";
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
        if (requestCode == FileDialog.FILE_DIALOG && resultCode == RESULT_OK) {

            if (isSelecionarArquivo) {
                String filePath = intent.getStringExtra(FileDialog.RESULT_PATH);
                editTextArquivo.setText(filePath);
                isSelecionarArquivo = false;
            }

            if (isBaixarArquivo) {
                String outFile = intent.getStringExtra(FileDialog.RESULT_PATH) + "/" + getNomeArquivoZip(typeSorteio);
                isBaixarArquivo = false;
                new BaixarArquivoZipTask().execute(getLinkBaixar(typeSorteio), outFile);
            }

        }

    }


    private class BaixarArquivoZipTask extends AsyncTask<String, Integer, String> {

        private boolean running = true;
        private Integer progress;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String url = params[0];
                String outFile = params[1];
                HttpDownload httpDownload = new HttpDownload();
                try {
                    httpDownload.downloadFile(url, outFile, progress);
                    publishProgress(progress);
                    return "Download concluído com sucesso";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Erro ao baixar o arquivo(" + url + ")-" + outFile + ": " + e.getMessage();
                }
            }
            return "Download cancelado";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ProcessaArquivoActivity.this,
                    "Baixando Arquivo",
                    "Aguarde...("+progress+"%)",
                    false,
                    true
            );
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
        }


        @Override
        protected void onPostExecute(String retorno) {
            progressDialog.dismiss();
            new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, "Baixar Arquivo", retorno).show();
        }

        @Override
        protected void onCancelled() {
            running = false;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
        }

    }


}
