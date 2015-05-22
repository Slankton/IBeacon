package com.example.franziska.ibeacon;

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

/**
 * Created by Slanktonen on 22.05.2015.
 */
public class DoorCalculator extends Activity implements BeaconConsumer {

    public double door;
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    TextView view;
    final static int tuerAbstand = 4 / 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        door = 0;
        super.onCreate(savedInstanceState);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override//Methode wird permanent ausgeführt, sobald ein Beacone entdeckt wurde
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
                                           @Override
                                           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                                               Beacon tuer1 = getBeaconByMinor(beacons, 1);
                                               Beacon tuer2 = getBeaconByMinor(beacons, 2);
                                               Beacon tuer3 = getBeaconByMinor(beacons, 3);
                                               if (tuer1 != null & tuer2 != null & tuer3 != null) {
                                                   if ((tuer1.getDistance() < tuer2.getDistance() && tuer1.getDistance() < tuer2.getDistance()) && tuer1.getDistance() < tuerAbstand) {
                                                       door = 0.5;
                                                   } else if (tuer1.getDistance() < tuerAbstand) {
                                                       door = 1;
                                                   } else if ((tuer1.getDistance() > tuer2.getDistance() && tuer2.getDistance() < tuer3.getDistance()) && (tuer1.getDistance() > tuerAbstand && tuer2.getDistance() > tuerAbstand)) {
                                                       door = 1.5;
                                                   } else if ((tuer1.getDistance() > tuer2.getDistance() && tuer2.getDistance() < tuer3.getDistance()) && tuer2.getDistance() < tuerAbstand) {
                                                       door = 2;
                                                   } else if ((tuer3.getDistance() < tuer2.getDistance() && tuer3.getDistance() < tuer1.getDistance()) && (tuer1.getDistance() > tuerAbstand && tuer2.getDistance() > tuerAbstand)) {
                                                       door = 2.5;
                                                   } else if ((tuer3.getDistance() < tuer2.getDistance() && tuer3.getDistance() < tuer1.getDistance()) && tuer3.getDistance() < tuerAbstand) {
                                                       door = 3.0;
                                                   } else if (tuer3.getDistance() < tuerAbstand) {
                                                       door = 3.5;
                                                   } else {//Bei keiner Tür
                                                       door = 0;
                                                   }
                                               }
                                           }
                                       }
        );

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (
                RemoteException e
                ) {
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


    public double getDoor() {
        return door;
    }
}