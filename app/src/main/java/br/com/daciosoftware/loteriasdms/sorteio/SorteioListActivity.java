package br.com.daciosoftware.loteriasdms.sorteio;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import br.com.daciosoftware.loteriasdms.AtualizaSorteioWebServiceTask;
import br.com.daciosoftware.loteriasdms.AtualizaUltimoSorteioWebServiceTask;
import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.Sorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.DateUtil;
import br.com.daciosoftware.loteriasdms.util.DialogBox;

public class SorteioListActivity extends AppCompatActivity {

    private enum TipoListagem {CRESCENTE, DECRESCENTE, ORDENAR_DEZENAS, POR_NUMERO, POR_DATA, POR_DATAS}

    private List<Sorteio> listSorteio;
    private ListView listViewSorteio;
    private TypeSorteio typeSorteio;
    private ProgressDialog progressDialog;

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
        registerForContextMenu(listViewSorteio);

        listarSorteios(typeSorteio, TipoListagem.DECRESCENTE, null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AtualizaSorteioWebServiceTask(SorteioListActivity.this,typeSorteio).execute();

                }
            });
        }


    }

    private class Param {
        private int numero;
        private Calendar data;
        private Calendar data2;

        public Param() {
        }

        public int getNumero() {
            return numero;
        }

        public void setNumero(int numero) {
            this.numero = numero;
        }

        public Calendar getData() {
            return data;
        }

        public void setData(Calendar data) {
            this.data = data;
        }

        public Calendar getData2() {
            return data2;
        }

        public void setData2(Calendar data2) {
            this.data2 = data2;
        }
    }

    private void listarSorteios(final TypeSorteio typeSorteio, final TipoListagem tipoListagem, final Param param) {

        progressDialog = ProgressDialog.show(this, "", "Listando. Aguarde...", true, false);

        new Thread() {

            public void run() {
                SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);

                switch (tipoListagem) {
                    case CRESCENTE:
                        listSorteio = sorteioDAO.listAll();
                        break;
                    case DECRESCENTE:
                        listSorteio = sorteioDAO.listAllDecrescente();
                        break;
                    case ORDENAR_DEZENAS:
                        listSorteio = sorteioDAO.dezenasCrescente(listSorteio);
                        break;
                    case POR_NUMERO:
                        listSorteio = listarPorNumero(sorteioDAO, param.getNumero());
                        break;
                    case POR_DATA:
                        listSorteio = listarPorData(sorteioDAO, param.getData());
                        break;
                    case POR_DATAS:
                        listSorteio = listarPorData(sorteioDAO, param.getData(), param.getData2());
                        break;
                }

                Message msg = new Message();
                msg.what = 100;
                handlerListSorteio.sendMessage(msg);
            }
        }.start();



    }

    private Handler handlerListSorteio = new Handler(){

        @Override
        public void handleMessage(Message msg){

            if(msg.what == 100) {
                listViewSorteio.setAdapter(new SorteioListAdapter(SorteioListActivity.this, listSorteio, typeSorteio));
            }

            progressDialog.dismiss();
        }
    };

    private List<Sorteio> listarPorNumero(SorteioDAO sorteioDAO, int numero) {
        List<Sorteio> list = new ArrayList<>();
        list.add(sorteioDAO.findByNumber(numero));
        return list;
    }

    private List<Sorteio> listarPorData(SorteioDAO sorteioDAO, Calendar date) {
        List<Sorteio> list = new ArrayList<>();
        list.add(sorteioDAO.findByDate(date));
        return list;
    }

    private List<Sorteio> listarPorData(SorteioDAO sorteioDAO, Calendar date1, Calendar date2) {
        return sorteioDAO.findByBetweenDate(date1, date2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);

        MenuItem searchItem = menu.findItem(R.id.searchMenu);
        SearchView searchView;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) searchItem.getActionView();
        } else {
            searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String searchFor) {
                    /*
                    Pesquisa pelo numero do concurso
                     */
                    try {
                        int numero = Integer.parseInt(searchFor);
                        Param param = new Param();
                        param.setNumero(numero);
                        listarSorteios(typeSorteio, TipoListagem.POR_NUMERO, param);
                    } catch (NumberFormatException nfe) {
                    }

                    /*
                    Pesquisa pela data do concurso
                     */
                    try {
                        Calendar data = DateUtil.dateBrToCalendar(searchFor);
                        Param param = new Param();
                        param.setData(data);
                        listarSorteios(typeSorteio, TipoListagem.POR_DATA, param);
                    } catch (ParseException pe) {
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

            case R.id.porOrdemCrescenteMenu:
                listarSorteios(typeSorteio, TipoListagem.CRESCENTE, null);
                return true;

            case R.id.porOrdemDecrescenteMenu:
                listarSorteios(typeSorteio, TipoListagem.DECRESCENTE, null);
                return true;

            case R.id.ordenarDezanas:
                listarSorteios(typeSorteio, TipoListagem.ORDENAR_DEZENAS, null);
                return true;

            case R.id.addMenu:
                Intent intent = new Intent(SorteioListActivity.this, SorteioEditActivity.class);
                intent.putExtra(Constantes.TYPE_SORTEIO, typeSorteio);
                startActivityForResult(intent, Constantes.INSERT_UPDATE);
                return true;

            case R.id.deleteAllMenu:
                deleteAllSorteio();
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

    private void deleteAllSorteio() {
        DialogBox dialogBox = new DialogBox(this,
                DialogBox.DialogBoxType.QUESTION,
                getResources().getString(R.string.msg_delete_registro),
                getResources().getString(R.string.msg_delete_todos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SorteioDAO sorteioDAO = SorteioDAO.getDAO(getApplicationContext(), typeSorteio);
                        sorteioDAO.deleteAll();
                        listarSorteios(typeSorteio, TipoListagem.DECRESCENTE, null);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
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
                sorteioDAO.delete(sorteio);
                listarSorteios(typeSorteio, TipoListagem.DECRESCENTE, null);
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
            listarSorteios(typeSorteio, TipoListagem.DECRESCENTE, null);
        }
    }
}
