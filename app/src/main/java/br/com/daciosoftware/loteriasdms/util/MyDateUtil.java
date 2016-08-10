package br.com.daciosoftware.loteriasdms.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class MyDateUtil {

    private MyDateUtil(){}

    public static Calendar dateBrToCalendar(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = sdf.parse(data);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar dateUSToCalendar(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = sdf.parse(data);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar dateShortBrToCalendar(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy", Locale.getDefault());
        Date date = sdf.parse(data);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String calendarToDateBr(Calendar data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(data.getTime());
    }

    public static String calendarToDateUS(Calendar data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(data.getTime());
    }


    public static String timeToString(){
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault());
        Calendar data = Calendar.getInstance();
        return sdf.format(data.getTime());
    }

    public static String calendarToShortDateBr(Calendar data) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy", Locale.getDefault());
        return sdf.format(data.getTime());
    }

}
