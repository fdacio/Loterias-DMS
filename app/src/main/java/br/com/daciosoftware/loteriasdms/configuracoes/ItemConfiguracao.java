package br.com.daciosoftware.loteriasdms.configuracoes;

/**
 * Created by fdacio on 21/01/17.
 */
public class ItemConfiguracao {

    private String label;
    private String sublabel;

    public ItemConfiguracao() {
    }

    public ItemConfiguracao(String label, String sublabel) {
        this.label = label;
        this.sublabel = sublabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSublabel() {
        return sublabel;
    }

    public void setSublabel(String sublabel) {
        this.sublabel = sublabel;
    }
}
