package com.aamaldonado.viaje.seguro.utpl.tft.utils;

public final class PhoneValidate {

    private PhoneValidate(){}
    public static boolean dataModelPhone(String phone) {
        // Comprobamos que la cadena solo contenga dígitos
        if (!phone.matches("\\d+")) {
            return false;
        }

        // Comprobamos que la longitud de la cadena sea 9 o 10
        int longitud = phone.length();
        if (longitud != 9 && longitud != 10) {
            return false;
        }

        /*
        * Si la longitud es de 9, comprobamos que el primer dígito no sea 0
        * porque se aceptan numeros de telefono sin 0, es decir 9 digitos,
        * */
        return longitud != 9 || phone.charAt(0) != '0';
    }
}
