package org.senai.mecatronica.dripper.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONException;
import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.managers.DataManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";
    private static final String IRRIGATION_FILE = "default_irrigation_data.json";
    private static final String FIELD_DATA_FILE = "default_field_data.json";

    private BottomNavigationView mBottomNav;
    private int currentSelectedItem;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize data manager
        dataManager = dataManager.getInstance(this, IRRIGATION_FILE, FIELD_DATA_FILE);
        try{
            dataManager.updateIrrigationData();
        } catch (JSONException e){
            System.out.println("Error creating JSON Object");
        } catch (IOException e){
            System.out.println("Could not read JSON File");
        }

        System.out.println("OK");

        //initialize bottom navigation menu
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired actions here
                selectFragment(item);
                // return true to display the item as the selected item
                return true;
            }
        });

        //first menu to be loaded on app start
        MenuItem selectedItem;
        if (savedInstanceState != null) {
            currentSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(currentSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, currentSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (currentSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item){
        Fragment frag = null;
        int itemID = item.getItemId();
        //init corresponding fragment
        switch(itemID){
            case R.id.menu_irrigation:
                frag = IrrigationFragment.newInstance(dataManager.getIrrigationDataList(), dataManager.getAutoMode());
                break;
            case R.id.menu_sensor_data:
                frag = SensorDataFragment.newInstance(25,32,10000,"Baixa");
                break;
            case R.id.menu_settings:
                frag = SettingsFragment.newInstance();
                break;
        }

        // update selected item
        currentSelectedItem = itemID;

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        //update action bar text
        updateToolbarText(item.getTitle());

        //adds a fragment transaction to the view container
        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }


}
