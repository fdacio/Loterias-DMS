package br.com.daciosoftware.loteriasdms;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.daciosoftware.loteriasdms.db.Database;
import br.com.daciosoftware.loteriasdms.menuadapter.LoteriasDMSAdapter;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class LoteriasDMSActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


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
               //Rotina de leitura de soteio da pagina web da caixa.
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

        Intent intent = new Intent(LoteriasDMSActivity.this, MenuSecundarioActivity.class);
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
