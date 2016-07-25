package br.com.daciosoftware.loteriasdms.processaarquivo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DateUtil;
import br.com.daciosoftware.loteriasdms.util.DecompressFile;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.DownloadFile;
import br.com.daciosoftware.loteriasdms.util.filedialog.FileDialog;

public class ProcessaArquivoActivity extends AppCompatActivity {

    private EditText editTextArquivo;
    private TypeSorteio typeSorteio;
    private boolean isSelecionarArquivo = false;
    private boolean isBaixarArquivo = false;
    private ProgressDialog progressDialog;
    private DownloadFile downloadFile;
    private DecompressFile decompressFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processar_arquivo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View layout = findViewById(R.id.layout_processar_arquivo);
        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this, layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);

        editTextArquivo = (EditText) findViewById(R.id.editTextArquivo);
        ImageButton imageButtonArquivo = (ImageButton) findViewById(R.id.imageButtonArquivo);
        Button buttonProcessarArquivo = (Button) findViewById(R.id.buttonProcessarArquivo);
        Button buttonBaixarArquivo = (Button) findViewById(R.id.buttonBaixarArquivo);
        styleTypeSorteio.setStyleButton(typeSorteio);

        imageButtonArquivo.setOnClickListener(new OnClickListenerButtonArquivo());
        buttonProcessarArquivo.setOnClickListener(new OnClickListenerButtonProcessarArquivo());
        buttonBaixarArquivo.setOnClickListener(new OnClickListenerBaixaArquivo());

    }

    /**
     * Subclasse privada que implementar o OnClickListener do Botao com lupa.
     * Esse botão abri um FileDialog para seleção de um arquivo HTML para
     * o processamento dos sorteios
     *
     */
    private class OnClickListenerButtonArquivo implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            v.startAnimation(buttonClick);

            FileDialog fileDialog = new FileDialog(ProcessaArquivoActivity.this, FileDialog.FileDialogType.OPEN_FILE);
            fileDialog.setFormaterFilter(new String[]{"html", "htm"});
            fileDialog.show();
        }
    }


    private class OnClickListenerBaixaArquivo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            v.startAnimation(buttonClick);

            if (isNetwork()) {
                String defaulDirectory = Environment.getExternalStorageDirectory().getPath() + "/LoteriasDMS";
                File dir = new File(defaulDirectory);
                if(!dir.exists()){
                    dir.mkdir();
                }
                String outFile = defaulDirectory+"/" + getNomeArquivoZip(typeSorteio);
                downloadFile = new DownloadFile();
                decompressFile = new DecompressFile();
                new BaixarArquivoZipTask().execute(getLinkBaixar(typeSorteio), outFile);
            } else {
                new DialogBox(ProcessaArquivoActivity.this,
                        DialogBox.DialogBoxType.INFORMATION, "Error",
                        getResources().getString(R.string.error_conexao)
                ).show();
            }
        }
    }

    /**
     * Processamento do Arquivo
     */
    private class OnClickListenerButtonProcessarArquivo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            v.startAnimation(buttonClick);

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

    /*
    Verifiaca o status da conexão
     */
    private boolean isNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
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
        String time = DateUtil.timeToString(Calendar.getInstance());
        switch (typeSorteio) {
            case MEGASENA:
                return "megasena_"+time+".zip";

            case LOTOFACIL:
                return "lotofacil_"+time+".zip";

            case QUINA:
                return "quina_"+time+".zip";

            default:
                return "arquivo_"+time+".zip";
        }
    }

    private String getNomeArquivoHtml(TypeSorteio typeSorteio) {
        String time = DateUtil.timeToString(Calendar.getInstance());
        switch (typeSorteio) {
            case MEGASENA:
                return "megasena_"+time+".htm";

            case LOTOFACIL:
                return "lotofacil_"+time+".htm";

            case QUINA:
                return "quina_"+time+".htm";

            default:
                return "arquivo_"+time+".htm";
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
            String filePath = intent.getStringExtra(FileDialog.RESULT_PATH);
            editTextArquivo.setText(filePath);
        }
    }


    /**
     * Subclasse privada que implementa o processo de baixa e descopressão do
     * arquivo com os resultados dos sorteios.
     * Essa subclasse extende de AsyncTask para realiazar o processo
     * em um outra Thread que não seja a Main Thread
     */
    private class BaixarArquivoZipTask extends AsyncTask<String, String, String> {
        private boolean running = true;
        private String pathFileHtml;

        @Override
        protected String doInBackground(String... params) {
            while (running) {

                String url = params[0];
                String outFile = params[1];
                try {
                    downloadFile.downloadFileBinary(url, outFile);
                }catch (IOException e) {
                    e.printStackTrace();
                    return "Erro ao baixar o arquivo: " + e.getMessage();
                }


                try{
                    publishProgress("Descomprimindo arquivo. Aguarde...");
                    String inFileDecompress = outFile;
                    String outFileHtml = new File(outFile).getParent() + "/"+ getNomeArquivoHtml(typeSorteio);
                    pathFileHtml = decompressFile.unzip(inFileDecompress, outFileHtml);

                } catch (IOException e2) {
                    e2.printStackTrace();
                    return "Erro ao descomprimir o aquivo:" + e2.getMessage();
                }


                if(isCancelled()) {
                    break;
                }

                return "Processo concluído com sucesso.";

            }
            return "Processo cancelado.";


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProcessaArquivoActivity.this);
            progressDialog.setMessage("Baixando arquivo. Aguarde...");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new cancelTask(this));
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(String retorno) {
            progressDialog.dismiss();
            editTextArquivo.setText(pathFileHtml);
            new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, "Baixar Arquivo", retorno).show();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(ProcessaArquivoActivity.this, "Processo cancelado!", Toast.LENGTH_SHORT).show();
            running = false;
        }

        @Override
        protected void onProgressUpdate(String... msgs){
            String newMsg = msgs[0];
            progressDialog.setMessage(newMsg);


        }

    }


    /**
     * Subclasse privada que implementa o cancelamento do processo de download
     * e descompressão do arquivo
     * Ela e instanciada no onCancelListener do progressDialog e recebe no
     * construtor a AsyncTask a ser cancelada, no caso de BaixarArquivoZipTask
     */
    private class cancelTask implements DialogInterface.OnCancelListener{

        private AsyncTask task;
        public cancelTask(AsyncTask task){
            this.task = task;
        }
        @Override
        public void onCancel(DialogInterface dialog) {
            new DialogBox(ProcessaArquivoActivity.this,
                    DialogBox.DialogBoxType.QUESTION,
                    "Processar Arquivo",
                    "Deseja cancelar o processo?",
                    new DialogInterface.OnClickListener() {//Resposta SIM do DialogBox Question
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                downloadFile.closeResources();
                                decompressFile.closeResources();
                            }catch (IOException e){e.printStackTrace();}

                            task.cancel(true);
                        }
                    },
                    new DialogInterface.OnClickListener() {//Resposta NÃO do DialogBox Question
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            progressDialog.show();
                        }
                    }

            ).show();

        }
    }
}
