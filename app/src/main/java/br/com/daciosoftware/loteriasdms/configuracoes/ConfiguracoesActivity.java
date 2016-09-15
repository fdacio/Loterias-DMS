package br.com.daciosoftware.loteriasdms.configuracoes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.urls.UrlsActivity;
import br.com.daciosoftware.loteriasdms.configuracoes.valoresaposta.ValoresApostaActivity;
import br.com.daciosoftware.loteriasdms.configuracoes.webservice.WebServiceActivity;

public class ConfiguracoesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] menuConfig = {
                getResources().getString(R.string.valores_aposta),
                getResources().getString(R.string.urls),
                getResources().getString(R.string.web_service)};

        ListView listViewMenuConfig = (ListView) findViewById(R.id.listViewMenuConfig);
        listViewMenuConfig.setAdapter(new MenuConfigAdapter(this, menuConfig));
        listViewMenuConfig.setOnItemClickListener(this);
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
        switch (position) {
            case 0:
                intent = new Intent(this, ValoresApostaActivity.class);
                break;
            case 1:
                intent = new Intent(this, UrlsActivity.class);
                break;
            case 2:
                intent = new Intent(this, WebServiceActivity.class);
                break;
        }
        startActivity(intent);
    }

}
