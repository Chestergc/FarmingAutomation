package org.senai.mecatronica.dripper.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.senai.mecatronica.dripper.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GraphsActivity extends AppCompatActivity {

    TextView textDateFrom;
    TextView textDateTo;
    Button btnOpenCalendarFrom;
    Button btnOpenCalendarTo;
    DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_screen);

        //initiate the date picker
        textDateFrom = (TextView) findViewById(R.id.textDateFrom);
        textDateTo = (TextView) findViewById(R.id.textDateTo);
        btnOpenCalendarFrom = (Button) findViewById(R.id.btnOpenCalendarFrom);
        btnOpenCalendarTo = (Button) findViewById(R.id.btnOpenCalendarTo);

        String dateFrom = getDateWithOffset(15);
        String dateTo = getDateWithOffset(0);

        textDateFrom.setText(dateFrom);
        textDateTo.setText(dateTo);

        //calendar button click actions
        btnOpenCalendarFrom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setDate(textDateFrom);
            }
        });

        btnOpenCalendarTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setDate(textDateTo);
            }
        });
    }


    private void setDate(final TextView text){

        //get current date as a calendar instance
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        //opens a new date picker
        datePicker = new DatePickerDialog(GraphsActivity.this,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },mYear, mMonth, mDay);
        datePicker.show();
    }

    private String getDateWithOffset(int offset){
        String date;

        //get first date with offset days from current
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -offset);
        date = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());

        return date;
    }
}
