package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleOfActivity;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.DialogNumber;
import br.com.daciosoftware.loteriasdms.util.MyFileUtil;
import br.com.daciosoftware.loteriasdms.util.NumberPickerDialog;

public class JogosGeradosActivity extends AppCompatActivity {

    private ListView listViewGeraJogos;
    private TextView textViewTotalJogos;
    private int[] dezenasSelecionadas;
    private int qtdeDezenasPorJogo;
    private int qtdeMinimaDezenaPorJogo;
    private int qtdeMaximaDezenaPorJogo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos_gerados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
        dezenasSelecionadas = getIntent().getIntArrayExtra(Constantes.DEZENAS_SELECIONADAS);
        qtdeMinimaDezenaPorJogo = getIntent().getIntExtra(Constantes.QTDE_MIN_DEZENAS_JOGO, 0);
        qtdeMaximaDezenaPorJogo = getIntent().getIntExtra(Constantes.QTDE_MAX_DEZENAS_JOGO, 0);
        qtdeDezenasPorJogo = qtdeMinimaDezenaPorJogo;
        Arrays.sort(dezenasSelecionadas);

        Button buttonDezenasPorJogo = (Button) findViewById(R.id.buttonDezenasPorJogo);
        if (buttonDezenasPorJogo != null) {
            buttonDezenasPorJogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogNumber dialogNumber = new DialogNumber((Button) view,
                            R.layout.dialog_number_picker,
                            new OnNumberSetListener((Button) view)
                    );

                    dialogNumber.setTitle(getResources().getString(R.string.label_qtde_dezenas_por_jogo));
                    dialogNumber.setStartValue(qtdeDezenasPorJogo);
                    dialogNumber.setMinValue(qtdeMinimaDezenaPorJogo);
                    dialogNumber.setMaxValue(qtdeMaximaDezenaPorJogo);
                    dialogNumber.show();

                }
            });
            String label = String.format(getResources().getString(R.string.label_dezenas_por_jogo), qtdeDezenasPorJogo);
            buttonDezenasPorJogo.setText(label);
        }

        /*
        * Instancia a listview para exibir os jogos gerados
         */
        listViewGeraJogos = (ListView) findViewById(R.id.listViewGeraJogos);
        textViewTotalJogos = (TextView) findViewById(R.id.textViewTotalJogos);
        String label = getResources().getString(R.string.label_total_jogos);
        textViewTotalJogos.setText(String.format(label, 0));

        listarJogosGerados(qtdeDezenasPorJogo);

        new StyleOfActivity(this, findViewById(R.id.layout_activity_gera_jogos)).setStyleInViews(typeSorteio);

    }

    private void listarJogosGerados(int qtdeDezenasPorJogo) {
        new GeraJogosTask().execute(qtdeDezenasPorJogo);

    }

    /*
    Método para obter o fatorial
    */
    private long getFatorial(long n) {
        if (n <= 1) {
            return 1;
        } else {
            return getFatorial(n - 1) * n;
        }
    }

    /**
     * Método que retorna a quantidade de combinações de universo em classe de qtdeDezenas;
     * Fomula matemática da Combinação(não é arranjo)
     *
     * @param qtdeDezenas - qtdeDezenas
     * @param universo    - universo
     * @return total possivel de combinações
     */

    private long getQtdeJogosPossiveis(int qtdeDezenas, int universo) {
        return getFatorial(universo) / (getFatorial(qtdeDezenas) * getFatorial(universo - qtdeDezenas));
    }

    /**
     * Verifca se um jogo está ou não na lista
     *
     * @param list       lista a comparar
     * @param jogoGerado jogo a ser comparado
     * @return true se estiverna lista, false se não estiver na lista
     */
    private boolean inList(List<JogoGerado> list, JogoGerado jogoGerado) {
        for (JogoGerado aJogoGerado : list) {
            if (aJogoGerado.equals(jogoGerado)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se uma dezena está ou não no array
     *
     * @param dezenas array a comparar
     * @param dezena  dezena a ser comparada
     * @return tue se estiver no array, false se não estiver
     */
    private boolean inArray(int[] dezenas, int dezena) {
        for (int nDezena : dezenas) {
            if (nDezena == dezena) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtém um indice radômico dentro de um limite
     * Se o limite for menor que 7 diminui o limite é
     * diminuido(bound/2) para evitar repetições
     *
     * @param bound limite a ser gerado o indice
     * @param loop  repeticao de busca do indice
     * @return indice dentro do limite
     */
    private int getIndiceRandomico(int bound, int loop) {
        Random random = new Random();
        if (bound > 7) {
            return random.nextInt(bound);
        } else {
            int boundMax = (bound / 2) + (bound % 2);
            int boundMin = bound - boundMax;
            if (loop % 2 == 0) {
                return random.nextInt(boundMax);
            } else {
                return random.nextInt(boundMin) + boundMax;
            }
        }
    }

    /**
     * Obtém um indice radômico dentro de um limite(bound)
     *
     * @param bound limite a ser gerado o indice
     * @return indice dentro do limite
     */
    private int getIndiceRandomico(int bound) {
        return new Random().nextInt(bound);
    }

    /**
     * Monta um array candidato de dezenas para
     * compor a lista de jogos
     *
     * @param qtdeDezenasPorJogo - quantidade de dezenas do array a ser montado
     * @return array montado
     */
    private int[] getArrayCandidato(int qtdeDezenasPorJogo) {
        int[] arrayDezenasJogoGerado = new int[qtdeDezenasPorJogo];
        int indiceRandomico;
        int dezena;
        int i = 0;
        while (i < qtdeDezenasPorJogo) {
            indiceRandomico = getIndiceRandomico(dezenasSelecionadas.length);
            dezena = dezenasSelecionadas[indiceRandomico];
            if (!inArray(arrayDezenasJogoGerado, dezena)) {
                arrayDezenasJogoGerado[i] = dezena;
                i++;
            }
        }

        Arrays.sort(arrayDezenasJogoGerado);

        return arrayDezenasJogoGerado;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gera_jogo, menu);
        return true;
    }

    private List<JogoGerado> getLisJogoGerado() {
        return ((JogosGeradosAdapter) listViewGeraJogos.getAdapter()).getList();
    }

    private String geraFileJogos(List<JogoGerado> listJogoGerado) throws IOException {
        String filePath = MyFileUtil.getDefaultDirectoryApp() + "/jogos-gerados.txt";
        File fileJogosGerado = new File(filePath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileJogosGerado));
        writer.write("###### JOGOS GERADOS ########\n");
        for (JogoGerado jogoGerado : listJogoGerado) {
            writer.write(jogoGerado.toString());
            writer.write("\n");
        }
        writer.close();
        return filePath;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.compartilhar:
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                try {
                    String myFilePath = geraFileJogos(getLisJogoGerado());
                    File fileWithinMyDir = new File(myFilePath);
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setType("text/plain");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + myFilePath));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Jogos Gerados");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Jogos Gerados");
                        startActivity(Intent.createChooser(intentShareFile, "Compartilhar Arquivo"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private class GeraJogosTask extends AsyncTask<Integer, String, Void> {

        private List<JogoGerado> listJogoGerado = new ArrayList<>();

        @Override
        protected Void doInBackground(Integer... params) {

            int qtdeDezenasPorJogo = params[0];
            long qtdeJogosPossiveis = getQtdeJogosPossiveis(qtdeDezenasPorJogo, dezenasSelecionadas.length);

            while (listJogoGerado.size() < qtdeJogosPossiveis) {
                JogoGerado jogoGerado = new JogoGerado(getArrayCandidato(qtdeDezenasPorJogo));
                if (!inList(listJogoGerado, jogoGerado)) {
                    listJogoGerado.add(jogoGerado);
                    String msg = getResources().getString(R.string.calculando_jogo);
                    publishProgress(String.format(msg, listJogoGerado.size(), qtdeJogosPossiveis));
                }
                if (isCancelled()) break;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(JogosGeradosActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.calculando));
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new cancelTaskGeraJogos(this));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void retorno) {
            progressDialog.dismiss();
            Collections.sort(listJogoGerado);
            JogosGeradosAdapter adapter = new JogosGeradosAdapter(JogosGeradosActivity.this, listJogoGerado);
            listViewGeraJogos.setAdapter(adapter);
            String label = getResources().getString(R.string.label_total_jogos);
            textViewTotalJogos.setText(String.format(label, listJogoGerado.size()));
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

    private class cancelTaskGeraJogos implements DialogInterface.OnCancelListener {

        private AsyncTask task;

        public cancelTaskGeraJogos(AsyncTask task) {
            this.task = task;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            new DialogBox(JogosGeradosActivity.this,
                    DialogBox.DialogBoxType.QUESTION,
                    getResources().getString(R.string.label_gerar_jogos),
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

    /**
     * Classe interna para implementar o retorno do DialogNumber
     */
    private class OnNumberSetListener implements NumberPickerDialog.OnNumberSetListener {
        private TextView textView;

        public OnNumberSetListener(TextView textView) {
            this.textView = textView;

        }

        @Override
        public void onNumberSet(int number) {
            if (number <= dezenasSelecionadas.length) {
                qtdeDezenasPorJogo = number;
                String label = String.format(getResources().getString(R.string.label_dezenas_por_jogo), qtdeDezenasPorJogo);
                textView.setText(label);
                listarJogosGerados(qtdeDezenasPorJogo);
            }
        }
    }
}
