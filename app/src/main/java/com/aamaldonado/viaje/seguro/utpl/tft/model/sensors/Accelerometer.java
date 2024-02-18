package com.aamaldonado.viaje.seguro.utpl.tft.model.sensors;

public class Accelerometer {
    private float ejeX;
    private float ejeY;
    private float ejeZ;

    private double lat;
    private double lng;
    public Accelerometer(){
        //empty
    }

    public float getEjeX() {
        return ejeX;
    }

    public void setEjeX(float ejeX) {
        this.ejeX = ejeX;
    }

    public float getEjeY() {
        return ejeY;
    }

    public void setEjeY(float ejeY) {
        this.ejeY = ejeY;
    }

    public float getEjeZ() {
        return ejeZ;
    }

    public void setEjeZ(float ejeZ) {
        this.ejeZ = ejeZ;
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
}
