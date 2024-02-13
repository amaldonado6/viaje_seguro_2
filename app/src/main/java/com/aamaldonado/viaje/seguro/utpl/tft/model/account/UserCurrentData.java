package com.aamaldonado.viaje.seguro.utpl.tft.model.account;

import java.util.Date;

public class UserCurrentData {
    String fecha;
    String idBus;
    String idViaje;

    String idCompania;

    public UserCurrentData() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdBus() {
        return idBus;
    }

    public void setIdBus(String idBus) {
        this.idBus = idBus;
    }

    public String getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(String idViaje) {
        this.idViaje = idViaje;
    }

    public String getIdCompania() {
        return idCompania;
    }

    public void setIdCompania(String idCompania) {
        this.idCompania = idCompania;
    }
}
