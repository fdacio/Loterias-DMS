package br.com.daciosoftware.loteriasdms.configuracoes;

/**
 * Created by fdacio on 21/01/17.
 */
public abstract class UrlDAO {

    public abstract String getUrl();

    public abstract void save(String url);

    public abstract String getHeader();

    public abstract String getLabel();

    public abstract void saveDefaulValues();

}
