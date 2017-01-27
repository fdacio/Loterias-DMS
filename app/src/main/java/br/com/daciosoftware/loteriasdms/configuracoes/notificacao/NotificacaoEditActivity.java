package br.com.daciosoftware.loteriasdms.configuracoes.notificacao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.notificacao.AgendaServicoNotificacao;

public class NotificacaoEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch switch1 = (Switch) findViewById(R.id.switch1);
        boolean isNotificar = new NotificacaoDAO(this).isNotificar();
        switch1.setChecked(isNotificar);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                   new NotificacaoDAO(buttonView.getContext()).save(isChecked);

                                                   if (isChecked) {
                                                       new AgendaServicoNotificacao().agendar(NotificacaoEditActivity.this, 15);
                                                   } else {
                                                       new AgendaServicoNotificacao().cancelarAgendamento(NotificacaoEditActivity.this);
                                                   }

                                               }
                                           }
        );
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
