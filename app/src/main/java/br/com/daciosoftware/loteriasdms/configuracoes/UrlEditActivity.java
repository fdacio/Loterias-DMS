package br.com.daciosoftware.loteriasdms.configuracoes;

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
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class UrlEditActivity extends AppCompatActivity {

    private UrlDAO urlDAO;
    private String url;
    private List<String> fieldsValidate;

    private EditText editTextUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urls_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TypeUrl typeUrl = (TypeUrl) getIntent().getSerializableExtra(Constantes.URLS);

        urlDAO = UrlDAOFactory.getInstance(this, typeUrl);

        TextView textViewHeader = (TextView) findViewById(R.id.textViewHeader);
        TextView textViewLabel = (TextView) findViewById(R.id.textViewLabel);
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);

        textViewHeader.setText(urlDAO.getHeader());
        textViewLabel.setText(urlDAO.getLabel());
        editTextUrl.setText(urlDAO.getUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean validaForm() {
        fieldsValidate = new ArrayList<>();
        String url = editTextUrl.getText().toString();

        if (url.equals("")) {
            fieldsValidate.add(urlDAO.getLabel());
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
                    url = editTextUrl.getText().toString();
                    urlDAO.save(url);
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
        editTextUrl.setText(urlDAO.getUrl());
    }

}
