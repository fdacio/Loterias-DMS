package br.com.daciosoftware.loteriasdms.sorteio;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DateUtil;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class SorteioListActivity extends AppCompatActivity {

    private List<Sorteio> listSorteio;
    private ListView listViewSorteio;
    private TypeSorteio typeSorteio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        typeSorteio = (TypeSorteio) getIntent().getSerializableExtra(Constantes.TYPE_SORTEIO);

        View layout = findViewById(R.id.layout_sorteio_list);
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(this, layout);
        styleTypeSorteio.setStyleHeader(typeSorteio);

        listViewSorteio = (ListView) findViewById(R.id.listViewSorteio);
        listViewSorteio.setEmptyView(findViewById(R.id.emptyElement));
        listViewSorteio.setAdapter(getSorteioListAdapter(typeSorteio));
        registerForContextMenu(listViewSorteio);

        styleTypeSorteio.setStyleFloatingActionButton(typeSorteio);
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SorteioListActivity.this, SorteioEditActivity.class);
                    intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
                    startActivityForResult(intent, Constantes.INSERT_UPDATE);
                }
            });
        }
    }


    private SorteioListAdapter getSorteioListAdapter(TypeSorteio typeSorteio) {
        SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);
        if (sorteioDAO != null) {
            listSorteio = sorteioDAO.listAll();
        }
        return new SorteioListAdapter(this, listSorteio, typeSorteio);
    }


    private SorteioListAdapter getSorteioListAdapter(TypeSorteio typeSorteio, int numero) {
        SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);
        if (sorteioDAO != null) {
            List<Sorteio> list = new ArrayList<>();
            list.add(sorteioDAO.findByNumber(numero));
            return new SorteioListAdapter(this, list, typeSorteio);
        } else {
            return null;
        }

    }

    private SorteioListAdapter getSorteioListAdapter(TypeSorteio typeSorteio, Calendar date) {
        SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);
        if (sorteioDAO != null) {
            List<Sorteio> list = new ArrayList<>();
            list.add(sorteioDAO.findByDate(date));
            return new SorteioListAdapter(this, list, typeSorteio);
        } else {
            return null;
        }

    }

    private SorteioListAdapter getSorteioListAdapter(TypeSorteio typeSorteio, Calendar date1, Calendar date2) {
        SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);
        if (sorteioDAO != null) {
            listSorteio = sorteioDAO.findByBetweenDate(date1, date2);
        }
        return new SorteioListAdapter(this, listSorteio, typeSorteio);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);

        // Associate searchable configuration with the SearchView
        MenuItem searchItem = menu.findItem(R.id.searchMenu);
        SearchView searchView = null;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) searchItem.getActionView();
        } else {
            searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        }
        //searchView.setSubmitButtonEnabled(true);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String searchFor) {
                    SorteioListAdapter sorteioListAdapter = null;

                    /*
                    Pesquisa pelo numero do concurso
                     */
                    try {
                        int numero = Integer.parseInt(searchFor);
                        sorteioListAdapter = getSorteioListAdapter(typeSorteio, numero);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                    /*
                    Pesquisa pela data do concurso
                     */
                    try {
                        Calendar date = DateUtil.dateBrToCalendar(searchFor);
                        sorteioListAdapter = getSorteioListAdapter(typeSorteio, date);
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }

                    if (sorteioListAdapter != null) {
                        listViewSorteio.setAdapter(sorteioListAdapter);
                    }

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.reloadMenu:
                listViewSorteio.setAdapter(getSorteioListAdapter(typeSorteio));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Sorteio sorteio = listSorteio.get((int) info.id);
        switch (item.getItemId()) {
            case R.id.edit:
                editSorteio(sorteio);
                return true;
            case R.id.delete:
                deleteSorteio(sorteio);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void editSorteio(Sorteio sorteio) {
        Intent intent = new Intent(SorteioListActivity.this, SorteioEditActivity.class);
        intent.putExtra(Constantes.ID_INSERT_UPDATE, sorteio.getId());
        intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
        startActivityForResult(intent, Constantes.INSERT_UPDATE);
    }

    private void deleteSorteio(Sorteio sorteio) {
        DialogBox dialogBox = new DialogBox(this,
                DialogBox.DialogBoxType.QUESTION,
                getResources().getString(R.string.msg_delete_registro),
                getResources().getString(R.string.label_numero_concurso) + sorteio.getNumero(),
                new OnClickYesDialog(sorteio),
                new OnClickNoDialog());
        dialogBox.show();
    }

    private class OnClickYesDialog implements DialogInterface.OnClickListener {
        private Sorteio sorteio;
        public OnClickYesDialog(Sorteio sorteio) {
            this.sorteio = sorteio;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);
                if (sorteioDAO != null) {
                    sorteioDAO.delete(sorteio);
                }
                listViewSorteio.setAdapter(getSorteioListAdapter(typeSorteio));
            } catch (SQLiteException e) {
                new DialogBox(SorteioListActivity.this, DialogBox.DialogBoxType.INFORMATION, "Error", e.getMessage()).show();
            }
        }
    }

    private class OnClickNoDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constantes.INSERT_UPDATE && resultCode == RESULT_OK) {
            listViewSorteio.setAdapter(getSorteioListAdapter(typeSorteio));
        }
    }
}
