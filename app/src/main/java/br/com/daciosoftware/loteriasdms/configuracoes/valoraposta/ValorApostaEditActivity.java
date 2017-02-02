package br.com.daciosoftware.loteriasdms.configuracoes.valoraposta;

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
import br.com.daciosoftware.loteriasdms.configuracoes.ValorApostaDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class ValorApostaEditActivity extends AppCompatActivity {

    private ValorApostaDAO valorApostaDAO;
    private EditText editTextValor;
    private List<String> fieldsValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valor_aposta_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TypeSorteio typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);

        valorApostaDAO = ValorApostaDAOFactory.getInstance(this, typeSorteio);

        TextView textViewHeader = (TextView) findViewById(R.id.textViewHeader);
        textViewHeader.setText(String.valueOf(valorApostaDAO.getHeader()));

        editTextValor = (EditText) findViewById(R.id.editTextValor);
        editTextValor.setText(String.valueOf(valorApostaDAO.getValor()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean validaForm() {
        fieldsValidate = new ArrayList<>();
        String url = editTextValor.getText().toString();

        if (url.equals("")) {
            fieldsValidate.add(valorApostaDAO.getLabel());
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
                    valorApostaDAO.save(Float.valueOf(editTextValor.getText().toString()));
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
        editTextValor.setText(String.valueOf(valorApostaDAO.getValor()));
    }


}
