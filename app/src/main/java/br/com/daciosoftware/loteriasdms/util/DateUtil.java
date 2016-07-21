package br.com.daciosoftware.loteriasdms.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dácio Braga on 19/07/2016.
 */
public class DateUtil {

    private DateUtil(){}


    public static Calendar dateBrToCalendar(String data) {
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }catch (ParseException pe){
            pe.printStackTrace();
            return null;
        }
    }

    public static Calendar dateUSToCalendar(String data) throws ParseException {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }catch (ParseException pe){
            pe.printStackTrace();
            return null;
        }
    }

    public static String calendarToDateBr(Calendar data){
        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        return sdf.format(data.getTime());
    }

    public static String calendarToDateUS(Calendar data){
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(data.getTime());
    }


}
