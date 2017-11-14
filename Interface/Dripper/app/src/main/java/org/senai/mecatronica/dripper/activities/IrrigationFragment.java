package org.senai.mecatronica.dripper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.adapters.SchedulesAdapter;
import org.senai.mecatronica.dripper.beans.IrrigationData;
import org.senai.mecatronica.dripper.managers.DataManager;

import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

/**
 * Setup irrigation parameters
 *
 * Scheduled:
 * Setup a time frame between irrigation processes and their duration
 *
 * Automatic:
 * Irrigation based on sensor data
 *
 */

public class IrrigationFragment extends Fragment implements IrrigationEditor{

    //fragment data labels
//    private static final String AUTO_MODE = "arg_autoMode";
//    private static final String IRRIGATION_DATA = "arg_irrigationData";
//    private static final String IRRIGATION_DATA_ITEM = "arg_irrigationData";
//    private static final String IRRIGATION_DATA_PREFS = "prefs_irrigation_data";

    //extras labels for intent
    private static final String EXTRA_ONETIME = "extra_mode";
    private static final String EXTRA_DATE = "extra_date";
    private static final String EXTRA_TIME = "extra_time";
    private static final String EXTRA_DURATION = "extra_duration_s";
    private static final String EXTRA_WEEKDAYS = "extra_weekdays";
    private static final String EXTRA_ID = "extra_id";

    private static final Integer ADD_CODE = 1;
    private static final Integer EDIT_CODE = 2;

    //temporary variables relative to final
//    private Boolean isAuto;
//    private List<IrrigationData> irrigationDataList;

    //view elements
    //private View mContent;

    private Switch switchAutoMode;
    private FloatingActionButton btnAddIrrigation;
    private View grayOutArea;
    private ListView irrigationListView;

    public static Fragment newInstance() {

        //instantiate fragment and add parameters to final variables
        Fragment frag = new IrrigationFragment();
//        Bundle args = new Bundle();
//        Bundle irrigationDataBundle = new Bundle();
//
//        for(IrrigationData data : irrigationDataList){
//            irrigationDataBundle.putSerializable(IRRIGATION_DATA_ITEM + "_" + data.getId(), data);
//        }
//
//        args.putBoolean(AUTO_MODE, isAuto);
//        args.putBundle(IRRIGATION_DATA, irrigationDataBundle);
//
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_irrigation, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //reset irrigation data
//        irrigationDataList = new ArrayList<>();

        // retrieve data from bundle or savedInstanceState
//        if (savedInstanceState == null) {
//            Bundle args = getArguments();
//            this.isAuto = args.getBoolean(AUTO_MODE);
//            Bundle list = args.getBundle(IRRIGATION_DATA);
//            for(int i = 0; i < list.size(); i++){
//                irrigationDataList.add((IrrigationData) list.get(IRRIGATION_DATA_ITEM + "_" + i));
//            }
//
//        } else {
//            //get data from savedInstanceState -> savedInstanceState.getData(DATA);
//            this.isAuto = savedInstanceState.getBoolean(AUTO_MODE);
//            Bundle list = savedInstanceState.getBundle(IRRIGATION_DATA);
//            for(int i = 0; i < list.size(); i++){
//                irrigationDataList.add((IrrigationData) list.get(IRRIGATION_DATA_ITEM + "_" + i));
//            }
//        }

        // initialize view elements (textView, images...)
        //viewElement = view.getViewById(R.id.viewElement)

        switchAutoMode = (Switch) view.findViewById(R.id.switch_auto_mode);
        btnAddIrrigation = (FloatingActionButton) view.findViewById(R.id.act_btn_add_irrigation);
        grayOutArea = view.findViewById(R.id.listview_schedules_grayout);
        irrigationListView = (ListView) view.findViewById(R.id.listview_schedules);

        // set stuff to view elements (clickers, text, colors)

        //add irrigation button click listener
        btnAddIrrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start add irrigation activity with intent
                addNewIrrigation();
            }
        });

        //switch auto mode listener
        switchAutoMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    grayOutArea.setVisibility(View.VISIBLE);
                    grayOutArea.setAlpha(0.5f);
                } else {
                    grayOutArea.setVisibility(View.GONE);
                    grayOutArea.setAlpha(0);
                }
                //update database
                DataManager.getInstance(getContext()).setAutoMode(isChecked);

                //temporary remove all irrigation data
                DataManager.getInstance(getContext()).clearIrrigationData();
                updateIrrigationListView();

            }
        });

        //retrieve auto mode state
        switchAutoMode.setChecked(DataManager.getInstance(getContext()).getAutoMode());

        //populate list of schedules taken from db
        updateIrrigationListView();

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//
//        //update state with temp variables
//        List<IrrigationData> irrigationDataList = new ArrayList<>();
//        Bundle irrigationDataBundle = new Bundle();
//        for(IrrigationData data : irrigationDataList){
//            irrigationDataBundle.putSerializable(IRRIGATION_DATA_ITEM + "_" + data.getId(), data);
//        }
//        outState.putBoolean(AUTO_MODE, isAuto);
//        outState.putBundle(IRRIGATION_DATA, irrigationDataBundle);
//
//        super.onSaveInstanceState(outState);
//    }

    private void updateIrrigationListView(){
        //set schedules to listview through custom adapter
        SchedulesAdapter adapter = new SchedulesAdapter(this.getContext(),
                DataManager.getInstance(getContext()).getIrrigationDataList(), this);
        irrigationListView.setAdapter(adapter);
    }

    private void addNewIrrigation(){
        Intent intent = new Intent(this.getContext(), EditIrrigationActivity.class);

        //TODO get current time and date to populate new irrigation
        String currentTime = "00:00:00";
        String currentDate = "01/01/00";
        int duration[] = {0,0,0};
        boolean weekdays[] = {false,false,false,false,false,false,false};

        intent.putExtra(EXTRA_ONETIME, 1);
        intent.putExtra(EXTRA_TIME, currentTime);
        intent.putExtra(EXTRA_DURATION, duration);
        intent.putExtra(EXTRA_DATE, currentDate);
        intent.putExtra(EXTRA_WEEKDAYS, weekdays);
        startActivityForResult(intent, ADD_CODE);
    }

    public void editIrrigation(int id){
        Intent intent = new Intent(this.getContext(), EditIrrigationActivity.class);

        //TODO get status from edit call
        String currentTime = "00:00:00";
        String currentDate = "01/01/00";
        int duration[] = {0,0,0};
        boolean weekdays[] = {false,false,false,false,false,false,false};

        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_ONETIME, 1);
        intent.putExtra(EXTRA_TIME, currentTime);
        intent.putExtra(EXTRA_DURATION, duration);
        intent.putExtra(EXTRA_DATE, currentDate);
        intent.putExtra(EXTRA_WEEKDAYS, weekdays);
        startActivityForResult(intent, EDIT_CODE);
    }

    public void deleteIrrigation(int id){
        DataManager.getInstance(getContext()).removeIrrigationData(id);
        updateIrrigationListView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            //on return add new element to irrigation data list
            IrrigationData iData = new IrrigationData();
//            iData.setOneTime(false);
//            iData.setDuration(new ArrayList<>(Arrays.asList(0,0,30)));
//            iData.setStartDate("11/02/13");
//            iData.setStartTime("12:40:00");
//            iData.setWeekDay("Monday", true);
//            iData.setWeekDay("Wednesday", true);
//            iData.setWeekDay("Friday", true);

            iData.setOneTime(data.getBooleanExtra(EXTRA_ONETIME, false));
            iData.setDuration(data.getIntegerArrayListExtra(EXTRA_DURATION));
            iData.setStartDate(data.getStringExtra(EXTRA_DATE));
            iData.setStartTime(data.getStringExtra(EXTRA_TIME));
            iData.setWeekDays(data.getBooleanArrayExtra(EXTRA_WEEKDAYS));
            int id = data.getIntExtra(EXTRA_ID, 0);

            if(requestCode == ADD_CODE){
                //TODO set unique id for every add
                //update database
                DataManager.getInstance(getContext()).addIrrigationData(iData);
            }else if(requestCode == EDIT_CODE){
                //TODO get id from intent and replace data in list
                DataManager.getInstance(getContext()).replaceIrrigationData(id, iData);
            }
        }

        updateIrrigationListView();
    }

    public static String getExtraOnetime() {
        return EXTRA_ONETIME;
    }

    public static String getExtraDate() {
        return EXTRA_DATE;
    }

    public static String getExtraTime() {
        return EXTRA_TIME;
    }

    public static String getExtraDuration() {
        return EXTRA_DURATION;
    }

    public static String getExtraWeekdays() {
        return EXTRA_WEEKDAYS;
    }

    public static String getExtraId(){
        return EXTRA_ID;
    }
}
