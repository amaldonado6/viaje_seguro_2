package com.aamaldonado.viaje.seguro.utpl.tft.utils;

import android.location.Location;
import android.util.Patterns;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class ValidateData {

    public static double getSpeed(Location location) {
        double sp = 0.0;
        DecimalFormat speedFormat = new DecimalFormat("#.##");
        if (location.hasSpeed()) {
            //obetener la velocidad en k/h
            sp = ((location.getSpeed() * 3600) / 1000);
            if (sp < 10) {
                sp = 0.0;
            }
        }
        return Double.parseDouble(speedFormat.format(sp));
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

    public static String getDateTime(Boolean time) {
        // Crear un objeto Date
        Date date = new Date();
        // Asignar el nuevo formato
        SimpleDateFormat formatter;
        if (time) {
            formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        } else {
            formatter = new SimpleDateFormat("dd-MM-yyyy");
        }
        // Convertir el objeto Date a String
        String strDate = formatter.format(date);
        return strDate;
    }

    public static String getTime() {
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        return hora+":"+minutos;
    }
}
