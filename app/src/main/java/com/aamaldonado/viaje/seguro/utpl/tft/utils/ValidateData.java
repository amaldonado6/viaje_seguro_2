package com.aamaldonado.viaje.seguro.utpl.tft.utils;

import android.util.Patterns;

public class ValidateData {
    public ValidateData(){
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
}
