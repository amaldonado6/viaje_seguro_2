package com.aamaldonado.viaje.seguro.utpl.tft.utils;

import android.location.Location;

import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class ValidateData {

    private ValidateData(){}

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

    public static String getDateTime(Boolean time) {
        // Crear un objeto Date
        Date date = new Date();
        // Asignar el nuevo formato
        SimpleDateFormat formatter;
        if (Boolean.TRUE.equals(time)) {
            formatter = new SimpleDateFormat(Constants.FORMATO_DATE_HORA,new Locale("es","ES"));
        } else {
            formatter = new SimpleDateFormat(Constants.FORMATO_DATE,new Locale("es","ES"));
        }
        // Convertir el objeto Date a String
        return formatter.format(date);
    }

    public static String getTime() {
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        return hora+":"+minutos;
    }
}
