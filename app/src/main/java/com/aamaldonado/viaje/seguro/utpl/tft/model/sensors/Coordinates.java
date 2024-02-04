package com.aamaldonado.viaje.seguro.utpl.tft.model.sensors;

import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;

public class Coordinates {
    private static Coordinates INSTANCE;
    Double lat;
    Double lng;
    String speed;

    private Coordinates() {
    }

    public static Coordinates getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Coordinates();
        }
        return INSTANCE;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
