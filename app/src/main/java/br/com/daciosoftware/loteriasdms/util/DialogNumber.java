package br.com.daciosoftware.loteriasdms.util;

import android.widget.TextView;

/**
 * Created by Dácio Braga on 21/07/2016.
 * Classe pra chamar um datepicker e setar o valor da data na view informada no construtor;
 */
public class DialogNumber {

    private int layoutInflater;
    private TextView textView;
    private NumberPickerDialog.OnNumberSetListener numberSetListener;
    private String title = "NumberPickerDialog";
    private int startValue = 0;
    private int minValue = 0;
    private int maxValue = 1000;


    /**
     *
     * @param textView View que recebera no seu texto o numero(TextView, Button, EditText)
     * @param numberSetListener - implementacao para oa retorno do numberpicker
     * @param layoutInflater Layout contendo um widget NumberPicker
     */
    public DialogNumber(TextView textView, int layoutInflater, NumberPickerDialog.OnNumberSetListener numberSetListener ) {
        this.layoutInflater = layoutInflater;
        this.textView = textView;
        this.numberSetListener = numberSetListener;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void show() {
        new NumberPickerDialog(this.textView.getContext(),
                this.layoutInflater,
                this.numberSetListener,
                this.title,
                this.startValue,
                this.minValue,
                this.maxValue)
                .show();

    }


}


