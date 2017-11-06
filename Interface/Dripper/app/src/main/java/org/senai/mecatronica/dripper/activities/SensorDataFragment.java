package org.senai.mecatronica.dripper.activities;

import android.os.Bundle;
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

public class SensorDataFragment extends Fragment {

    //fragment final variables
    private static final String TEMPERATURE = "arg_temperature";
    private static final String MOISTURE = "arg_moisture";
    private static final String LUMINOSITY = "arg_luminosity";
    private static final String SOIL_MOISTURE = "arg_soil_moisture";

    //temporary variables relative to final
    private Integer temperature;
    private Integer moisture;
    private Integer luminosity;
    private String soilMoisture;


    //view elements
    private View mContent;
    private TextView temperatureData;
    private TextView moistureData;
    private TextView luminosityData;
    private TextView soilMoistureData;

    public static Fragment newInstance(Integer temperature, Integer moisture, Integer luminosity, String soilMoisture) {
        //instantiate fragment and add parameters to final variables
        Fragment frag = new SensorDataFragment();
        Bundle args = new Bundle();
        args.putInt(TEMPERATURE, temperature);
        args.putInt(MOISTURE, moisture);
        args.putInt(LUMINOSITY, luminosity);
        args.putString(SOIL_MOISTURE, soilMoisture);

        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensor_data, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve data from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            //get data from bundle -> args.getData(DATA);
            temperature = args.getInt(TEMPERATURE);
            moisture = args.getInt(MOISTURE);
            luminosity = args.getInt(LUMINOSITY);
            soilMoisture = args.getString(SOIL_MOISTURE);

        } else {
            //get data from savedInstanceState -> savedInstanceState.getData(DATA);
            temperature = savedInstanceState.getInt(TEMPERATURE);
            moisture = savedInstanceState.getInt(MOISTURE);
            luminosity = savedInstanceState.getInt(LUMINOSITY);
            soilMoisture = savedInstanceState.getString(SOIL_MOISTURE);
        }

        // initialize view elements (textView, images...)
        mContent = view.findViewById(R.id.sensor_data_fragment_content);
        temperatureData = (TextView) view.findViewById(R.id.txt_sensor_temperature_data);
        moistureData = (TextView) view.findViewById(R.id.txt_sensor_moisture_data);
        luminosityData = (TextView) view.findViewById(R.id.txt_sensor_luminosity_data);
        soilMoistureData = (TextView) view.findViewById(R.id.txt_sensor_soil_moisture_data);

        // set stuff to view elements (clickers, text, colors)
        temperatureData.setText(temperature.toString() + "°C");
        moistureData.setText(moisture.toString() + "%");
        luminosityData.setText(luminosity.toString() + " Lux");
        soilMoistureData.setText(soilMoisture);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //update state with temp variables
        outState.putInt(TEMPERATURE, temperature);
        outState.putInt(MOISTURE, moisture);
        outState.putInt(LUMINOSITY, luminosity);
        outState.putString(SOIL_MOISTURE, soilMoisture);
        //*for string outState.putString(ARG_TEXT, text);
        super.onSaveInstanceState(outState);
    }

}
