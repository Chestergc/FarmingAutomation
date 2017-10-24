package org.senai.mecatronica.dripper.helpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import org.senai.mecatronica.dripper.activities.GraphsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Felipe on 24/10/2017.
 */

public class DateOperations {

    private static DatePickerDialog datePicker;

    public static void setDate(Context context, final TextView text){

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
                        text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },mYear, mMonth, mDay);
        datePicker.show();
    }

    public static String getDateWithOffset(int offset){
        String date;

        //get first date with offset days from current
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -offset);
        date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());

        return date;
    }
}
