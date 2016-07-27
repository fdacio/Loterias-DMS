package br.com.daciosoftware.loteriasdms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Calendar;

import br.com.daciosoftware.loteriasdms.configuracoes.ConfiguracoesActivity;
import br.com.daciosoftware.loteriasdms.dao.Lotofacil;
import br.com.daciosoftware.loteriasdms.dao.LotofacilContract;
import br.com.daciosoftware.loteriasdms.dao.LotofacilDAO;
import br.com.daciosoftware.loteriasdms.dao.Megasena;
import br.com.daciosoftware.loteriasdms.dao.MegasenaContract;
import br.com.daciosoftware.loteriasdms.dao.MegasenaDAO;
import br.com.daciosoftware.loteriasdms.dao.Quina;
import br.com.daciosoftware.loteriasdms.dao.QuinaContract;
import br.com.daciosoftware.loteriasdms.dao.QuinaDAO;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.db.Database;
import br.com.daciosoftware.loteriasdms.menuadapter.LoteriasDMSAdapter;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DateUtil;
import br.com.daciosoftware.loteriasdms.util.DialogBox;
import br.com.daciosoftware.loteriasdms.util.HttpConnection;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);
            toolbar.setTitle(getResources().getString(R.string.app_name));
            toolbar.setSubtitle(getResources().getString(R.string.subtitle_dezenas_mais_sorteadas));
            setSupportActionBar(toolbar);
        }

        String[] menuMain = {
                getResources().getString(R.string.mega_sena),
                getResources().getString(R.string.lotofacil),
                getResources().getString(R.string.quina)};

        ListView listViewMenuMain = (ListView) findViewById(R.id.listViewMenuMain);
        if (listViewMenuMain != null) {
            listViewMenuMain.setAdapter(new LoteriasDMSAdapter(getApplicationContext(), menuMain));
            listViewMenuMain.setOnItemClickListener(this);
        }

        Button buttonConfig = (Button) findViewById(R.id.buttonConfig);
        buttonConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MegasenaDAO megasenaDAO = new MegasenaDAO(getApplicationContext(), new MegasenaContract());
                    for (Sorteio megasena1 : megasenaDAO.listAll()) {
                        megasenaDAO.delete(megasena1);
                    }
                    Megasena megasena = new Megasena();
                    megasena.setNumero(1);
                    Calendar data = Calendar.getInstance();
                    data.set(2001, Calendar.MARCH, 21);
                    megasena.setData(data);
                    megasena.setLocal("Parnaiba");
                    megasena.setD1(10);
                    megasena.setD2(15);
                    megasena.setD3(30);
                    megasena.setD4(35);
                    megasena.setD5(36);
                    megasena.setD6(44);
                    megasenaDAO.save(megasena);
                    megasena.setNumero(2);
                    Calendar data2 = Calendar.getInstance();
                    data2.set(2002, Calendar.DECEMBER, 10);
                    megasena.setData(data2);
                    megasenaDAO.save(megasena);
                    megasena.setNumero(3);
                    Calendar data3 = Calendar.getInstance();
                    data2.set(2002, Calendar.DECEMBER, 10);
                    megasena.setData(data3);

                    megasenaDAO.save(megasena);
                    for (Sorteio megasena1 : megasenaDAO.listAll()) {
                        Log.i(Constantes.CATEGORIA, megasena1.toString());
                    }

                    LotofacilDAO lotofacilDAO = new LotofacilDAO(getApplicationContext(), new LotofacilContract());
                    for (Sorteio lotofacil1 : lotofacilDAO.listAll()) {
                        lotofacilDAO.delete(lotofacil1);
                    }
                    Lotofacil lotofacil = new Lotofacil();
                    lotofacil.setNumero(1);
                    Calendar data22 = Calendar.getInstance();
                    data22.set(2013, Calendar.APRIL, 30);
                    lotofacil.setData(data22);
                    lotofacil.setLocal("Parnaiba");
                    lotofacil.setD1(10);
                    lotofacil.setD2(15);
                    lotofacil.setD3(30);
                    lotofacil.setD4(35);
                    lotofacil.setD5(36);
                    lotofacil.setD6(44);
                    lotofacil.setD7(1);
                    lotofacil.setD8(10);
                    lotofacil.setD9(13);
                    lotofacil.setD10(19);
                    lotofacil.setD11(33);
                    lotofacil.setD12(17);
                    lotofacil.setD13(16);
                    lotofacil.setD14(8);
                    lotofacil.setD15(33);

                    lotofacilDAO.save(lotofacil);
                    lotofacil.setNumero(2);
                    lotofacilDAO.save(lotofacil);
                    lotofacil.setNumero(3);
                    lotofacilDAO.save(lotofacil);

                    for (Sorteio lotofacil1 : lotofacilDAO.listAll()) {
                        Log.i(Constantes.CATEGORIA, lotofacil1.toString());
                    }


                    QuinaDAO quinaDAO = new QuinaDAO(getApplicationContext(), new QuinaContract());
                    for (Sorteio quina1 : quinaDAO.listAll()) {
                        quinaDAO.delete(quina1);
                    }
                    Quina quina = new Quina();
                    quina.setNumero(1);
                    String dataBr = "21/08/2001";
                    try {
                        quina.setData(DateUtil.dateBrToCalendar(dataBr));
                    } catch (ParseException e) {

                    }
                    quina.setLocal("Parnaiba");
                    quina.setD1(10);
                    quina.setD2(15);
                    quina.setD3(30);
                    quina.setD4(35);
                    quina.setD5(80);

                    quinaDAO.save(quina);
                    quina.setNumero(2);
                    quinaDAO.save(quina);
                    quina.setNumero(3);
                    quinaDAO.save(quina);

                    for (Sorteio quina1 : quinaDAO.listAll()) {
                        Log.i(Constantes.CATEGORIA, quina1.toString());
                    }

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loterias_dm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            exitApp(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TypeSorteio typeSorteio;
        switch (position) {
            case 0:
                typeSorteio = TypeSorteio.MEGASENA;
                break;
            case 1:
                typeSorteio = TypeSorteio.LOTOFACIL;
                break;
            case 2:
                typeSorteio = TypeSorteio.QUINA;
                break;
            default:
                typeSorteio = TypeSorteio.MEGASENA;
        }

        Intent intent = new Intent(MainActivity.this, MenuSecundarioActivity.class);
        intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        exitApp(this);
    }

    public void exitApp(final Activity activity) {
        new DialogBox(this,
                DialogBox.DialogBoxType.QUESTION,
                getResources().getString(R.string.app_name),
                getResources().getString(R.string.msg_sair_app),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Database.encerrarSessao();
        System.exit(0);
    }


    private class atualizarSorteioWebServiceTask extends AsyncTask<String, String, String> {

        private boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            TypeSorteio typeSorteio = null;
            for (int tipoSorteio = 1; tipoSorteio < 4; tipoSorteio++) {
                switch (tipoSorteio){
                    case 1: typeSorteio = TypeSorteio.MEGASENA;break;
                    case 2: typeSorteio = TypeSorteio.QUINA;break;
                    case 3: typeSorteio = TypeSorteio.LOTOFACIL;break;
                }
                SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREF, MODE_PRIVATE);
                String urlWebService = sharedPreferences.getString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
                try {
                    String jsonWebService = HttpConnection.getContent(urlWebService + String.valueOf(tipoSorteio));
                    JSONObject jsonObject = new JSONObject(jsonWebService);
                    int numero = jsonObject.getInt("NumeroConcurso");
                    Calendar data = DateUtil.dateUSToCalendar(jsonObject.getString("Data"));
                    String local = jsonObject.getString("RealizadoEm").split(" ")[3];
                    String numeros = jsonObject.getString("Numeros");
                    String[] dezenas = numeros.replace("[","").replace("]","").split(",");

                    SorteioDAO sorteioDAO = SorteioDAO.getDAO(MainActivity.this, typeSorteio);

                    Sorteio sorteio = sorteioDAO.getInstanciaEntity();
                    sorteio.setNumero(numero);
                    sorteio.setData(data);
                    sorteio.setLocal(local);

                    java.lang.reflect.Method methodGet = null;
                    for (int i = 0; i < dezenas.length; i++) {
                        int dezena = Integer.parseInt(dezenas[i]);

                        String methodName = "setD" + String.valueOf(i + 1);
                        try {
                            methodGet = sorteio.getClass().getMethod(methodName,Integer.TYPE);
                        } catch (SecurityException | NoSuchMethodException e) {
                            return "Erro ao obter dados do Web Service: " + e.getMessage();
                        }

                        if (methodGet != null) {
                            try {
                                methodGet.invoke(sorteio,dezena);
                            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                                return "Erro ao obter dados do Web Service: " + e.getMessage();
                            }
                        }
                    }


                } catch (IOException | JSONException|ParseException e) {
                    return "Erro ao obter dados do Web Service: " + e.getMessage();
                }

            }

            return "Atualização realizada com sucesso.";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Atualizando Sorteios. Aguarde...");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new cancelTaskAtualizarSorteio(this));
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(String retorno) {
            progressDialog.dismiss();
            new DialogBox(MainActivity.this, DialogBox.DialogBoxType.INFORMATION, "Atualização de Sorteios", retorno).show();

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Atualização cancelado!", Toast.LENGTH_SHORT).show();
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(String... msgs) {
            String newMsg = msgs[0];
            progressDialog.setMessage(newMsg);


        }

    }


    private class cancelTaskAtualizarSorteio implements DialogInterface.OnCancelListener {

        private AsyncTask task;

        public cancelTaskAtualizarSorteio(AsyncTask task) {
            this.task = task;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            new DialogBox(MainActivity.this,
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

    private String getLinkSorteios(TypeSorteio typeSorteio) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREF, MODE_PRIVATE);

        switch (typeSorteio) {
            case MEGASENA:
                return sharedPreferences.getString(Constantes.URL_RESULTADOS_MEGASENA, Constantes.URL_RESULTADOS_MEGASENA_DEFAULT);

            case LOTOFACIL:
                return sharedPreferences.getString(Constantes.URL_RESULTADOS_LOTOFACIL, Constantes.URL_RESULTADOS_LOTOFACIL_DEFAULT);

            case QUINA:
                return sharedPreferences.getString(Constantes.URL_RESULTADOS_QUINA, Constantes.URL_RESULTADOS_QUINA_DEFAULT);

            default:
                return "www.cef.gov.br/loterias";
        }
    }


    public int getNumeroUltimoSorteio(TypeSorteio typeSorteio) throws NumberFormatException, IOException {

        String url = getLinkSorteios(typeSorteio);
        String html = HttpConnection.getContent(url);

        Element divResultados = Jsoup.parse(html).select("div#resultados").first();

        Element spanNumeroData = divResultados.select("span").first();

        String numerodata = spanNumeroData.text();

        String numero = numerodata.split(" ")[1];

        return Integer.parseInt(numero);

    }

}
