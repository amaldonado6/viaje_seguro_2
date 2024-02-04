package com.aamaldonado.viaje.seguro.utpl.tft.providers.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Coordinates;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.ValidateData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;


public class LocationService extends Service {
    private final FusedLocationProviderClient fusedLocationClient;
    private final MutableLiveData<Location> locationData;
    private final Context context;

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("TAG1","GET LOCATION");
            //current location
            if (Objects.nonNull(locationResult)) {
                dataHandler(locationResult);
            }
        }
    };

    private void dataHandler(LocationResult locationResult) {
        if(Objects.nonNull(locationResult.getLastLocation())){
            locationData.setValue(locationResult.getLastLocation()); // set location to ViewModel
            //Store DB
            Coordinates.getInstance().setLat(locationResult.getLastLocation().getLatitude());
            Coordinates.getInstance().setLng(locationResult.getLastLocation().getLongitude());
            Coordinates.getInstance().setSpeed(ValidateData.getSpeed(locationResult.getLastLocation()));
            DataHandler.getInstance().setCurrentClientDataLatLng(Coordinates.getInstance());
            DataHandler.getInstance().setCurrentClientDataToBus(Coordinates.getInstance());
        }
    }

    public LocationService(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationData = new MutableLiveData<>();
        this.context = context;
    }

    public LiveData<Location> getLocationData() {
        return locationData;
    }

    public void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

