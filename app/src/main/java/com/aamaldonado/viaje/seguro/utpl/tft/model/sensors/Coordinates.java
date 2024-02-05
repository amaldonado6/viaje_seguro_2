package com.aamaldonado.viaje.seguro.utpl.tft.model.sensors;

import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;

public class Coordinates {
    private static Coordinates INSTANCE;
    Double lat;
    Double lng;
    Double speed;

    String idExceso;

    Boolean checkExceso;

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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getIdExceso() {
        return idExceso;
    }

    public void setIdExceso(String idExceso) {
        this.idExceso = idExceso;
    }

    public Boolean getCheckExceso() {
        return checkExceso;
    }

    public void setCheckExceso(Boolean checkExceso) {
        this.checkExceso = checkExceso;
    }
}
