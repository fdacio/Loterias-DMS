package br.com.daciosoftware.loteriasdms.configuracoes;

/**
 * Created by fdacio on 01/02/17.
 */
public abstract class ValorApostaDAO {

    public abstract float getValor();

    public abstract void save(float valor);

    public abstract String getHeader();

    public abstract String getLabel();

    public abstract void saveDefaulValues();

}
