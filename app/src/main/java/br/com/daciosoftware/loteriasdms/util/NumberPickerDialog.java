package br.com.daciosoftware.loteriasdms.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;


/**
 * Created by Dácio Braga on 12/08/2016.
 */
public class NumberPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private final NumberPicker mNumberPicker;
    private final OnNumberSetListener mNumberSetListener;


    public interface OnNumberSetListener {
        void onNumberSet(int number);
    }

    /**
     *
     * @param context - Contexto da Aplicação
     * @param layoutInflater - Layout contendo um widget numberpicker
     * @param callBack - implementaccao que registrar o retorno do numberpiker
     * @param title - Título para o AlertDialog
     * @param startValue - valor inicial para o numberpicker
     * @param mimValue - menor valor possivel para o numberpicker
     * @param maxValue - maior valor possível para o numberpiker
     */
    public NumberPickerDialog(Context context, int layoutInflater, OnNumberSetListener callBack, String title, int startValue, int mimValue, int maxValue) {
        this(context, layoutInflater, 0, callBack, title, startValue, mimValue, maxValue);
    }


    static int resolveDialogTheme(Context context, int resid) {
        if (resid == 0) {
            final TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.style.Theme_Holo, outValue, true);
            return outValue.resourceId;
        } else {
            return resid;
        }
    }

    /**
     *
     * @param context - Contexto da Aplicação
     * @param layoutInflater - Layout contendo um widget numberpicker
     * @param theme - tema para oa alertdialog - Padrão Theme.HOLO
     * @param listener - implementaccao que registrar o retorno do numberpiker
     * @param title - Título para o AlertDialog
     * @param startValue - valor inicial para o numberpicker
     * @param mimValue - menor valor possivel para o numberpicker
     * @param maxValue - maior valor possível para o numberpiker
     */

    public NumberPickerDialog(Context context, int layoutInflater, int theme, OnNumberSetListener listener, String title, int startValue, int mimValue, int maxValue) {
        super(context, resolveDialogTheme(context, theme));


        mNumberSetListener = listener;
        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(layoutInflater, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(android.R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(android.R.string.cancel), this);
        setTitle(title);

        mNumberPicker = getNumberPickerInViewGroup((ViewGroup)view);
        if(mNumberPicker != null) {
            mNumberPicker.setMinValue(mimValue);
            mNumberPicker.setMaxValue(maxValue);
            mNumberPicker.setValue(startValue);
            mNumberPicker.setWrapSelectorWheel(false);
         }
    }

    private NumberPicker getNumberPickerInViewGroup(ViewGroup viewGroup){
        for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof NumberPicker) {
                return (NumberPicker)child;
            }
        }
        return null;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case BUTTON_POSITIVE:
                if (mNumberSetListener != null) {
                    mNumberPicker.clearFocus();
                    mNumberSetListener.onNumberSet(mNumberPicker.getValue());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }


    }

}
