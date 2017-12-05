package org.senai.mecatronica.dripper.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.managers.BluetoothManager;
import org.senai.mecatronica.dripper.managers.DataManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int currentSelectedItem;
    private DataManager dataManager;
    private BluetoothManager btManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize data manager
        dataManager = dataManager.getInstance(this);
        try{
            dataManager.updateIrrigationData();
            dataManager.updateSensorData();
        } catch (JSONException e){
            System.out.println("Error creating JSON Object");
        } catch (IOException e){
            System.out.println("Could not read JSON File");
        }

        //initialize bluetooth manager
        btManager = BluetoothManager.getInstance(this);

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

        //restore saved instance state or create a new
        MenuItem selectedItem;
        if (savedInstanceState != null) {
            //selected menu item
            currentSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(currentSelectedItem);
        } else {
            //selected menu item
            selectedItem = mBottomNav.getMenu().getItem(0);
        }

        selectFragment(selectedItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync_btn:
                /*
                btManager.setMacAddress(dataManager.getMacAddress());
                if(!btManager.isSupported()){
                    Toast.makeText(this, "Bluetooth não suportado neste dispositivo", Toast.LENGTH_SHORT).show();
                }else if(!btManager.isEnabled()){
                    Toast.makeText(this, "Habilite o Bluetooth para sincronizar", Toast.LENGTH_SHORT).show();
                }else if (!btManager.isPaired()){
                    Toast.makeText(this, "Dispositivo não pareado", Toast.LENGTH_SHORT).show();
                } else{
//                    new ConnectToBTServer().execute();
                    btManager.startConnection();
                }
                break;
                */
                dataManager.testDataParser();
            case R.id.config_btn:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
        }
        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btManager.closeConnection();
    }

    private void selectFragment(MenuItem item){
        Fragment frag = null;
        int itemID = item.getItemId();

        //init corresponding fragment
        switch(itemID){
            case R.id.menu_irrigation:
                frag = IrrigationFragment.newInstance();
                break;
            case R.id.menu_sensor_data:
                frag = SensorDataFragment.newInstance();
                break;
        }

        // update selected item
        currentSelectedItem = itemID;

        // update checked items
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            mBottomNav.getMenu().getItem(i).setChecked(false);
        }
        item.setChecked(true);

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

    //AsyncTask<Params, Progress, Result>
    private class ConnectToBTServer extends AsyncTask<Void, Void, Void>{
        ProgressDialog connectingDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            connectingDialog.setMessage("Conectando ao dispositivo");
            //show dialog
            connectingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            btManager.startConnection();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if(connectingDialog != null && connectingDialog.isShowing()){
                connectingDialog.dismiss();
            }
//            if(success){
////                btManager.sendIrrigationData(dataManager.getIrrigationDataUri());
//                Toast.makeText(MainActivity.this, "Conexão bem sucedida", Toast.LENGTH_LONG).show();
//            }
            super.onPostExecute(aVoid);
        }
    }

}
