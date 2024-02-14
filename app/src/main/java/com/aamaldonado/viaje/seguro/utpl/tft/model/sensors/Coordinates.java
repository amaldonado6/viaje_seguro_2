package com.aamaldonado.viaje.seguro.utpl.tft.model.sensors;

public class Coordinates {
    private double lat;
    private double lng;
    private double speed;

    private String idExceso;

    private Boolean checkExceso;

    public Coordinates() {
        //empty
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
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
