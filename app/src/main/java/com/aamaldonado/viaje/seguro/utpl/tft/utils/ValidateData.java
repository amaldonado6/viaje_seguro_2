package com.aamaldonado.viaje.seguro.utpl.tft.utils;

import android.location.Location;
import android.util.Patterns;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ValidateData {

    public static String getSpeed(Location location) {
        double sp = 0.0;
        DecimalFormat speedFormat = new DecimalFormat("#.##");
        if (location.hasSpeed()) {
            //obetener la velocidad en k/h
            sp = ((location.getSpeed() * 3600) / 1000);
            if (sp < 10) {
                sp = 0.0;
            }
        }
        return speedFormat.format(sp);
    }

    public boolean validateMail(String mail) {
        boolean valor = false;
        //verificar mail
        if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            //verificar nombre
            valor = true;
        }
        return valor;
    }
    public boolean validateName(String name) {
        boolean valor = false;
        //verificar mail
        if (!name.isEmpty() && name.length() > 3) {
            //verificar nombre
            valor = true;
        }
        return valor;
    }

    public static String getDate(){
        // Crear un objeto Date
        Date date = new Date();
        // Asignar el nuevo formato
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        // Convertir el objeto Date a String
        String strDate = formatter.format(date);
        return strDate;
    }
}
