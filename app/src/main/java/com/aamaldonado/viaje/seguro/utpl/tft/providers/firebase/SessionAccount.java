package com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase;

import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.AuthProvider;

public class SessionAccount {
    private final AuthProvider authProvider; //provider

    public SessionAccount() {
        authProvider = new AuthProvider(); //inicializar el provider
    }

    public String getUser() {
        return authProvider.getClientId();
    }

    public boolean getSessionExist() {
        return authProvider.existSession();
    }
}
