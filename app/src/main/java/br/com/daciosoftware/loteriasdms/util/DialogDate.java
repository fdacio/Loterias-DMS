package br.com.daciosoftware.loteriasdms.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dácio Braga on 21/07/2016.
 * Classe pra chamar um datepicker e setar o valor da data na view informada no construtor;
 */
public class DialogDate {

    private TextView textView;

    /**
     * @param textView View que receberá a data vinda do DatePicker
     *                 Parametros aceitaveis do tipo TextView, EditText, Button
     */
    public DialogDate(TextView textView) {
        this.textView = textView;
    }

    public void show() {
        if (textView != null) {
            int dia;
            int mes;
            int ano;

            try {
                Calendar dateOfTextView = dateBrToCalendar(textView.getText().toString());
                dia = dateOfTextView.get(Calendar.DAY_OF_MONTH);
                mes = dateOfTextView.get(Calendar.MONTH);
                ano = dateOfTextView.get(Calendar.YEAR);

            } catch (ParseException e) {
                Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                ano = calendar.get(Calendar.YEAR);
            }

            Dialog datePickerDialog = new DatePickerDialog(textView.getContext(), new OnDateSetSorteioListener(), ano, mes, dia);
            datePickerDialog.show();
        }
    }

    private class OnDateSetSorteioListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            textView.setText(calendarToDateBr(calendar));
        }
    }


    private Calendar dateBrToCalendar(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = sdf.parse(data);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    private String calendarToDateBr(Calendar data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(data.getTime());
    }

}