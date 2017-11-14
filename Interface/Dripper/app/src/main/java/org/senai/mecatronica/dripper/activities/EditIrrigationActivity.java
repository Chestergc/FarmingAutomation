package org.senai.mecatronica.dripper.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.senai.mecatronica.dripper.R;

import java.util.ArrayList;
import java.util.List;

public class EditIrrigationActivity extends AppCompatActivity {

    private Button btnCancel;
    private Button btnSave;
    private Spinner chooseMode;
    private EditText editDate;
    private EditText editTime;
    private TextView labelDate;
    private List<EditText> editDuration;
    private LinearLayout weekDaysLayout;
    private List<TextView> weekDays;

    private Boolean oneTime;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_irrigation);

        //call initialization methods
        initializeViewElements();
        Intent intent = getIntent();
        setupStartingParameters(intent);
        setupClickListeners();
    }

    /**
     * Initialize variables for each view element
     * */
    private void initializeViewElements(){
        btnCancel = (Button) findViewById(R.id.btn_edit_menu_cancel);
        btnSave = (Button) findViewById(R.id.btn_edit_menu_save);
        chooseMode = (Spinner) findViewById(R.id.spinner_edit_menu_mode);
        labelDate = (TextView) findViewById(R.id.lbl_edit_menu_date);
        editDate = (EditText) findViewById(R.id.date_edit_menu);
        editTime = (EditText) findViewById(R.id.txt_edit_menu_time);
        editDuration = new ArrayList<>();
        editDuration.add((EditText) findViewById(R.id.num_edit_menu_hours));
        editDuration.add((EditText) findViewById(R.id.num_edit_menu_minutes));
        editDuration.add((EditText) findViewById(R.id.num_edit_menu_seconds));
        weekDaysLayout = (LinearLayout) findViewById(R.id.layout_edit_menu_weekdays);
        weekDays = new ArrayList<>();
        weekDays.add((TextView) findViewById(R.id.week_pick_sunday));
        weekDays.add((TextView) findViewById(R.id.week_pick_monday));
        weekDays.add((TextView) findViewById(R.id.week_pick_tuesday));
        weekDays.add((TextView) findViewById(R.id.week_pick_wednesday));
        weekDays.add((TextView) findViewById(R.id.week_pick_thursday));
        weekDays.add((TextView) findViewById(R.id.week_pick_friday));
        weekDays.add((TextView) findViewById(R.id.week_pick_saturday));
    }

    /**
     * Populate view with starting parameters sent through the intent
     * */
    private void setupStartingParameters(Intent parameters){
        oneTime = false;
        id = parameters.getIntExtra(IrrigationFragment.getExtraId(), 0);
        editDate.setText(parameters.getStringExtra(IrrigationFragment.getExtraDate()));
        editTime.setText(parameters.getStringExtra(IrrigationFragment.getExtraTime()));

        int durationParams[] = parameters.getIntArrayExtra(IrrigationFragment.getExtraDuration());
        for(int i = 0; i < durationParams.length; i++){
            String s = String.valueOf(durationParams[i]);
            editDuration.get(i).setText(s);
        }

        boolean week[] = parameters.getBooleanArrayExtra(IrrigationFragment.getExtraWeekdays());
        for(int i = 0; i < week.length; i++){
            if(week[i]){
                weekDays.get(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }
        }

    }

    /**
     * Setup click listeners for each element on the activity
     * */
    private void setupClickListeners(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to previous activity
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to previous activity
                if(checkIrrigationParameters()){
                    Intent resultsIntent = getResultParameters();
                    setResult(RESULT_OK, resultsIntent);
                    finish();
                }
            }
        });

        chooseMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getSelectedItem().toString();
                if(selected.equals("Periódica")){
                    editDate.setVisibility(View.GONE);
                    labelDate.setVisibility(View.GONE);
                    weekDaysLayout.setVisibility(View.VISIBLE);
                    oneTime = false;
                }else if(selected.equals("Programada")){
                    editDate.setVisibility(View.VISIBLE);
                    labelDate.setVisibility(View.VISIBLE);
                    weekDaysLayout.setVisibility(View.GONE);
                    oneTime = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * If the data is valid, takes the values inserted by the user and puts into intent
     * for sending back to the caller activity
     * */
    private Intent getResultParameters(){
        Intent intent = new Intent();
        intent.putExtra(IrrigationFragment.getExtraId(), id);
        intent.putExtra(IrrigationFragment.getExtraOnetime(), oneTime);
        intent.putExtra(IrrigationFragment.getExtraDate(), editDate.getText().toString());
        intent.putExtra(IrrigationFragment.getExtraTime(), editTime.getText().toString());
        ArrayList<Integer> durationParams = new ArrayList<>();
        for(int i = 0; i < editDuration.size(); i++){
            //hours, mins, secs
            durationParams.add(Integer.parseInt(editDuration.get(i).getText().toString()));
        }
        intent.putExtra(IrrigationFragment.getExtraDuration(), durationParams);

        boolean[] weekParams = new boolean[7];
        //TODO: implement week choice logic
        for(int i = 0; i < weekParams.length; i++){
            weekParams[i] = true;
        }
        intent.putExtra(IrrigationFragment.getExtraWeekdays(), weekParams);

        return intent;
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    /**
     * Checks if entered data is valid for sending to intent.
     * */
    private boolean checkIrrigationParameters(){
        //TODO check if time is not null
        //TODO check if duration is not 0
        //TODO check if date is not null or before current time
        //TODO check if weekday is chosen
        return true;
    }
}
