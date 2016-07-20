package br.com.daciosoftware.loteriasdms;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;

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
import br.com.daciosoftware.loteriasdms.db.Database;
import br.com.daciosoftware.loteriasdms.menuadapter.LoteriasDMSAdapter;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DateUtil;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loterias_dms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitle(getResources().getString(R.string.subtitle_dezenas_mais_sorteadas));
        setSupportActionBar(toolbar);

        String[] menuMain = {
                getResources().getString(R.string.mega_sena),
                getResources().getString(R.string.lotofacil),
                getResources().getString(R.string.quina)};

        ListView listViewMenuMain = (ListView) findViewById(R.id.listViewMenuMain);
        listViewMenuMain.setAdapter(new LoteriasDMSAdapter(getApplicationContext(),menuMain));
        listViewMenuMain.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MegasenaDAO megasenaDAO = new MegasenaDAO(getApplicationContext(), new MegasenaContract());
                for(Sorteio megasena1 : megasenaDAO.listAll()){
                    megasenaDAO.delete(megasena1);
                }
                Megasena megasena = new Megasena();
                megasena.setNumero(1);
                Calendar data = Calendar.getInstance();
                data.set(2001,Calendar.MARCH,21);
                megasena.setData(data);
                megasena.setLocal("Parnaiba");
                megasena.setD1(10);
                megasena.setD2(15);
                megasena.setD3(30);
                megasena.setD4(35);
                megasena.setD5(36);
                megasena.setD6(44);
                megasenaDAO.save(megasena);
                for(Sorteio megasena1 : megasenaDAO.listAll()){
                    Log.i(Constantes.CATEGORIA, megasena1.toString());
                }

                LotofacilDAO lotofacilDAO = new LotofacilDAO(getApplicationContext(), new LotofacilContract());
                for(Sorteio lotofacil1 : lotofacilDAO.listAll()){
                    lotofacilDAO.delete(lotofacil1);
                }
                Lotofacil lotofacil = new Lotofacil();
                lotofacil.setNumero(1);
                Calendar data2 = Calendar.getInstance();
                data2.set(2013, Calendar.APRIL,30);
                lotofacil.setData(data2);
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
                for(Sorteio lotofacil1 : lotofacilDAO.listAll()){
                    Log.i(Constantes.CATEGORIA, lotofacil1.toString());
                }


                QuinaDAO quinaDAO = new QuinaDAO(getApplicationContext(), new QuinaContract());
                for(Sorteio quina1 : quinaDAO.listAll()){
                    quinaDAO.delete(quina1);
                }
                Quina quina = new Quina();
                quina.setNumero(1);
                String dataBr = "21/08/2001";
                quina.setData(DateUtil.dateBrToCalendar(dataBr));
                quina.setLocal("Parnaiba");
                quina.setD1(10);
                quina.setD2(15);
                quina.setD3(30);
                quina.setD4(35);
                quina.setD5(80);

                quinaDAO.save(quina);
                for(Sorteio quina1 : quinaDAO.listAll()){
                    Log.i(Constantes.CATEGORIA, quina1.toString());
                }

            }
        });
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
        switch (position){
            case 0:
                typeSorteio=TypeSorteio.MEGASENA;
                break;
            case 1:
                typeSorteio=TypeSorteio.LOTOFACIL;
                break;
            case 2:
                typeSorteio=TypeSorteio.QUINA;
                break;
            default: typeSorteio=TypeSorteio.MEGASENA;
        }

        Intent intent = new Intent(MainActivity.this, MenuSecundarioActivity.class);
        intent.putExtra(Constantes.TYPE_SORTEIO,typeSorteio);
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

}
