package br.com.daciosoftware.loteriasdms.configuracoes.webservice;

import java.io.Serializable;

/**
 * Created by fdacio on 01/09/16.
 */
public class WebService implements Serializable {

    private String urlWebService;

    public WebService(String urlWebService) {
        this.urlWebService = urlWebService;
    }

    public String getUrlWebService() {
        return urlWebService;
    }

    public void setUrlWebService(String urlWebService) {
        this.urlWebService = urlWebService;
    }
}
