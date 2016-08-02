package br.com.daciosoftware.loteriasdms.sorteio;

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

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.HttpConnection;

/**
 * Created by Dácio Braga on 28/07/2016.
 */

/**
 *
 */
public class AtualizaSorteiosTask extends AsyncTask<Void, String, String> {

    private Context context;
    private TypeSorteio typeSorteio;
    private boolean running = true;
    private String msg = "Atualizando Sorteios.\nAguarde...";
    private ProgressDialog progressDialog;
    private AtualizaSorteiosInterface atualizacaoSorteioInterface;

    public AtualizaSorteiosTask(Context context, TypeSorteio typeSorteio) {
        this.context = context;
        this.typeSorteio = typeSorteio;
    }

    public AtualizaSorteiosTask(Context context, TypeSorteio typeSorteio, AtualizaSorteiosInterface atulizacaoSorteiointerface) {
        this(context, typeSorteio);
        this.atualizacaoSorteioInterface = atulizacaoSorteiointerface;
    }


    @Override
    protected String doInBackground(Void... v) {
        String nomeSorteio;
        int jogo = 0;
        switch (typeSorteio) {
            case MEGASENA:
                nomeSorteio = "Mega-Sena";
                jogo = 1;
                break;
            case QUINA:
                nomeSorteio = "Quina";
                jogo = 2;
                break;
            case LOTOFACIL:
                nomeSorteio = "Lotofácil";
                jogo = 3;
                break;
            default:
                nomeSorteio = "Sorteio";
                break;
        }


        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, context.MODE_PRIVATE);
        String urlWebServiceRoot = sharedPreferences.getString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
        SorteioDAO sorteioDAO = SorteioDAO.getDAO(context, typeSorteio);

        String urlWebServicePrimeiroSorteio = urlWebServiceRoot+String.valueOf(jogo);

        try {

            String jsonWebServiceUS = HttpConnection.getContentJSON(urlWebServicePrimeiroSorteio);
            JSONObject jsonObjectUS = new JSONObject(jsonWebServiceUS);
            int numeroUltimoSorteioWS = jsonObjectUS.getInt("NumeroConcurso");
            Sorteio sorteio = sorteioDAO.findFirst();
            int numeroUltimoSorteioBD = (sorteio != null) ? sorteio.getNumero() : 0;
            int numeroSorteio = (numeroUltimoSorteioBD == 0) ? numeroUltimoSorteioWS : numeroUltimoSorteioBD - 1;


            /*
            Processamento inicia-se do mais rescente sorteio até o mais antigo.
            Se já houver sorteio regisrados, inicia apartir do ultimo registro
             */
            while (running) {
                String urlWebServiceSorteio = urlWebServicePrimeiroSorteio + "/" + numeroSorteio;

                try {
                    String jsonWebService = HttpConnection.getContentJSON(urlWebServiceSorteio);
                    JSONObject jsonObject = new JSONObject(jsonWebService);
                    /*
                    Esse erro acontece pq não tem todos os sorteio, qndo chega nesse ponto
                    é pq não ha mais sorteios disponível.
                     */

                     String status = jsonObject.getString("Status");
                     if (status.equals("end")) {
                          return "Atualização realizada com sucesso.";
                     }

                    int numero = jsonObject.getInt("NumeroConcurso");
                    Calendar data = MyDateUtil.dateUSToCalendar(jsonObject.getString("Data"));
                    String local = jsonObject.getString("RealizadoEm");
                    JSONArray jsonArray = jsonObject.optJSONArray("Sorteios");
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    String numeros = jsonObject2.getString("Numeros");
                    String[] dezenas = numeros.replace("[", "").replace("]", "").split(",");

                    msg = "Atualizando Sorteios.\n" +
                            nomeSorteio + " Concurso:" + numero + "\n" +
                            "Aguarde...";
                    publishProgress(msg);

                    Sorteio sorteioAdd = sorteioDAO.getInstanciaEntity();
                    sorteioAdd.setNumero(numero);
                    sorteioAdd.setData(data);
                    sorteioAdd.setLocal(local);

                    java.lang.reflect.Method methodGet = null;
                    for (int i = 0; i < dezenas.length; i++) {
                        int dezena = Integer.parseInt(dezenas[i]);

                        String methodName = "setD" + String.valueOf(i + 1);
                        try {
                            methodGet = sorteioAdd.getClass().getMethod(methodName, Integer.TYPE);
                        } catch (SecurityException | NoSuchMethodException e) {
                            return "Erro ao gerar o sorteio: " + e.getMessage();
                        }

                        if (methodGet != null) {
                            try {
                                methodGet.invoke(sorteioAdd, dezena);
                            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                                return "Erro ao gerar o sorteio: " + e.getMessage();
                            }
                        }
                    }

                    if (sorteioDAO.findByNumber(numero) == null) {
                        sorteioDAO.save(sorteioAdd);
                    }

                    numeroSorteio--;

                } catch (IOException | JSONException | ParseException e) {
                    return "Erro ao obter dados do Web Service: " + urlWebServiceSorteio;
                }

                if(isCancelled()){
                    running = false;
                    return  "Atualização Cancelada";
                }

            }//Fim do while

        } catch (IOException | JSONException  e) {
            return "Erro ao obter dados do Web Service: " + urlWebServicePrimeiroSorteio;
        }


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
        if(this.atualizacaoSorteioInterface != null) {
            this.atualizacaoSorteioInterface.executarAposAtualizacao();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(this.atualizacaoSorteioInterface != null) {
            this.atualizacaoSorteioInterface.executarAposAtualizacao();
        }
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
