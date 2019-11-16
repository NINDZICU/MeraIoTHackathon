package ru.sovcombank.iotmerahackathon.beaconreference;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import ru.sovcombank.iotmerahackathon.MainActivity;

/**
 * Created by dyoung on 12/13/13.
 */
public class BeaconReferenceApplication extends Application implements BootstrapNotifier {
    private static final String TAG = "BeaconReferenceApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MainActivity mainActivity = null;
    private String cumulativeLog = "";

    public void onCreate() {
        super.onCreate();
        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));

        beaconManager.setDebug(false);

        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);


        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    @Override
    public void didEnterRegion(Region arg0) {
        Log.d(TAG, "did enter region.");
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (mainActivity != null) {
                logToDisplay("I see a beacon again" );
            } else {
                Log.d(TAG, "Sending notification.");
            }
        }
    }

    @Override
    public void didExitRegion(Region region) {
        logToDisplay("I no longer see a beacon.");
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        logToDisplay("Current region state is: " + (state == 1 ? "INSIDE" : "OUTSIDE ("+state+")"));
    }



    private void logToDisplay(String line) {
        cumulativeLog += (line + "\n");
        if (this.mainActivity != null) {
//            this.mainActivity.updateLog(cumulativeLog);
        }
    }

    public String getLog() {
        return cumulativeLog;
    }

}
