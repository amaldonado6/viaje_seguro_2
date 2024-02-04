package com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase;

import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.model.account.UserCurrentData;
import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Coordinates;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DataHandler {

    private static DataHandler INSTANCE;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    private DataHandler(){
        mDatabase = FirebaseDatabase.getInstance();
    }
    public static DataHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DataHandler();
        }
        return INSTANCE;
    }


    /**
     * Metodo para actualizar los datos individuales del usuario, en que transporte se encuentra
     * */
    public void setCurrentClientData(UserCurrentData data) {
        SessionAccount sessionVM = new SessionAccount();
        if (sessionVM.getSessionExist()){
            myRef = mDatabase.getReference(Constants.USER_DATA_REF).child(sessionVM.getUser());
            // Crea un mapa con los campos que quieres actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put("fecha", data.getFecha());
            updates.put("idBus", data.getIdBus());
            updates.put("idViaje", data.getIdViaje());
            // Actualiza los campos del usuario en la base de datos
            myRef.updateChildren(updates);
        }
    }
    /**
     * Metodo para actualizar los datos individuales del usuario (ubicacion)
     * */
    public void setCurrentClientDataLatLng(Coordinates coordinates) {
        SessionAccount sessionVM = new SessionAccount();
        if (sessionVM.getSessionExist()){
            myRef = mDatabase.getReference(Constants.USER_DATA_REF).child(sessionVM.getUser());
            // Crea un mapa con los campos que quieres actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put("coordenadas", coordinates);
            // Actualiza los campos del usuario en la base de datos
            myRef.updateChildren(updates);
        }
    }

    public void setCurrentClientDataToBus(Coordinates instance) {

    }
}
