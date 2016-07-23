package br.com.daciosoftware.loteriasdms.configuracoes;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class ConfiguracoesActivity extends AppCompatActivity {

    private EditText editTextUrlArquivoResultadoMegasena;
    private EditText editTextUrlResultadoMegasena;
    private EditText editTextUrlArquivoResultadoLotofacil;
    private EditText editTextUrlResultadoLotofacil;
    private EditText editTextUrlArquivoResultadoQuina;
    private EditText editTextUrlResultadoQuina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextUrlArquivoResultadoMegasena = (EditText) findViewById(R.id.editTextUrlArquivoResultadoMegasena);
        editTextUrlResultadoMegasena = (EditText)findViewById(R.id.editTextUrlResultadoMegasena);
        editTextUrlArquivoResultadoLotofacil = (EditText) findViewById(R.id.editTextUrlArquivoResultadoLotofacil);
        editTextUrlResultadoLotofacil = (EditText) findViewById(R.id.editTextUrlResultadoLotofacil);
        editTextUrlArquivoResultadoQuina = (EditText) findViewById(R.id.editTextUrlArquivoResultadoQuina);
        editTextUrlResultadoQuina = (EditText) findViewById(R.id.editTextUrlResultadoQuina);


        loadUrlInForm();


    }


    private void loadUrlInForm(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREF,0);

        editTextUrlArquivoResultadoMegasena.setText(sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA, Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA_DEFAULT));
        editTextUrlResultadoMegasena.setText(sharedPreferences.getString(Constantes.URL_RESULTADOS_MEGASENA, Constantes.URL_RESULTADOS_MEGASENA_DEFAULT));

        editTextUrlArquivoResultadoLotofacil.setText(sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL,Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL_DEFAULT));
        editTextUrlResultadoLotofacil.setText(sharedPreferences.getString(Constantes.URL_RESULTADOS_LOTOFACIL,Constantes.URL_RESULTADOS_LOTOFACIL_DEFAULT));

        editTextUrlArquivoResultadoQuina.setText(sharedPreferences.getString(Constantes.URL_ARQUIVO_RESULTADOS_QUINA,Constantes.URL_ARQUIVO_RESULTADOS_QUINA_DEFAULT));
        editTextUrlResultadoQuina.setText(sharedPreferences.getString(Constantes.URL_RESULTADOS_QUINA,Constantes.URL_RESULTADOS_QUINA_DEFAULT));
    }

    private void saveUrlInPref(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_MEGASENA,editTextUrlArquivoResultadoMegasena.getText().toString());
        editor.putString(Constantes.URL_RESULTADOS_MEGASENA,editTextUrlResultadoMegasena.getText().toString());

        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_LOTOFACIL,editTextUrlArquivoResultadoLotofacil.getText().toString());
        editor.putString(Constantes.URL_RESULTADOS_LOTOFACIL,editTextUrlResultadoLotofacil.getText().toString());

        editor.putString(Constantes.URL_ARQUIVO_RESULTADOS_QUINA,editTextUrlArquivoResultadoQuina.getText().toString());
        editor.putString(Constantes.URL_RESULTADOS_QUINA,editTextUrlResultadoQuina.getText().toString());

        editor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                DialogBox dialogBox = new DialogBox(this,
                        DialogBox.DialogBoxType.QUESTION,
                        getResources().getString(R.string.msg_salvar_configuracoes),
                        "",
                        new OnClickYesDialog(),
                        new OnClickNoDialog());
                dialogBox.show();


                return true;
            case R.id.desfazer:
                loadUrlInForm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class OnClickYesDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            saveUrlInPref();
            finish();
        }
    }

    private class OnClickNoDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            loadUrlInForm();
        }
    }


}
