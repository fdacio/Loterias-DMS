package br.com.daciosoftware.loteriasdms.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by Dácio Braga on 21/07/2016.
 * Classe pra chamar um datepicker e setar o valor da data na view informada no construtor;
 */
public class DialogNumber {

    private Context context;
    private NumberPicker numberPicker;
    private NumberPicker.OnValueChangeListener onValueChangeListener;
    private String title = "";
    private int startValue = 0;
    private int maxValue = 1000;
    private int minValue = 0;
    private int valueReturn;


    /**
     * @param context da Aplicacao
     */
    public DialogNumber(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOnValueChangeListener(NumberPicker.OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getValueReturn() {
        return valueReturn;
    }

    public void show() {
        LayoutInflater inflater = (LayoutInflater) this.context.getApplicationContext().getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.dialog_number_picker, null);
        numberPicker = (NumberPicker) npView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        Toast.makeText(context,"startValue:"+startValue,Toast.LENGTH_SHORT).show();
        numberPicker.setValue(startValue);
        numberPicker.setWrapSelectorWheel(true);
        if (onValueChangeListener != null) {
            numberPicker.setOnValueChangedListener(onValueChangeListener);
        }

        Dialog numberPickerDialog = new AlertDialog.Builder(this.context)
                .setView(npView)
                .setTitle(title)
                .setPositiveButton(R.string.btn_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                valueReturn = numberPicker.getValue();
                                dialog.dismiss();

                            }
                        })
                .setNegativeButton(R.string.btn_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .create();

        numberPickerDialog.show();
    }
}


