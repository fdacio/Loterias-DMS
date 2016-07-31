package br.com.daciosoftware.loteriasdms;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

    /**
     * Estiliza um active conforme Tipo de Jogo
     *
     * @param typeSorteio
     */
    public void setStyleInViews(TypeSorteio typeSorteio) {
        setStyleHeader(typeSorteio);
        setStyleToolbar(typeSorteio);
        setStyleButton(typeSorteio);
        setStyleFloatingActionButton(typeSorteio);
        setStyleImageButton(typeSorteio);
    }

    /**
     * Estiliza o SecundaryMenu. É chamado no adapter do secundary menu
     *
     * @param typeSorteio
     * @param positionMenu
     */
    public void setStyleMenu(TypeSorteio typeSorteio, int positionMenu) {
        ImageView imageViewIconMenu = (ImageView) layoutActivity.findViewById(R.id.imageViewIconMenu);
        TextView textViewLabelMenu = (TextView) layoutActivity.findViewById(R.id.textViewLabelMenu);

        if (imageViewIconMenu != null) {
            imageViewIconMenu.setImageResource(getIconMenu(typeSorteio, positionMenu));
        }
        if (textViewLabelMenu != null) {
            textViewLabelMenu.setTextColor(ColorStateList.valueOf(getColorRgb(typeSorteio)));
        }
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
                return Color.rgb(34, 149, 81);
            case LOTOFACIL:
                return Color.rgb(124, 11, 137);
            case QUINA:
                return Color.rgb(41, 11, 137);
            default:
                return Color.rgb(63, 81, 181);
        }
    }

    private int getTexto(TypeSorteio typeSorteio) {
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


    private int getBackgroundButton(TypeSorteio typeSorteio) {
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

    private void setStyleHeader(TypeSorteio typeSorteio) {
        ImageView imageViewIconJogo = (ImageView) layoutActivity.findViewById(R.id.imageViewIconJogo);
        TextView textViewLabelJogo = (TextView) layoutActivity.findViewById(R.id.textViewLabelJogo);
        if (imageViewIconJogo != null) {
            imageViewIconJogo.setImageResource(getIcon(typeSorteio));
        }
        if (textViewLabelJogo != null) {
            textViewLabelJogo.setText(getTexto(typeSorteio));
            textViewLabelJogo.setTextColor(ColorStateList.valueOf(getColorRgb(typeSorteio)));
        }
    }

    private void setStyleFloatingActionButton(TypeSorteio typeSorteio) {
        ArrayList<FloatingActionButton> floatinbActionsButtons = getFloatingActionButton();
        for (FloatingActionButton fab : floatinbActionsButtons) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getColorRgb(typeSorteio)));
        }
    }

    private void setStyleButton(TypeSorteio typeSorteio) {
        ArrayList<Button> buttons = getButtons();
        for (Button button : buttons) {
            button.setBackgroundResource(getBackgroundButton(typeSorteio));
            button.setTextColor(ColorStateList.valueOf(Color.WHITE));
        }
    }


    private void setStyleImageButton(TypeSorteio typeSorteio) {
        ArrayList<ImageButton> imageButtons = getImageButtons();
        for (ImageButton imageButton : imageButtons) {
            imageButton.setBackgroundResource(getBackgroundButton(typeSorteio));
        }
    }

    private void setStyleToolbar(TypeSorteio typeSorteio) {
        Toolbar toolbar = (Toolbar) layoutActivity.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundResource(getColor(typeSorteio));
        }
        /**
         * Para toolbar interna(Botton)
         */
        ArrayList<Toolbar> toolbars = getToolbars();
        for (Toolbar toolbarInGroupView : toolbars) {
            toolbarInGroupView.setBackgroundResource(getColor(typeSorteio));
        }
    }


    private ArrayList<Button> getButtons() {
        ArrayList<Button> buttons = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) layoutActivity.getParent();
        findButtons(viewGroup, buttons);
        return buttons;
    }


    private static void findButtons(ViewGroup viewGroup, ArrayList<Button> buttons) {
        for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                findButtons((ViewGroup) child, buttons);
            } else if (child instanceof Button) {
                buttons.add((Button) child);
            }
        }
    }

    private ArrayList<ImageButton> getImageButtons() {
        ArrayList<ImageButton> imageButtons = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) layoutActivity.getParent();
        findImageButtons(viewGroup, imageButtons);
        return imageButtons;
    }


    private static void findImageButtons(ViewGroup viewGroup, ArrayList<ImageButton> imageButtons) {
        for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                findImageButtons((ViewGroup) child, imageButtons);
            } else if (child instanceof Button) {
                imageButtons.add((ImageButton) child);
            }
        }
    }


    private ArrayList<FloatingActionButton> getFloatingActionButton() {
        ArrayList<FloatingActionButton> floatinbActionsButtons = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) layoutActivity.getParent();
        findFloatingActionButton(viewGroup, floatinbActionsButtons);
        return floatinbActionsButtons;
    }

    private static void findFloatingActionButton(ViewGroup viewGroup, ArrayList<FloatingActionButton> buttons) {
        for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                findFloatingActionButton((ViewGroup) child, buttons);
            } else if (child instanceof FloatingActionButton) {
                buttons.add((FloatingActionButton) child);
            }
        }
    }

    private ArrayList<Toolbar> getToolbars() {
        ArrayList<Toolbar> toolbars = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) layoutActivity.getParent();
        findToolbars(viewGroup, toolbars);
        return toolbars;
    }

    private static void findToolbars(ViewGroup viewGroup, ArrayList<Toolbar> toolbars) {
        for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                findToolbars((ViewGroup) child, toolbars);
            } else if (child instanceof Toolbar) {
                toolbars.add((Toolbar) child);
            }
        }
    }


}
