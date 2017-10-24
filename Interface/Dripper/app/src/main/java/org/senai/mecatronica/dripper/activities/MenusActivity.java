package org.senai.mecatronica.dripper.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.helpers.DateOperations;

import java.util.Date;

/**
 * Created by Felipe on 24/10/2017.
 */

public class MenusActivity extends AppCompatActivity {

    //Irrigation menu GUI Elements
    ViewFlipper viewFlipperMenus;
    ImageButton imgBtnIrrigationMenu;
    ImageButton imgBtnHistoricalDataMenu;
    ImageButton imgBtnSensorDataMenu;
    ImageButton imgBtnSettingsMenu;

    TextView textDateFrom;
    TextView textDateTo;
    Button btnOpenCalendarFrom;
    Button btnOpenCalendarTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        //Initialize all GUI Menu Elements
        initIrrigationMenuElements();
        initDataMenuElements();
        initHistoricalDataMenuElements();
        initSettingsMenuElements();

        initIrrigationMenuListeners();
        initDataMenuListeners();
    }


    private void initIrrigationMenuElements(){
        imgBtnIrrigationMenu = (ImageButton) findViewById(R.id.imgBtnIrrigationMenu);
        imgBtnHistoricalDataMenu = (ImageButton) findViewById(R.id.imgBtnHistoricalDataMenu);
        imgBtnSensorDataMenu = (ImageButton) findViewById(R.id.imgBtnSensorDataMenu);
        imgBtnSettingsMenu = (ImageButton) findViewById(R.id.imgBtnSettingsMenu);

        viewFlipperMenus = (ViewFlipper) findViewById(R.id.viewFlipperMenus);

    }

    private void initDataMenuElements(){
        //initiate the date picker
        textDateFrom = (TextView) findViewById(R.id.textDateFrom);
        textDateTo = (TextView) findViewById(R.id.textDateTo);
        btnOpenCalendarFrom = (Button) findViewById(R.id.btnOpenCalendarFrom);
        btnOpenCalendarTo = (Button) findViewById(R.id.btnOpenCalendarTo);

        String dateFrom = DateOperations.getDateWithOffset(15);
        String dateTo = DateOperations.getDateWithOffset(0);

        textDateFrom.setText(dateFrom);
        textDateTo.setText(dateTo);
    }

    private void initHistoricalDataMenuElements(){

    }

    private void initSettingsMenuElements(){

    }

    private void initIrrigationMenuListeners(){

        imgBtnIrrigationMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //viewFlipperMenus.setDisplayedChild(viewFlipperMenus.indexOfChild(R.id.childId));
                viewFlipperMenus.setDisplayedChild(0);
            }
        });

        imgBtnSensorDataMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewFlipperMenus.setDisplayedChild(1);
            }
        });

        imgBtnHistoricalDataMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewFlipperMenus.setDisplayedChild(2);
            }
        });

        imgBtnSettingsMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewFlipperMenus.setDisplayedChild(3);
            }
        });
    }

    private void initDataMenuListeners(){
        //calendar button click actions
        btnOpenCalendarFrom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DateOperations.setDate(MenusActivity.this, textDateFrom);
            }
        });

        btnOpenCalendarTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DateOperations.setDate(MenusActivity.this, textDateTo);
            }
        });
    }
}
