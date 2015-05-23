package com.example.franziska.ibeacon;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class RoomList extends ActionBarActivity {

        int REQUEST_ENABLE_BT = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raumliste_prototyp);
        // setContentView(R.layout.activity_room_list);
        isBluetoothOn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Aufrufen des Raumplan-Layouts
    public void showRoom(View view) {
        setContentView(R.layout.raumplan_layout);
    }

    // zurück zum Start-Layout
    public void back(View view) {
        setContentView(R.layout.raumliste_prototyp);
    }

    public void toastAnzeigen(View view) {
        Toast.makeText(getApplicationContext(), "Ziel erreicht, Raum gefunden",Toast.LENGTH_SHORT).show();
    }

    public void isBluetoothOn() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
            msgBox.setMessage("Your Device does not support Bluetooth");
            msgBox.setNeutralButton("Okay!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "App wird beendet", Toast.LENGTH_SHORT).show();
                    RoomList.this.finish();
                }
            });
            msgBox.show();
        }

        else {

            if (!mBluetoothAdapter.isEnabled()) {
                AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
                msgBox.setMessage("Bluetooth ist ausgeschaltet, wird aber benötigt. Soll Bluetooth eingeschaltet werden?");
                        msgBox.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Bluetooth einschalten
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            }
                        });

                msgBox.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "App wird beendet...",
                                Toast.LENGTH_SHORT).show();
                        RoomList.this.finish();
                    }
                });
                msgBox.show();
            }
        }
    }
}


