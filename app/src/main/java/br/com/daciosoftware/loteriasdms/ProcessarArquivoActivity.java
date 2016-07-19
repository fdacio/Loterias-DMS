package br.com.daciosoftware.loteriasdms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import br.com.daciosoftware.loteriasdms.util.Constantes;

public class ProcessarArquivoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processar_arquivo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View layout = (View) findViewById(R.id.layout_processar_arquivo);
        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);
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


}
