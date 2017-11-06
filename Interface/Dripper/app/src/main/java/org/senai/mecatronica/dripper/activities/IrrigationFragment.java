package org.senai.mecatronica.dripper.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.adapters.SchedulesAdapter;
import org.senai.mecatronica.dripper.beans.IrrigationData;

import java.util.ArrayList;
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

    //fragment final variables
    //private static final String ARG_PARAMETER = "arg_parameter";

    //temporary variables relative to final
    //private Object tempParameter;

    //view elements
    //private View mContent;

    private Switch switchAutoMode;
    private FloatingActionButton btnAddIrrigation;

    public static Fragment newInstance() {
        //instantiate fragment and add parameters to final variables
        Fragment frag = new IrrigationFragment();
        Bundle args = new Bundle();
        //args.putParameter(VARIABLE, parameter)

        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_irrigation, container, false);

        switchAutoMode = (Switch) view.findViewById(R.id.switch_auto_mode);
        btnAddIrrigation = (FloatingActionButton) view.findViewById(R.id.act_btn_add_irrigation);

        btnAddIrrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start add irrigation activity
            }
        });

        //populate list of schedules taken from db
        ListView irrigationListView = (ListView) view.findViewById(R.id.listview_schedules);
        List<IrrigationData> irrigationDataList = new ArrayList<>();

        IrrigationData data = new IrrigationData();
        data.setOneTime(false);
        data.setDuration(10);
        data.setStart("15:30:05");
        data.addWeekDay("Monday");
        data.addWeekDay("Wednesday");
        data.addWeekDay("Friday");

        irrigationDataList.add(data);

        //set schedules to listview through custom adapter
        SchedulesAdapter adapter = new SchedulesAdapter(this.getContext(), irrigationDataList);
        irrigationListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve data from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            //get data from bundle -> args.getData(DATA);

        } else {
            //get data from savedInstanceState -> savedInstanceState.getData(DATA);
        }

        // initialize view elements (textView, images...)
        //viewElement = view.getViewById(R.id.viewElement)

        // set stuff to view elements (clickers, text, colors)
        //viewElement.setStuff(Stuff);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //update state with temp variables
        //outState.putObject(ARG_PARAMETER, tempParameter);
        //*for string outState.putString(ARG_TEXT, text);
        super.onSaveInstanceState(outState);
    }

}
