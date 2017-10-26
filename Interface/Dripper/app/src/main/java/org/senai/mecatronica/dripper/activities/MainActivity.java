package org.senai.mecatronica.dripper.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import org.senai.mecatronica.dripper.R;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int currentSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void selectFragment(MenuItem item){
        Fragment frag = null;
        int itemID = item.getItemId();
        //init corresponding fragment
        switch(itemID){
            case R.id.menu_irrigation:
                frag = IrrigationFragment.newInstance();
                break;
            case R.id.menu_historical_data:
                frag = HistoricalDataFragment.newInstance();
                break;
            case R.id.menu_sensor_data:
                frag = SensorDataFragment.newInstance();
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
            ft.add(R.id.container, frag, frag.getTag());
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
