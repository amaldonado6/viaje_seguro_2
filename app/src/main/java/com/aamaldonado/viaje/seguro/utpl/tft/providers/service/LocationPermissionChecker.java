package com.aamaldonado.viaje.seguro.utpl.tft.providers.service;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;

public class LocationPermissionChecker {
    private final Activity activity;

    public LocationPermissionChecker(Activity activity) {
        this.activity = activity;
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODA_LOCATION_PERMISSION);
    }
}