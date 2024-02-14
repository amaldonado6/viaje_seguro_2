package com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aamaldonado.viaje.seguro.utpl.tft.providers.service.LocationService;

public class LocationViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    private LocationService locationService;
    private LiveData<Location> locationData;

    private final MutableLiveData<Boolean> guiaStatus;

    public LocationViewModel(){
        guiaStatus = new MutableLiveData<>();
    }

    public LiveData<Location> getLocationData() {
        return locationData;
    }

    public void startLocationUpdates(Context context) {
        locationService = new LocationService(context);
        locationService.startLocationUpdates();
        locationData = locationService.getLocationData();
    }

    public void stopLocationUpdates() {
        locationService.stopLocationUpdates();
    }

    public LiveData<Boolean> getStatus(){
        return guiaStatus;
    }

    public void  setGuiaStatus(Boolean status){
        guiaStatus.setValue(status);
    }

}
