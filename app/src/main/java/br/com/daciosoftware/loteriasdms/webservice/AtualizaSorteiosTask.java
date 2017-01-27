package br.com.daciosoftware.loteriasdms.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAOFactory;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.sorteio.SorteioListListener;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

/**
 * Created by Dácio Braga on 28/07/2016.
 */

public class AtualizaSorteiosTask extends AsyncTask<Void, String, String> {

    private Context context;
    private TypeSorteio typeSorteio;
    private ProgressDialog progressDialog;
    private SorteioListListener sorteioListListener;

    public AtualizaSorteiosTask(Context context, TypeSorteio typeSorteio) {
        this.context = context;
        this.typeSorteio = typeSorteio;
    }

    public AtualizaSorteiosTask(Context context, TypeSorteio typeSorteio, SorteioListListener sorteioListListener) {
        this(context, typeSorteio);
        this.sorteioListListener = sorteioListListener;
    }

    @Override
    protected String doInBackground(Void... v) {

        SorteioWebService sorteioWebService = new SorteioWebService(context);

        /*
        Atualização do último sorteio, via tela inicial, de todos tipos sorteios
         */
        if (this.typeSorteio == null) {

            try {
                for (TypeSorteio typeSorteio : TypeSorteio.values()) {

                    SorteioDAO sorteioDAO = SorteioDAOFactory.getInstance(context, typeSorteio);
                    Sorteio sorteio = sorteioWebService.getUltimoSorteioFromWebService(typeSorteio);

                    String msg = String.format(context.getResources().getString(R.string.atualizando_sorteio_concurso), sorteio.getNome(), sorteio.getNumero());
                    publishProgress(msg);

                    if (sorteioDAO.findByNumber(sorteio.getNumero()) == null) {
                        sorteioDAO.save(sorteio);
                    }

                    if (isCancelled()) {
                        break;
                    }
                }

            } catch (Exception e) {
                //Log.i(Constantes.CATEGORIA, "Error=" + e.getMessage());
                return context.getResources().getString(R.string.erro_web_service) + e.getMessage();
            }

        /*
        Atualização de todos sorteios
         */
        } else {

            try {

                SorteioDAO sorteioDAO = SorteioDAOFactory.getInstance(context, typeSorteio);

                int numeroSorteio = (sorteioDAO.findLast() != null) ? sorteioDAO.findLast().getNumero() : sorteioWebService.getUltimoSorteioFromWebService(typeSorteio).getNumero();

                Sorteio sorteio = sorteioWebService.getSorteioFromWebService(typeSorteio, numeroSorteio);

                while (sorteio != null) {

                    String msg = String.format(context.getResources().getString(R.string.atualizando_sorteio_concurso), sorteio.getNome(), sorteio.getNumero());
                    publishProgress(msg);

                    if (sorteioDAO.findByNumber(sorteio.getNumero()) == null) {
                        sorteioDAO.save(sorteio);
                    }

                    numeroSorteio--;
                    sorteio = sorteioWebService.getSorteioFromWebService(typeSorteio, numeroSorteio);

                    if (isCancelled()) {
                        break;
                    }

                }

            } catch (Exception e) {
                return context.getResources().getString(R.string.erro_web_service) + e.getMessage();
            }
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

            if (this.sorteioListListener != null) {
                this.sorteioListListener.executarAposAtualizacao();
            }

        } else {
            new DialogBox(context, DialogBox.DialogBoxType.INFORMATION, context.getResources().getString(R.string.atualizacao_sorteios), retorno).show();
        }
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


    private class cancelTaskAtualizarSorteio implements DialogInterface.OnCancelListener {

        private AtualizaSorteiosTask task;

        public cancelTaskAtualizarSorteio(AtualizaSorteiosTask task) {
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

                            if (task.sorteioListListener != null) {
                                task.sorteioListListener.executarAposAtualizacao();
                            }

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
