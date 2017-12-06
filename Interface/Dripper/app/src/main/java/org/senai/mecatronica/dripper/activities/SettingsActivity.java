package org.senai.mecatronica.dripper.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.senai.mecatronica.dripper.R;
import org.senai.mecatronica.dripper.managers.DataManager;

import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    TextView aboutItem;
    TextView macAddress;
    TextView clearDataItem;
    RelativeLayout userManualItem;
    RelativeLayout macAddressItem;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //call initialization methods
        initializeViewElements();
        setupClickListeners();
    }


    private void initializeViewElements() {
        macAddressItem = (RelativeLayout) findViewById(R.id.layout_mac_address_item);
        userManualItem = (RelativeLayout) findViewById(R.id.layout_user_manual_item);
        aboutItem = (TextView) findViewById(R.id.lbl_settings_about);
        macAddress = (TextView) findViewById(R.id.txt_settings_mac_address);
        clearDataItem = (TextView) findViewById(R.id.lbl_settings_clear_data);

        macAddress.setText(DataManager.getInstance(context).getMacAddress());
    }

    private void setupClickListeners() {
        macAddressItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open popup for editing the mac address
                final Dialog d = new Dialog(context);
                d.setTitle("Escreva o endereço MAC");
                d.setContentView(R.layout.dialog_mac_address_edit);

                final EditText macEdit = (EditText) d.findViewById(R.id.txt_mac_address_edit);
                Button confirmBtn = (Button) d.findViewById(R.id.btn_ok_dialog_mac_address);

                macEdit.setText(macAddress.getText());

                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if mac address is valid
                        String mac = String.valueOf(macEdit.getText()).trim().toUpperCase();
                        if(isMacAddress(mac)){
                            macAddress.setText(mac);
                            DataManager.getInstance(context).setMacAddress(mac);
                        } else {
                            Toast.makeText(d.getContext(), "Endereço MAC inválido", Toast.LENGTH_LONG);
                        }
                        d.dismiss();
                    }
                });
                d.show();
            }
        });

        userManualItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open user manual pdf
            }
        });

        aboutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open popup with about text
            }
        });

        clearDataItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Apagar todos os dados gravados?")
                        .setTitle("Apagar Dados");

                // Add the buttons
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        DataManager.getInstance(context).clearDataFiles();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private boolean isMacAddress(String text){
//        Pattern macAddressPattern = Pattern.compile("^(?:[A-F0-9]{2}:){5}(?:[A-F0-9]{2})$");
        if(Pattern.matches("^(?:[A-F0-9]{2}:){5}(?:[A-F0-9]{2})$", text)){
            return true;
        }
        return false;
    }


}
