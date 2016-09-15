package br.com.daciosoftware.loteriasdms.configuracoes.urls;

import java.io.Serializable;

import br.com.daciosoftware.loteriasdms.TypeSorteio;

/**
 * Created by fdacio on 28/08/16.
 */
public class Urls implements Serializable {
    private TypeSorteio typeSorteio;
    private String header;
    private String urlResultado;
    private String urlArquivo;

    public Urls(TypeSorteio typeSorteio, String header, String urlResultado, String urlArquivo) {
        this.typeSorteio = typeSorteio;
        this.header = header;
        this.urlResultado = urlResultado;
        this.urlArquivo = urlArquivo;
    }

    public TypeSorteio getTypeSorteio() {
        return typeSorteio;
    }

    public String getHeader() {
        return header;
    }

    public String getUrlResultado() {
        return urlResultado;
    }

    public String getUrlArquivo() {
        return urlArquivo;
    }

    public void setUrlResultado(String urlResultado) {
        this.urlResultado = urlResultado;
    }

    public void setUrlArquivo(String urlArquivo) {
        this.urlArquivo = urlArquivo;
    }
}
