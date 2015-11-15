package tracker.gps.com.gpsspeedtracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MySpeedListener implements LocationListener {

    private final MainActivity mainActivity;

    public MySpeedListener(Context context) {
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mainActivity.setCurrentSpeed((int) (location.getSpeed() * 3.6));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

