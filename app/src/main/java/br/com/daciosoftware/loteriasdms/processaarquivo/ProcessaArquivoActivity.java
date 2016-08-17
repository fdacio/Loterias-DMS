package br.com.daciosoftware.loteriasdms.processaarquivo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DecompressFile;
import br.com.daciosoftware.loteriasdms.util.DeviceInformation;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.DownloadFile;
import br.com.daciosoftware.loteriasdms.util.LogProcessamento;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;
import br.com.daciosoftware.loteriasdms.util.MyFileUtil;
import br.com.daciosoftware.loteriasdms.util.filedialog.FileDialog;

public class ProcessaArquivoActivity extends AppCompatActivity {

    private TextView textViewLabelFile;
    private EditText editTextArquivo;
    private TypeSorteio typeSorteio;
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


        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);

        textViewLabelFile = (TextView) findViewById(R.id.textViewLabelFile);
        editTextArquivo = (EditText) findViewById(R.id.editTextArquivo);
        ImageButton imageButtonArquivo = (ImageButton) findViewById(R.id.imageButtonArquivo);
        Button buttonProcessarArquivo = (Button) findViewById(R.id.buttonProcessarArquivo);
        Button buttonBaixarArquivo = (Button) findViewById(R.id.buttonBaixarArquivo);


        if (imageButtonArquivo != null) {
            imageButtonArquivo.setOnClickListener(new OnClickListenerButtonArquivo());
        }
        if (buttonProcessarArquivo != null) {
            buttonProcessarArquivo.setOnClickListener(new OnClickListenerButtonProcessarArquivo());
        }
        if (buttonBaixarArquivo != null) {
            buttonBaixarArquivo.setOnClickListener(new OnClickListenerBaixaArquivo());
        }


        new StyleOfActivity(this, findViewById(R.id.layout_processar_arquivo)).setStyleInViews(typeSorteio);
    }

    /**
     * Subclasse privada que implementar o OnClickListener do Botao com lupa.
     * Esse botão abri um FileDialog para seleção de um arquivo HTML para
     * o processamento dos sorteios
     */
    private class OnClickListenerButtonArquivo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FileDialog fileDialog = new FileDialog(ProcessaArquivoActivity.this, FileDialog.FileDialogType.OPEN_FILE);
            fileDialog.setStartPath(MyFileUtil.getDefaultDirectoryApp());
            fileDialog.setFormaterFilter(new String[]{"html", "htm"});
            fileDialog.show();
        }
    }


    private class OnClickListenerBaixaArquivo implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (DeviceInformation.isNetwork(ProcessaArquivoActivity.this)) {
                String defaulDirectory = MyFileUtil.getDefaultDirectoryApp();
                String outFile = defaulDirectory + "/" + getNomeArquivoZip(typeSorteio);
                downloadFile = new DownloadFile();
                decompressFile = new DecompressFile();
                new BaixarArquivoZipTask().execute(getLinkBaixar(typeSorteio), outFile);
            } else {
                new DialogBox(ProcessaArquivoActivity.this,
                        DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.error),
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

            String filePath = editTextArquivo.getText().toString();
            if (filePath.equals("")) {
                new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.error), getResources().getString(R.string.error_informe_arquivo)).show();
                return;
            }

            File file = new File(filePath);
            if (!file.exists()) {
                new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.error), getResources().getString(R.string.error_arquivo_invalido)).show();
                return;
            }

            new ProcessarArquivoHtml().execute(filePath);
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
        String time = MyDateUtil.timeToString();
        switch (typeSorteio) {
            case MEGASENA:
                return "megasena_" + time + ".zip";

            case LOTOFACIL:
                return "lotofacil_" + time + ".zip";

            case QUINA:
                return "quina_" + time + ".zip";

            default:
                return "arquivo_" + time + ".zip";
        }
    }

    private String getNomeArquivoHtml(TypeSorteio typeSorteio) {
        String time = MyDateUtil.timeToString();
        switch (typeSorteio) {
            case MEGASENA:
                return "megasena_" + time + ".htm";

            case LOTOFACIL:
                return "lotofacil_" + time + ".htm";

            case QUINA:
                return "quina_" + time + ".htm";

            default:
                return "arquivo_" + time + ".htm";
        }
    }

    private String getTituloArquivoHtml(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return "mega-sena";

            case LOTOFACIL:
                return "lotofácil";

            case QUINA:
                return "quina";

            default:
                return "titulo";
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

    private void setSizeFileInLabel(String filePath){
        float fileSize = MyFileUtil.getSizeMBytes(new File(filePath));
        String label = getResources().getString(R.string.label_informe_arquivo);
        textViewLabelFile.setText(String.format(label + "(%.2f MB)", fileSize));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FileDialog.FILE_DIALOG && resultCode == RESULT_OK) {
            String filePath = intent.getStringExtra(FileDialog.RESULT_PATH);
            setSizeFileInLabel(filePath);
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

        private String pathFileHtml;

        @Override
        protected String doInBackground(String... params) {

            String url = params[0];
            String outFile = params[1];
            try {
                downloadFile.downloadFileBinary(url, outFile);
            } catch (IOException e) {
                return getResources().getString(R.string.erro_baixar_arquivo) + e.getMessage();
            }

            try {
                publishProgress(getResources().getString(R.string.descomprimindo));
                String outFileHtml = new File(outFile).getParent() + "/" + getNomeArquivoHtml(typeSorteio);
                pathFileHtml = decompressFile.unzip(outFile, outFileHtml);

            } catch (IOException e2) {
                return getResources().getString(R.string.erro_descomprimir) + e2.getMessage();
            }


            if (isCancelled()) {
                return getResources().getString(R.string.processo_cancelado);
            }

            return getResources().getString(R.string.processo_concluido);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProcessaArquivoActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.baixando));
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new cancelTaskBaixarArquivoZip(this));
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(String retorno) {
            progressDialog.dismiss();
            setSizeFileInLabel(pathFileHtml);
            editTextArquivo.setText(pathFileHtml);
            try {
                downloadFile.closeResources();
                decompressFile.closeResources();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.baixar_arquivo), retorno).show();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

        @Override
        protected void onProgressUpdate(String... msgs) {
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
    private class cancelTaskBaixarArquivoZip implements DialogInterface.OnCancelListener {

        private AsyncTask task;

        public cancelTaskBaixarArquivoZip(AsyncTask task) {
            this.task = task;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            new DialogBox(ProcessaArquivoActivity.this,
                    DialogBox.DialogBoxType.QUESTION,
                    getResources().getString(R.string.processar_arquivo),
                    getResources().getString(R.string.deseja_cancelar_processo),
                    new DialogInterface.OnClickListener() {//Resposta SIM do DialogBox Question
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                downloadFile.closeResources();
                                decompressFile.closeResources();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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

    private boolean isRowValid(String col) {
        try {
            Integer.parseInt(col);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private class ProcessarArquivoHtml extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String pathFileHtml = params[0];
            SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);

            try {

                MyHtmlParse doc = MyHtmlParse.getInstance(pathFileHtml);

                String titleDoc = doc.getTitleHtml();
                String titleJogo = getTituloArquivoHtml(typeSorteio);
                if (!titleDoc.toLowerCase().contains(titleJogo.substring(0, 4))) {
                    return getResources().getString(R.string.error_arquivo_outro_jogo) + titleDoc;
                }

                String msg = getResources().getString(R.string.processando_arquivo_concurso);

                List<String> trows = doc.getTrowsTable();
                trows.remove(0);
                for (String trow : trows) {
                    List<String> tds = MyHtmlParse.getTdsInTrow(trow);
                    String valueTd = MyHtmlParse.getTextTag(tds.get(0));
                    if (isRowValid(valueTd)) {
                        try {
                            publishProgress(String.format(msg,Integer.parseInt(valueTd)));
                            if (sorteioDAO != null) {
                                sorteioDAO.insertSorteioFromTrow(tds);
                            }
                        } catch (ParseException e) {
                            LogProcessamento.registraLog(trow + ":" + e.getMessage());
                        }

                        if (isCancelled()) {
                            return getResources().getString(R.string.processo_cancelado);
                        }
                    }
                }

            } catch (NumberFormatException | IOException | OutOfMemoryError e) {
                return getResources().getString(R.string.erro_processar_arquivo) + e.getMessage();
            }
            return getResources().getString(R.string.processo_concluido);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProcessaArquivoActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.processando));
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new cancelTaskProcessamentoHtml(this));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String retorno) {
            progressDialog.dismiss();
            textViewLabelFile.setText(getResources().getString(R.string.label_informe_arquivo));
            editTextArquivo.setText("");
            new DialogBox(ProcessaArquivoActivity.this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.processar_arquivo), retorno).show();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(String... msgs) {
            String msg = msgs[0];
            progressDialog.setMessage(msg);
        }
    }


    private class cancelTaskProcessamentoHtml implements DialogInterface.OnCancelListener {

        private AsyncTask task;

        public cancelTaskProcessamentoHtml(AsyncTask task) {
            this.task = task;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            new DialogBox(ProcessaArquivoActivity.this,
                    DialogBox.DialogBoxType.QUESTION,
                    getResources().getString(R.string.processar_arquivo),
                    getResources().getString(R.string.deseja_cancelar_processo),
                    new DialogInterface.OnClickListener() {//Resposta SIM do DialogBox Question
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
