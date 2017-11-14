package org.senai.mecatronica.dripper.helpers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.senai.mecatronica.dripper.activities.GraphsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Felipe on 24/10/2017.
 */

public class DateOperations {

    private static DatePickerDialog datePicker;
    private static TimePickerDialog timePicker;

    public static void setDate(Context context, final EditText text){

        //get current date as a calendar instance
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        //opens a new date picker
        datePicker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        year = year%100;
                        text.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear+1, year));
                    }
                },mYear, mMonth, mDay);
        datePicker.show();
    }

    public static void setTime(Context context, final EditText text, String currentTime){

        final Calendar c = Calendar.getInstance();

        try {
            Date date = new SimpleDateFormat("h:mm a", Locale.getDefault()).parse(currentTime);
            c.setTime(date);
        } catch (ParseException e){
            e.printStackTrace();
        }

        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                boolean isPM = (hourOfDay >= 12);
                text.setText(String.format(Locale.getDefault(), "%01d:%02d %s",
                        (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
            }
        }, hourOfDay, minute, false);
        timePicker.show();
    }

    public static String getDateWithOffset(int offset){
        String date;

        //get first date with offset days from current
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -offset);
        date = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(c.getTime());

        return date;
    }

    public static String getCurrentTime(){
        String time;
        Calendar c = Calendar.getInstance();
        time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(c.getTime());
        return time;
    }

    public static String getCurrentDate(){
        String date;
        Calendar c = Calendar.getInstance();
        date = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(c.getTime());
        return date;
    }

}
