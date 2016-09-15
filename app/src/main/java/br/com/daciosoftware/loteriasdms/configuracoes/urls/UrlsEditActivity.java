package br.com.daciosoftware.loteriasdms.configuracoes.urls;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class UrlsEditActivity extends AppCompatActivity {

    private Urls urls;
    private EditText editTextUrlResultado;
    private EditText editTextUrlArquivo;
    private List<String> fieldsValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urls_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        urls = (Urls) getIntent().getSerializableExtra(Constantes.URLS);

        TextView textViewHeaderJogo = (TextView) findViewById(R.id.textViewHeaderJogo);
        editTextUrlResultado = (EditText) findViewById(R.id.editTextUrlResultado);
        editTextUrlArquivo = (EditText) findViewById(R.id.editTextUrlArquivo);

        textViewHeaderJogo.setText(urls.getHeader());
        textViewHeaderJogo.setTextColor(getColorRgb(urls.getTypeSorteio()));
        editTextUrlResultado.setText(urls.getUrlResultado());
        editTextUrlArquivo.setText(urls.getUrlArquivo());

    }

    private ColorStateList getColorRgb(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return ColorStateList.valueOf(Color.rgb(34, 149, 81));
            case LOTOFACIL:
                return ColorStateList.valueOf(Color.rgb(124, 11, 137));
            case QUINA:
                return ColorStateList.valueOf(Color.rgb(41, 11, 137));
            default:
                return ColorStateList.valueOf(Color.rgb(63, 81, 181));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean validaForm() {
        fieldsValidate = new ArrayList<>();
        String urlResultado = editTextUrlResultado.getText().toString();
        String urlArquivo = editTextUrlArquivo.getText().toString();

        if (urlResultado.equals("")) {
            fieldsValidate.add(getResources().getString(R.string.label_url_resultados));
        }
        if (urlArquivo.equals("")) {
            fieldsValidate.add(getResources().getString(R.string.label_url_arquivo_resultados));
        }

        return fieldsValidate.size() <= 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                if (validaForm()) {
                    urls.setUrlResultado(editTextUrlResultado.getText().toString());
                    urls.setUrlArquivo(editTextUrlArquivo.getText().toString());
                    new UrlsDAO(this).save(urls);
                    finish();
                } else {
                    new DialogBox(this, DialogBox.DialogBoxType.INFORMATION, getResources().getString(R.string.msg_validade_form), this.fieldsValidate.toString()).show();
                }
                return true;
            case R.id.desfazer:
                loadUrlInForm();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUrlInForm() {
        editTextUrlResultado.setText(urls.getUrlResultado());
        editTextUrlArquivo.setText(urls.getUrlArquivo());
    }

}
