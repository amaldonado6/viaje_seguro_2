package com.aamaldonado.viaje.seguro.utpl.tft.model.sensors;

import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;

public class Coordinates {
    private Double lat;
    private Double lng;
    private Double speed;

    private String idExceso;

    private Boolean checkExceso;

    public Coordinates() {
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
