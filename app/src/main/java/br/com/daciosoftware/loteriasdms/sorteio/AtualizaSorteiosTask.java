package br.com.daciosoftware.loteriasdms.sorteio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.HttpConnection;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

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


        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        String urlWebServiceRoot = sharedPreferences.getString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
        SorteioDAO sorteioDAO = SorteioDAO.getDAO(context, typeSorteio);

        String urlWebServicePrimeiroSorteio = urlWebServiceRoot+String.valueOf(jogo);

        try {

            String jsonWebServiceUS = HttpConnection.getContentJSON(urlWebServicePrimeiroSorteio);
            JSONObject jsonObjectUS = new JSONObject(jsonWebServiceUS);
            int numeroUltimoSorteioWS = jsonObjectUS.getInt("NumeroConcurso");
            Sorteio sorteio = sorteioDAO != null ? sorteioDAO.findLast() : null;
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

                     String status = jsonObject.getString("Status");
                     if (status.equals("end")) {
                         return "OK";
                     }

                    int numero = jsonObject.getInt("NumeroConcurso");
                    Calendar data = MyDateUtil.dateUSToCalendar(jsonObject.getString("Data"));
                    String local = jsonObject.getString("RealizadoEm");
                    JSONArray jsonArray = jsonObject.optJSONArray("Sorteios");
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    String numeros = jsonObject2.getString("Numeros");
                    String[] dezenasWS = numeros.replace("[", "").replace("]", "").split(",");
                    int[] dezenas = new int[dezenasWS.length];
                    for (int i = 0; i < dezenas.length; i++) {
                        dezenas[i] = Integer.parseInt(dezenasWS[i]);
                    }


                    String msg = String.format(context.getResources().getString(R.string.atualizando_sorteio_concurso), nomeSorteio, numero);

                    publishProgress(msg);

                    Sorteio sorteioAdd = sorteioDAO != null ? sorteioDAO.getInstanciaEntity() : null;
                    if (sorteioAdd != null) {
                        sorteioAdd.setNumero(numero);
                        sorteioAdd.setData(data);
                        sorteioAdd.setLocal(local);
                        sorteioAdd.setDezenas(dezenas);

                        if (sorteioDAO.findByNumber(numero) == null) {
                            sorteioDAO.save(sorteioAdd);
                        }
                    }

                    numeroSorteio--;

                } catch (IOException | JSONException | ParseException e) {
                    return context.getResources().getString(R.string.erro_web_service) + urlWebServiceSorteio;
                }

                if(isCancelled()){
                    running = false;
                    return  context.getResources().getString(R.string.atualizacao_cancelado);
                }

            }//Fim do while

        } catch (IOException | JSONException  e) {
            return context.getResources().getString(R.string.erro_web_service) + urlWebServicePrimeiroSorteio;
        }

        return "OK";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.atualizando_sorteio));
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setOnCancelListener(new cancelTaskAtualizarSorteio(this));
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String retorno) {
        progressDialog.dismiss();
        if (retorno.equals("OK")) {
            new DialogBox(context, DialogBox.DialogBoxType.INFORMATION, context.getResources().getString(R.string.atualizacao_sorteios), context.getResources().getString(R.string.atualizacao_concluido)).show();
            if (this.atualizacaoSorteioInterface != null) {
                this.atualizacaoSorteioInterface.executarAposAtualizacao();
            }
        } else {
            new DialogBox(context, DialogBox.DialogBoxType.INFORMATION, context.getResources().getString(R.string.atualizacao_sorteios), retorno).show();
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
                    context.getResources().getString(R.string.atualizacao_sorteios),
                    context.getResources().getString(R.string.deseja_cancelar_processo),
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
