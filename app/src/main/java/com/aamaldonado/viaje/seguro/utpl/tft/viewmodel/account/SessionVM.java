package com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.account;

import androidx.lifecycle.ViewModel;

import com.aamaldonado.viaje.seguro.utpl.tft.providers.AuthProvider;

public class SessionVM extends ViewModel {
    private final AuthProvider authProvider; //provider

    public SessionVM() {
        authProvider = new AuthProvider(); //inicializar el provider
    }

    public String getUser() {
        return authProvider.getClientId();
    }

    public boolean getSessionExist() {
        return authProvider.existSession();
    }
}
