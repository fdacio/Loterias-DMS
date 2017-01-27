package br.com.daciosoftware.loteriasdms.configuracoes.valoraposta;

import br.com.daciosoftware.loteriasdms.TypeSorteio;

/**
 * Created by fdacio on 03/09/16.
 */
public class ValorAposta {

    private TypeSorteio typeSorteio;
    private int qtdeDezenas;
    private float valorAposta;

    public ValorAposta(TypeSorteio typeSorteio, int qtdeDezenas, float valorAposta) {
        this.typeSorteio = typeSorteio;
        this.qtdeDezenas = qtdeDezenas;
        this.valorAposta = valorAposta;
    }

    public TypeSorteio getTypeSorteio() {
        return typeSorteio;
    }

    public int getQtdeDezenas() {
        return qtdeDezenas;
    }

    public float getValorAposta() {
        return valorAposta;
    }
}

