package br.com.daciosoftware.loteriasdms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.daciosoftware.loteriasdms.menuadapter.MenuSecundarioAdapter;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class MenuSecundarioActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private TypeSorteio typeSorteio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_secundario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);

        String[] menuMain = {
                getResources().getString(R.string.menu_dezenas_mais_sorteadas),
                getResources().getString(R.string.menu_sorteios),
                getResources().getString(R.string.menu_processar_arquivo),
                getResources().getString(R.string.menu_confira_seu_jogo),};

        ListView listViewMenuSecundario = (ListView) findViewById(R.id.listViewMenuSecundario);
        listViewMenuSecundario.setAdapter(new MenuSecundarioAdapter(getApplicationContext(),menuMain,typeSorteio));
        listViewMenuSecundario.setOnItemClickListener(this);

        View layout = (View) findViewById(R.id.layout_activity_menu_secundario);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this,layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position){
            case 0:
                intent = new Intent(MenuSecundarioActivity.this, DezenasMaisSorteadasActivity.class);
                break;
            case 1:
                intent = new Intent(MenuSecundarioActivity.this, SorteioListActivity.class);
                break;
            case 2:
                intent = new Intent(MenuSecundarioActivity.this, ProcessarArquivoActivity.class);
                break;
            case 3:
                intent = new Intent(MenuSecundarioActivity.this, ConfiraSeuJogoActivity.class);
                break;
        }

        intent.putExtra(Constantes.TYPE_SORTEIO,typeSorteio);
        startActivity(intent);

    }
}
