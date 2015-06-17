package com.example.ibeaconstest;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Iterator;


public class MainActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    TextView view;
    final static int tuerAbstand = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        Log.i(TAG, "Create");
        view = (TextView) findViewById(R.id.iBeaconAusgabe);
        view.setText("gesetztz");
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
                                           @Override
                                           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                                               Beacon tuer1 = getBeaconByMinor(beacons, 1);
                                               Beacon tuer2 = getBeaconByMinor(beacons, 2);
                                               Beacon tuer3 = getBeaconByMinor(beacons, 3);
                                               if (tuer1 != null & tuer2 != null & tuer3 != null) {
                                                   final String ausgabe;
                                                   if ((tuer1.getDistance() < tuer2.getDistance() && tuer2.getDistance() < tuer3.getDistance()) && tuer1.getDistance() < tuerAbstand) {
                                                       Log.i(TAG, "Sie stehen vor Tür1");
                                                       ausgabe = "Sie stehen vor Tür1";
                                                   } else if ((tuer1.getDistance() > tuer2.getDistance() && tuer2.getDistance() < tuer3.getDistance()) && tuer2.getDistance() < tuerAbstand) {
                                                       Log.i(TAG, "Sie stehen vor Tür2");
                                                       ausgabe = "Sie stehen vor Tür2";
                                                   }
                                                   else if ((tuer3.getDistance() < tuer2.getDistance() && tuer3.getDistance() < tuer1.getDistance()) && tuer3.getDistance() < tuerAbstand) {
                                                       Log.i(TAG, "Sie stehen vor Tür3");
                                                       ausgabe = "Sie stehen vor Tür3";
                                                   } else {
                                                       Log.i(TAG, "Sie stehen vor keiner Tür");
                                                       ausgabe = "Sie stehen vor keiner Tür";
                                                   }
                                                   runOnUiThread(new Runnable() {
                                                       public void run() {
                                                           view.setText(ausgabe);

                                                       }
                                                   });


                                               }


                                           }
                                       }

        );

        try

        {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException e
                )

        {
        }

    }

    private Beacon getBeaconByMinor(Collection<Beacon> beacons, int i) {
        Iterator<Beacon> iter = beacons.iterator();
        while (iter.hasNext()) {
            Beacon tmp = iter.next();
            if (tmp.getId3().toInt() == i) {
                return tmp;
            }
        }
        return null;

    }
}