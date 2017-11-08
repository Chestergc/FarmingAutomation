package org.senai.mecatronica.dripper.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.senai.mecatronica.dripper.R;

/**
 * Created by Felipe on 26/10/2017.
 */

public class SettingsFragment extends Fragment{

    public static Fragment newInstance() {
        //instantiate fragment and add parameters to final variables
        Fragment frag = new SettingsFragment();
        Bundle args = new Bundle();
        //args.putInt(TEMPERATURE, temperature);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve data from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            //get data from bundle -> args.getData(DATA);
            //temperature = args.getInt(TEMPERATURE);


        } else {
            //get data from savedInstanceState -> savedInstanceState.getData(DATA);
           // temperature = savedInstanceState.getInt(TEMPERATURE);

        }

        // initialize view elements (textView, images...)

        // set stuff to view elements (clickers, text, colors)

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //update state with temp variables
        //*for string outState.putString(ARG_TEXT, text);

        super.onSaveInstanceState(outState);
    }
}
