package be.project.farmhelp;

import com.google.android.gms.location.FusedLocationProviderClient;

public class LocationManager {

    private static final int REQUEST_CODE = 111;
    private FusedLocationProviderClient client;

    public void getCurrentLocation(){
        checkLocationPermission();
    }

    private void checkLocationPermission() {

    }
}
