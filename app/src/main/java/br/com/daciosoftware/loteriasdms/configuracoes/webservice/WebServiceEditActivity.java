package br.com.daciosoftware.loteriasdms.configuracoes.webservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class WebServiceEditActivity extends AppCompatActivity {

    private WebService webService;
    private EditText editTextUrlWebService;
    private List<String> fieldsValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webService = (WebService) getIntent().getSerializableExtra(Constantes.WEB_SERVICE);
        editTextUrlWebService = (EditText) findViewById(R.id.editTextUrlWebService);
        editTextUrlWebService.setText(webService.getUrlWebService());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean validaForm() {
        fieldsValidate = new ArrayList<>();
        String urlWebService = editTextUrlWebService.getText().toString();

        if (urlWebService.equals("")) {
            fieldsValidate.add(getResources().getString(R.string.label_url_web_service));
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
                    webService.setUrlWebService(editTextUrlWebService.getText().toString());
                    new WebServiceDAO(this).save(webService);
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
        editTextUrlWebService = (EditText) findViewById(R.id.editTextUrlWebService);
    }

}
