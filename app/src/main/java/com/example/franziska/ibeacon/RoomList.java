package com.example.franziska.ibeacon;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RoomList extends ActionBarActivity {

        int REQUEST_ENABLE_BT = 5;
        DoorCalculator doorCalculator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raumliste_prototyp);
        // setContentView(R.layout.activity_room_list);
        //isBluetoothOn();
        doorCalculator = new DoorCalculator();
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
        standortAnzeigen();
    }

    // zurück zum Start-Layout
    public void back(View view) {
        setContentView(R.layout.raumliste_prototyp);
    }

    public void toastAnzeigen(String message) {
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
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

    public void standorteInvisible(View[] standorte) {
        for (int i = 0; i < standorte.length; i++) {
            standorte[i].setVisibility(View.INVISIBLE);
        }
    }

    public void suchen(View view) {
        EditText searchField = (EditText) findViewById(R.id.room_search_field);
        String eingabe = searchField.getText().toString();
        toastAnzeigen(eingabe);
        if( eingabe.equals("2-128")) {
            findViewById(R.id.button_128).setVisibility(View.VISIBLE);
            findViewById(R.id.text_128).setVisibility(View.VISIBLE);
        }
        if( eingabe.equals("2-129")) {
            findViewById(R.id.button_129).setVisibility(View.VISIBLE);
            findViewById(R.id.text_129).setVisibility(View.VISIBLE);
        }
        if( eingabe.equals("2-130")) {
            findViewById(R.id.button_130).setVisibility(View.VISIBLE);
            findViewById(R.id.text_130).setVisibility(View.VISIBLE);
        }

    }

    public void standortAnzeigen() {

        View [] standorte = new View[7];

        standorte[0] = (View) findViewById(R.id.standort1);
        standorte[1] = (View) findViewById(R.id.standort2);
        standorte[2] = (View) findViewById(R.id.standort3);
        standorte[3] = (View) findViewById(R.id.standort4);
        standorte[4] = (View) findViewById(R.id.standort5);
        standorte[5] = (View) findViewById(R.id.standort6);
        standorte[6] = (View) findViewById(R.id.standort7);

        standorteInvisible(standorte);

         if (doorCalculator.getDoor() == 0.0) {
            standorteInvisible(standorte);
            toastAnzeigen("Raum nicht in Reichweite");
        }

        if (doorCalculator.getDoor() == 0.5) {
            standorteInvisible(standorte);
            standorte[0].setVisibility(View.VISIBLE);
        }

        if (doorCalculator.getDoor() == 1.0) {
            standorteInvisible(standorte);
            standorte[1].setVisibility(View.VISIBLE);
        }

        if (doorCalculator.getDoor() == 1.5) {
            standorteInvisible(standorte);
            standorte[2].setVisibility(View.VISIBLE);
        }

        if (doorCalculator.getDoor() == 2.0) {
            standorteInvisible(standorte);
            standorte[3].setVisibility(View.VISIBLE);
        }

        if (doorCalculator.getDoor() == 2.5) {
            standorteInvisible(standorte);
            standorte[4].setVisibility(View.VISIBLE);
        }

        if (doorCalculator.getDoor() == 3.0) {
            standorteInvisible(standorte);
            standorte[5].setVisibility(View.VISIBLE);
        }

        if (doorCalculator.getDoor() == 3.5) {
            standorteInvisible(standorte);
            standorte[6].setVisibility(View.VISIBLE);
        }
    }
}


