package org.senai.mecatronica.dripper.activities;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class IrrigationFragment extends Fragment {

    //fragment data labels
    private static final String AUTO_MODE = "arg_autoMode";
    private static final String IRRIGATION_DATA = "arg_irrigationData";
    private static final String IRRIGATION_DATA_ITEM = "arg_irrigationData";

    //temporary variables relative to final
    private Boolean isAuto;

    //view elements
    //private View mContent;

    private Switch switchAutoMode;
    private FloatingActionButton btnAddIrrigation;
    private View grayOutArea;
    private ListView irrigationListView;

    public static Fragment newInstance(List<IrrigationData> irrigationDataList, Boolean isAuto) {
        //instantiate fragment and add parameters to final variables
        Fragment frag = new IrrigationFragment();
        Bundle args = new Bundle();
        Bundle irrigationDataBundle = new Bundle();

        for(IrrigationData data : irrigationDataList){
            irrigationDataBundle.putSerializable(IRRIGATION_DATA_ITEM + "_" + data.getId(), data);
        }

        args.putBoolean(AUTO_MODE, isAuto);
        args.putBundle(IRRIGATION_DATA, irrigationDataBundle);

        frag.setArguments(args);
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

        List<IrrigationData> irrigationDataList = new ArrayList<>();

        // retrieve data from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            this.isAuto = args.getBoolean(AUTO_MODE);
            Bundle list = args.getBundle(IRRIGATION_DATA);
            for(int i = 0; i < list.size(); i++){
                irrigationDataList.add((IrrigationData) list.get(IRRIGATION_DATA_ITEM + "_" + i));
            }

        } else {
            //get data from savedInstanceState -> savedInstanceState.getData(DATA);
            this.isAuto = savedInstanceState.getBoolean(AUTO_MODE);
            Bundle list = savedInstanceState.getBundle(IRRIGATION_DATA);
            for(int i = 0; i < list.size(); i++){
                irrigationDataList.add((IrrigationData) list.get(IRRIGATION_DATA_ITEM + "_" + i));
            }
        }

        // initialize view elements (textView, images...)
        //viewElement = view.getViewById(R.id.viewElement)

        switchAutoMode = (Switch) view.findViewById(R.id.switch_auto_mode);
        btnAddIrrigation = (FloatingActionButton) view.findViewById(R.id.act_btn_add_irrigation);
        grayOutArea = view.findViewById(R.id.listview_schedules_grayout);
        irrigationListView = (ListView) view.findViewById(R.id.listview_schedules);

        // set stuff to view elements (clickers, text, colors)
        btnAddIrrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start add irrigation activity with intent
                //on return add new element to irrigation data list
                //update database
            }
        });

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
            }
        });

        //populate list of schedules taken from db
        IrrigationData data = new IrrigationData();
        data.setOneTime(false);
        data.setDuration(10);
        data.setStartTime("15:30:05");
        data.setWeekDays(Arrays.asList("Monday", "Wednesday", "Friday"));
        data.setId(0);

        IrrigationData data2 = new IrrigationData();
        data2.setOneTime(false);
        data2.setDuration(50);
        data2.setStartTime("15:30:10");
        data2.setWeekDays(Arrays.asList("Tuesday", "Thursday", "Saturday","Sunday"));
        data2.setId(1);

        irrigationDataList.add(data);
        irrigationDataList.add(data2);

        //set schedules to listview through custom adapter
        SchedulesAdapter adapter = new SchedulesAdapter(this.getContext(), irrigationDataList);
        irrigationListView.setAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //update state with temp variables

        List<IrrigationData> irrigationDataList = new ArrayList<>();
        Bundle irrigationDataBundle = new Bundle();
        for(IrrigationData data : irrigationDataList){
            irrigationDataBundle.putSerializable(IRRIGATION_DATA_ITEM + "_" + data.getId(), data);
        }
        outState.putBoolean(AUTO_MODE, isAuto);
        outState.putBundle(IRRIGATION_DATA, irrigationDataBundle);

        super.onSaveInstanceState(outState);
    }

}
