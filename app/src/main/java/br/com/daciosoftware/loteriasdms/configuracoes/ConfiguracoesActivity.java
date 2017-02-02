package br.com.daciosoftware.loteriasdms.configuracoes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados.ArquivoResultadoUrlListActivity;
import br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados.ArquivoResultadoUrlLotofacilDAO;
import br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados.ArquivoResultadoUrlMegasenaDAO;
import br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados.ArquivoResultadoUrlQuinaDAO;
import br.com.daciosoftware.loteriasdms.configuracoes.notificacao.NotificacaoDAO;
import br.com.daciosoftware.loteriasdms.configuracoes.notificacao.NotificacaoEditActivity;
import br.com.daciosoftware.loteriasdms.configuracoes.valoraposta.ValorApostaListActivity;
import br.com.daciosoftware.loteriasdms.configuracoes.webservice.WebServiceDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class ConfiguracoesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String sublabel;
    private List<ItemConfiguracao> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildList();

    }

    private void buildList() {

        lista = new ArrayList<>();

        ItemConfiguracao itemConfiguracao1 = new ItemConfiguracao();
        itemConfiguracao1.setLabel("Notificações");
        NotificacaoDAO dao = new NotificacaoDAO(this);
        sublabel = (dao.isNotificar() ? getResources().getString(R.string.notificacao_switch_on) : getResources().getString(R.string.notificacao_switch_off));
        itemConfiguracao1.setSublabel(sublabel);
        lista.add(itemConfiguracao1);

        ItemConfiguracao itemConfiguracao2 = new ItemConfiguracao("Valores de Aposta", "Valores de aposta dos jogos.");
        lista.add(itemConfiguracao2);

        ItemConfiguracao itemConfiguracao3 = new ItemConfiguracao("Arquivos de Todos Resultados", "Urls para download dos arquivos com todos resultados dos jogos.");
        lista.add(itemConfiguracao3);

        ItemConfiguracao itemConfiguracao4 = new ItemConfiguracao();
        itemConfiguracao4.setLabel("Web Service");
        itemConfiguracao4.setSublabel(new WebServiceDAO(this).getUrl());
        lista.add(itemConfiguracao4);

        ItemConfiguracao itemConfiguracao5 = new ItemConfiguracao();
        itemConfiguracao5.setLabel("Configurações Padrão");
        itemConfiguracao5.setSublabel("Restaurar para as configurações iniciais do aplicativo.");
        lista.add(itemConfiguracao5);



        ListView listViewMenuConfig = (ListView) findViewById(R.id.listViewMenuConfig);
        listViewMenuConfig.setAdapter(new MenuConfigAdapter(this, lista));
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
                intent = new Intent(this, NotificacaoEditActivity.class);
                break;
            case 1:
                intent = new Intent(this, ValorApostaListActivity.class);
                break;
            case 2:
                intent = new Intent(this, ArquivoResultadoUrlListActivity.class);
                break;
            case 3:
                intent = new Intent(this, UrlEditActivity.class);
                intent.putExtra(Constantes.URLS, TypeUrl.WEBSERVICE);
                break;
            case 4:
                restaurarPadroes();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        buildList();
    }

    private void restaurarPadroes() {

        DialogBox dialogBox2 = new DialogBox(this,
                DialogBox.DialogBoxType.QUESTION,
                getResources().getString(R.string.msg_restaurar_config),
                "",
                new OnClickYesDialogRestore(),
                new OnClickNoDialogRestore());
        dialogBox2.show();

    }

    private class OnClickYesDialogRestore implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            new NotificacaoDAO(ConfiguracoesActivity.this).saveDefaulValues();
            new ArquivoResultadoUrlMegasenaDAO(ConfiguracoesActivity.this).saveDefaulValues();
            new ArquivoResultadoUrlLotofacilDAO(ConfiguracoesActivity.this).saveDefaulValues();
            new ArquivoResultadoUrlQuinaDAO(ConfiguracoesActivity.this).saveDefaulValues();
            new WebServiceDAO(ConfiguracoesActivity.this).saveDefaulValues();

            buildList();
        }
    }

    private class OnClickNoDialogRestore implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

}
