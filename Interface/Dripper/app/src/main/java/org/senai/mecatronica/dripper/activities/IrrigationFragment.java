package org.senai.mecatronica.dripper.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.senai.mecatronica.dripper.R;

/**
 * Setup irrigation parameters
 *
 * Manual:
 * Setup a timer and start the irrigation process
 *
 * Programmed:
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
        return inflater.inflate(R.layout.irrigation_layout, container, false);
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
