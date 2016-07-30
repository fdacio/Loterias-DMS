package br.com.daciosoftware.loteriasdms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Calendar;

import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DateUtil;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.HttpConnection;

/**
 * Created by Dácio Braga on 28/07/2016.
 */

/**
 *
 */
public class AtualizaUltimoSorteioWebServiceTask extends AsyncTask<Void, String, String> {

    private Context context;
    private boolean running = true;
    private ProgressDialog progressDialog;
    private String msg = "Atualizando Sorteios.\nAguarde...";

    public AtualizaUltimoSorteioWebServiceTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... v) {

        TypeSorteio typeSorteio;
        String nomeSorteio;

        int jogo = 1;
        while (jogo < 4) {

            switch (jogo) {
                case 1:
                    typeSorteio = TypeSorteio.MEGASENA;
                    nomeSorteio = "Mega-Sena";
                    break;

                case 2:
                    typeSorteio = TypeSorteio.QUINA;
                    nomeSorteio = "Quina";
                    break;

                case 3:
                    typeSorteio = TypeSorteio.LOTOFACIL;
                    nomeSorteio = "Lotofácil";
                    break;

                default:
                    typeSorteio = null;
                    nomeSorteio = null;
            }


            try {

                SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, context.MODE_PRIVATE);
                String urlWebService = sharedPreferences.getString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
                SorteioDAO sorteioDAO = SorteioDAO.getDAO(context, typeSorteio);

                //Obtem o Objeto JSon com os dados do ultimo sorteio
                String jsonWebService = HttpConnection.getContentJSON(urlWebService + String.valueOf(jogo));
                JSONObject jsonObject = new JSONObject(jsonWebService);

                int numero = jsonObject.getInt("NumeroConcurso");
                Calendar data = DateUtil.dateUSToCalendar(jsonObject.getString("Data"));
                String local = jsonObject.getString("RealizadoEm");
                JSONArray jsonArray = jsonObject.optJSONArray("Sorteios");
                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                String numeros = jsonObject2.getString("Numeros");
                String[] dezenas = numeros.replace("[", "").replace("]", "").split(",");

                msg = "Atualizando Sorteios.\n" +
                        nomeSorteio + " Concurso:" + numero + "\n" +
                        "Aguarde...";
                publishProgress(msg);

                Sorteio sorteio = sorteioDAO.getInstanciaEntity();
                sorteio.setNumero(numero);
                sorteio.setData(data);
                sorteio.setLocal(local);

                java.lang.reflect.Method methodGet = null;
                for (int i = 0; i < dezenas.length; i++) {
                    int dezena = Integer.parseInt(dezenas[i]);

                    String methodName = "setD" + String.valueOf(i + 1);
                    try {
                        methodGet = sorteio.getClass().getMethod(methodName, Integer.TYPE);
                    } catch (SecurityException | NoSuchMethodException e) {
                        return "Erro ao obter dados do Web Service: " + e.getMessage();
                    }

                    if (methodGet != null) {
                        try {
                            methodGet.invoke(sorteio, dezena);
                        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                            return "Erro ao obter dados do Web Service: " + e.getMessage();
                        }
                    }
                }
                if (sorteioDAO.findByNumber(numero) == null) {
                    sorteioDAO.save(sorteio);
                }


            } catch (IOException | JSONException | ParseException e) {
                return "Erro ao obter dados do Web Service: " + e.getMessage();
            }

            jogo++;

            if(!running) return "Atualização cancelada";

        }//Fim do while

        return "Atualização realizada com sucesso.";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setOnCancelListener(new cancelTaskAtualizarSorteio(this));
        progressDialog.show();
    }


    @Override
    protected void onPostExecute(String retorno) {
        progressDialog.dismiss();
        new DialogBox(context, DialogBox.DialogBoxType.INFORMATION, "Atualização de Sorteios", retorno).show();

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(context, "Atualização cancelado!", Toast.LENGTH_SHORT).show();
        running = false;

    }

    @Override
    protected void onProgressUpdate(String... msgs) {
        String newMsg = msgs[0];
        progressDialog.setMessage(newMsg);
    }


    private class cancelTaskAtualizarSorteio implements DialogInterface.OnCancelListener {

        private AsyncTask task;

        public cancelTaskAtualizarSorteio(AsyncTask task) {
            this.task = task;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            new DialogBox(context,
                    DialogBox.DialogBoxType.QUESTION,
                    "Processar Arquivo",
                    "Deseja cancelar o processo?",
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
