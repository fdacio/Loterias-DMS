package br.com.daciosoftware.loteriasdms.notificacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.sorteio.SorteioListListener;
import br.com.daciosoftware.loteriasdms.util.DeviceInformation;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.webservice.AtualizaSorteiosTask;
import br.com.daciosoftware.loteriasdms.webservice.SorteioWebService;

public class AtualizaSorteioListActivity extends AppCompatActivity implements SorteioListListener {

    private List<Sorteio> listSorteio;
    private ListView listViewSorteio;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualiza_sorteio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);
            toolbar.setTitle(getResources().getString(R.string.title_activity_atualiza_sorteio));
            toolbar.setSubtitle(getResources().getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }

        listViewSorteio = (ListView) findViewById(R.id.listViewSorteio);
        listViewSorteio.setEmptyView(findViewById(R.id.emptyElement));

        new ListaSorteiosTask(this).execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listSorteio.size() > 0) {
                    if (DeviceInformation.isNetwork(AtualizaSorteioListActivity.this)) {
                        new AtualizaSorteiosTask(AtualizaSorteioListActivity.this, null, AtualizaSorteioListActivity.this).execute();

                    } else {
                        new DialogBox(AtualizaSorteioListActivity.this,
                                DialogBox.DialogBoxType.INFORMATION, "Error",
                                getResources().getString(R.string.error_conexao)
                        ).show();
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_urls, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.restaurar) {
            new ListaSorteiosTask(this).execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void executarAposAtualizacao() {
        new ListaSorteiosTask(this).execute();
    }

    private class ListaSorteiosTask extends AsyncTask<Void, String, String> {

        private Context context;

        public ListaSorteiosTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                listSorteio = new SorteioWebService(AtualizaSorteioListActivity.this).getSorteiosAtualizar();


            } catch (Exception e) {
                return context.getResources().getString(R.string.erro_web_service) + e.getMessage();
            }
            return "OK";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.listando));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String retorno) {
            progressDialog.dismiss();
            if (retorno.equals("OK")) {

                listViewSorteio.setAdapter(new AtualizaSorteioListAdapter(AtualizaSorteioListActivity.this, listSorteio));

            } else {
                new DialogBox(AtualizaSorteioListActivity.this,
                        DialogBox.DialogBoxType.INFORMATION,
                        AtualizaSorteioListActivity.this.getResources().getString(R.string.atualizacao_sorteios),
                        retorno)
                        .show();

            }
        }

    }
}
