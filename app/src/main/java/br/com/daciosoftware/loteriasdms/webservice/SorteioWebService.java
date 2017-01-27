package br.com.daciosoftware.loteriasdms.webservice;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAO;
import br.com.daciosoftware.loteriasdms.dao.SorteioDAOFactory;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.pojo.SorteioFactory;
import br.com.daciosoftware.loteriasdms.util.Constantes;
import br.com.daciosoftware.loteriasdms.util.HttpConnection;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by fdacio on 14/10/16.
 */
public class SorteioWebService {

    private Context context;


    public SorteioWebService(Context context) {
        this.context = context;
    }

    private String getUrlRoot() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREF, Context.MODE_PRIVATE);
        String urlRoot = sharedPreferences.getString(Constantes.URL_WEB_SERVICE, Constantes.URL_WEB_SERVICE_DEFAULT);
        return urlRoot;
    }

    private String getCodigoSorteio(TypeSorteio typeSorteio) {

        switch (typeSorteio) {
            case MEGASENA:
                return "1";
            case QUINA:
                return "2";
            case LOTOFACIL:
                return "3";
            default:
                return null;
        }
    }

    private JSONObject getJSONObjectFromUrl(String url) throws Exception {

        String jsonWebService = HttpConnection.getContentJSON(url);
        JSONObject jsonObject = new JSONObject(jsonWebService);

        String status = jsonObject.getString("Status");
        if (status.equals("end")) {
            return null;
        } else {
            return jsonObject;
        }
    }


    private Sorteio getSorteioFromJSONObject(TypeSorteio typeSorteio, JSONObject jsonObject) throws Exception {

        int numero = jsonObject.getInt("NumeroConcurso");
        Calendar data = MyDateUtil.dateUSToCalendar(jsonObject.getString("Data"));
        String local = jsonObject.getString("RealizadoEm");
        JSONArray jsonArray = jsonObject.optJSONArray("Sorteios");
        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
        String numeros = jsonObject2.getString("Numeros");
        String[] dezenasWS = numeros.replace("[", "").replace("]", "").split(",");
        int[] dezenas = new int[dezenasWS.length];
        for (int i = 0; i < dezenas.length; i++) {
            dezenas[i] = Integer.parseInt(dezenasWS[i]);
        }

        Sorteio sorteio = SorteioFactory.getInstance(typeSorteio);
        sorteio.setNumero(numero);
        sorteio.setData(data);
        sorteio.setLocal(local);
        sorteio.setDezenas(dezenas);

        return sorteio;

    }

    public Sorteio getUltimoSorteioFromWebService(TypeSorteio typeSorteio) throws Exception {
        String urlWebServiceSorteio = getUrlRoot() + getCodigoSorteio(typeSorteio);

        JSONObject jsonObject = getJSONObjectFromUrl(urlWebServiceSorteio);

        if (jsonObject != null) {
            return getSorteioFromJSONObject(typeSorteio, jsonObject);
        } else {
            return null;
        }
    }

    public Sorteio getSorteioFromWebService(TypeSorteio typeSorteio, int numeroSorteio) throws Exception {
        String urlWebServiceSorteio = getUrlRoot() + getCodigoSorteio(typeSorteio) + "/" + numeroSorteio;

        JSONObject jsonObject = getJSONObjectFromUrl(urlWebServiceSorteio);

        if (jsonObject != null) {
            return getSorteioFromJSONObject(typeSorteio, jsonObject);
        } else {
            return null;
        }
    }

    public List<Sorteio> getSorteiosAtualizar() throws Exception {

        List<Sorteio> listaSorteio = new ArrayList<>();

        for (TypeSorteio typeSorteio : TypeSorteio.values()) {

            SorteioDAO sorteioDAO = SorteioDAOFactory.getInstance(context, typeSorteio);
            Sorteio sorteioLastDB = sorteioDAO.findLast();
            Sorteio sorteioWeb = getUltimoSorteioFromWebService(typeSorteio);

            if ((sorteioLastDB != null) && (sorteioWeb != null)) {
                if ((sorteioWeb.getNumero() > sorteioLastDB.getNumero())) {
                    listaSorteio.add(sorteioWeb);
                }
            } else if (sorteioWeb != null) {
                listaSorteio.add(sorteioWeb);
            }
        }

        return listaSorteio;
    }
}