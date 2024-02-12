package com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.model.account.UserCurrentData;
import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Coordinates;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.ValidateData;
import com.google.common.base.Strings;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataHandler {

    private static DataHandler INSTANCE;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    DatabaseReference busRef;
    SessionAccount sessionVM;
    String idBus;

    String idViaje;

    private DataHandler() {
        sessionVM = new SessionAccount();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference(Constants.USER_DATA_REF).child(sessionVM.getUser());
        getdataBus();
    }

    public static DataHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataHandler();
        }
        return INSTANCE;
    }

    private void getdataBus() {
        DatabaseReference myRefBus = mDatabase.getReference(Constants.USER_DATA_REF).child(sessionVM.getUser());
        myRefBus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.nonNull(snapshot.child("idBus").getValue())) {
                    idBus = Objects.requireNonNull(snapshot.child("idBus").getValue()).toString();
                }
                if (Objects.nonNull(snapshot.child("idViaje").getValue())) {
                    idViaje = Objects.requireNonNull(snapshot.child("idViaje").getValue()).toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error al leer el valor
                Log.d(TAG, "Error al leer valores de la bdd.", error.toException());
            }
        });
    }


    /**
     * Metodo para actualizar los datos individuales del usuario en un transporte (en el nodo del transporte)
     */
    public void setCurrentClientData(UserCurrentData data) {
        if (sessionVM.getSessionExist()) {
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
     * Metodo para actualizar los datos individuales del usuario (ubicacion en su propio nodo)
     */
    public void setCurrentClientDataLatLng(Coordinates coordinates) {
        if (sessionVM.getSessionExist()) {
            // Crea un mapa con los campos que quieres actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put("coordenadas", coordinates);
            // Actualiza los campos del usuario en la base de datos
            myRef.updateChildren(updates);
        }
    }

    /**
     * Metodo para registrar los excesos de velocidad capturados en el transcurso del viaje
     */
    public void setCurrentClientDataToBus(Coordinates coordinates) {
        if (!Strings.isNullOrEmpty(idBus) && !Strings.isNullOrEmpty(idViaje) && coordinates.getSpeed() >= 90) {
            coordinates.setCheckExceso(Boolean.FALSE); // el reporte del exceso esta por confirmarse
            busRef = mDatabase.getReference(Constants.BUS_DATA_REF)
                    .child(idBus)
                    .child(Constants.CHILD_REPORTS)
                    .child(ValidateData.getDateTime(false))
                    .child(idViaje)
                    .child(sessionVM.getUser() + "-" + ValidateData.getTime());
            busRef.setValue(coordinates);
        }
    }

    /**
     * Metodo para obtener los excesos de velocidad individuales que se capturaron en el transcurso del viaje
     */
    public void getExcesos(ChildEventListener valueEventListener) {
        idBus = "IB123";
        idViaje = "IV123";
        if (!Strings.isNullOrEmpty(idBus) && !Strings.isNullOrEmpty(idViaje)) {
            DatabaseReference excesosRef = mDatabase.getReference(Constants.BUS_DATA_REF)
                    .child(idBus)
                    .child(Constants.CHILD_REPORTS)
                    .child(ValidateData.getDateTime(false))
                    .child(idViaje);
            excesosRef.addChildEventListener(valueEventListener);
        }

    }

    /**
     * Metodo para actualizar los datos individuales del usuario (ubicacion en su propio nodo)
     */
    public void setEstadoDelExcesoDeVelocidad(Boolean status,String key) {
        DatabaseReference newRef = mDatabase.getReference(Constants.BUS_DATA_REF)
                .child(idBus)
                .child(Constants.CHILD_REPORTS)
                .child(ValidateData.getDateTime(false))
                .child(idViaje)
                .child(key);
        if (sessionVM.getSessionExist()) {
            // Crea un mapa con los campos que quieres actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put("checkExceso", status);
            // Actualiza los campos del usuario en la base de datos
            newRef.updateChildren(updates);
        }
    }

}
