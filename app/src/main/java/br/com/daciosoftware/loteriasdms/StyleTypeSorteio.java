package br.com.daciosoftware.loteriasdms;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dácio Braga on 18/07/2016.
 */
public class StyleTypeSorteio {
    private Context context;
    private View layoutActivity;

    public StyleTypeSorteio(Context context, View layoutActivity) {
        this.context = context;
        this.layoutActivity = layoutActivity;
    }

    private int getIcon(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return R.mipmap.ic_mega_sena;
            case LOTOFACIL:
                return R.mipmap.ic_lotafacil;
            case QUINA:
                return R.mipmap.ic_quina;
            default:
                return R.mipmap.ic_launcher;
        }
    }

    private int getColor(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return R.color.colorMegasena;
            case LOTOFACIL:
                return R.color.colorLotofacil;
            case QUINA:
                return R.color.colorQuina;
            default:
                return R.color.colorPrimary;
        }
    }

    private int getColorRgb(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return Color.rgb(34,149,81);
            case LOTOFACIL:
                return Color.rgb(124,11,137);
            case QUINA:
                return Color.rgb(41,11,137);
            default:
                return Color.rgb(63,81,181);
        }
    }

    private int getTexto(TypeSorteio typeSorteio){
        switch (typeSorteio) {
            case MEGASENA:
                return R.string.mega_sena;
            case LOTOFACIL:
                return R.string.lotofacil;
            case QUINA:
                return R.string.quina;
            default:
                return R.string.app_name;
        }
    }

    private int getIconMenu(TypeSorteio typeSorteio, int position) {
        switch (typeSorteio) {
            case MEGASENA:
                switch (position) {
                    case 0:
                        return R.drawable.ic_dezenas_mais_sorteadas_megasena_24;
                    case 1:
                        return R.drawable.ic_confira_seu_jogo_megasena_24dp;
                    case 2:
                        return R.drawable.ic_sorteios_megasena_24dp;
                    case 3:
                        return R.drawable.ic_processar_arquivo_megasena_24dp;
                }

            case LOTOFACIL:
                switch (position) {
                    case 0:
                        return R.drawable.ic_dezenas_mais_sorteadas_lotofacil_24;
                    case 1:
                        return R.drawable.ic_confira_seu_jogo_lotofacil_24dp;
                    case 2:
                        return R.drawable.ic_sorteios_lotofacil_24dp;
                    case 3:
                        return R.drawable.ic_processar_arquivo_lotofacil_24dp;
                }

            case QUINA:
                switch (position) {
                    case 0:
                        return R.drawable.ic_dezenas_mais_sorteadas_quina_24;
                    case 1:
                        return R.drawable.ic_confira_seu_jogo_quina_24dp;
                    case 2:
                        return R.drawable.ic_sorteios_quina_24dp;
                    case 3:
                        return R.drawable.ic_processar_arquivo_quina_24dp;
                }

            default:
                return R.mipmap.ic_launcher;
        }
    }


    private int getBackgroundButton(TypeSorteio typeSorteio){
        switch (typeSorteio) {
            case MEGASENA:
                return R.drawable.selector_button_megasena;
            case LOTOFACIL:
                return R.drawable.selector_button_lotofacil;
            case QUINA:
                return R.drawable.selector_button_quina;
            default:
                return R.drawable.selector_button;
        }

    }

    public void setStyleHeader(TypeSorteio typeSorteio) {
        ImageView imageViewIconJogo = (ImageView) layoutActivity.findViewById(R.id.imageViewIconJogo);
        TextView textViewLabelJogo = (TextView) layoutActivity.findViewById(R.id.textViewLabelJogo);
        Toolbar toolbar = (Toolbar) layoutActivity.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundResource(getColor(typeSorteio));
        }
        if (imageViewIconJogo != null) {
            imageViewIconJogo.setImageResource(getIcon(typeSorteio));
        }
        if (textViewLabelJogo != null) {
            textViewLabelJogo.setText(getTexto(typeSorteio));
            textViewLabelJogo.setTextColor(this.context.getResources().getColor(getColor(typeSorteio)));
        }
    }

    public void setStyleMenu(TypeSorteio typeSorteio, int positionMenu) {
        ImageView imageViewIconMenu = (ImageView) layoutActivity.findViewById(R.id.imageViewIconMenu);
        TextView textViewLabelMenu = (TextView) layoutActivity.findViewById(R.id.textViewLabelMenu);

        if(imageViewIconMenu != null){
            imageViewIconMenu.setImageResource(getIconMenu(typeSorteio,positionMenu));
        }
        if (textViewLabelMenu != null) {
            textViewLabelMenu.setTextColor(this.context.getResources().getColor(getColor(typeSorteio)));
        }

    }

    public void setStyleFloatingActionButton(TypeSorteio typeSorteio) {
        FloatingActionButton fab = (FloatingActionButton) layoutActivity.findViewById(R.id.fab);
        //FloatingActionButton fabAdd = (FloatingActionButton) layoutActivity.findViewById(R.id.fabAdd);
        if (fab != null){
            fab.setBackgroundTintList(ColorStateList.valueOf(getColorRgb(typeSorteio)));
        }
        /*
        if (fabAdd != null){
            fabAdd.setBackgroundTintList(ColorStateList.valueOf(getColorRgb(typeSorteio)));
        }
        */

    }

    public void setStyleButton(TypeSorteio typeSorteio){
        ImageButton imageButtonArquivo = (ImageButton) layoutActivity.findViewById(R.id.imageButtonArquivo);
        Button buttonBaixarProcessar = (Button) layoutActivity.findViewById(R.id.buttonProcessarArquivo);
        Button buttonBaixarArquivo = (Button) layoutActivity.findViewById(R.id.buttonBaixarArquivo);

        if(imageButtonArquivo != null){
            imageButtonArquivo.setBackgroundResource(getBackgroundButton(typeSorteio));
        }
        if(buttonBaixarProcessar != null){
            buttonBaixarProcessar.setBackgroundResource(getBackgroundButton(typeSorteio));
        }
        if(buttonBaixarArquivo != null){
            buttonBaixarArquivo.setBackgroundResource(getBackgroundButton(typeSorteio));
        }

    }

}
