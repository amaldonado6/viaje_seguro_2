package com.aamaldonado.viaje.seguro.utpl.tft.providers.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class AddressService {

    private String obtenerDireccion(float lat, float lng, Context context) {
        String direccionCompleta;
        //obtener la direccion
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException | IllegalArgumentException ioException) {
            Log.d(Constants.TAG1, Objects.requireNonNull(ioException.getMessage()));
        }
        // Maneja el caso en el que no se encontró ninguna dirección.
        assert addresses != null;
        Address address = addresses.get(0);
        // Aquí puedes obtener la dirección completa
        direccionCompleta = address.getAddressLine(0);
        return direccionCompleta;
    }
}
