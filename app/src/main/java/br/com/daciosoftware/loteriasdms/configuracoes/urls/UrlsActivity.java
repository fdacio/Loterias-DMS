package br.com.daciosoftware.loteriasdms.configuracoes.urls;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class UrlsActivity extends AppCompatActivity implements UrlsEditListener {

    private List<Urls> listUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buildList();
    }


    private void buildList() {
        ListView listViewUrls = (ListView) findViewById(R.id.listViewUrls);
        listUrls = new UrlsDAO(this).listAll();
        listViewUrls.setAdapter(new UrlsAdapter(this, listUrls, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_urls, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();

                return true;
            case R.id.restaurar:
                DialogBox dialogBox2 = new DialogBox(this,
                        DialogBox.DialogBoxType.QUESTION,
                        getResources().getString(R.string.msg_restaurar_config),
                        "",
                        new OnClickYesDialogRestore(),
                        new OnClickNoDialogRestore());
                dialogBox2.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        buildList();
    }

    @Override
    public void editClicked(int position) {
        Intent intent = new Intent(this, UrlsEditActivity.class);
        intent.putExtra(Constantes.URLS, listUrls.get(position));
        startActivity(intent);
    }

    private class OnClickYesDialogRestore implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            new UrlsDAO(UrlsActivity.this).saveDefaulValues();
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
