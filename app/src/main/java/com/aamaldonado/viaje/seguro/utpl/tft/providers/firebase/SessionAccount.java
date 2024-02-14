package com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase;

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
