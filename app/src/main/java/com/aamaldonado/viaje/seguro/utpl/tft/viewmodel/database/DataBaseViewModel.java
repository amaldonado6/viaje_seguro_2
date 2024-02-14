package com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Coordinates;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.SessionAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBaseViewModel extends ViewModel {

    private final MutableLiveData<Coordinates> exceso;
    private final MutableLiveData<List<Coordinates>> excesosList;

    private final LiveData<Boolean> tieneTransporte;

    public DataBaseViewModel() {
        exceso = new MutableLiveData<>();
        excesosList = new MutableLiveData<>();
        excesosList.setValue(new ArrayList<>());
        tieneTransporte = DataHandler.getInstance().getTransporte();
    }
    public LiveData<Boolean> getTieneTransporte() {
        return tieneTransporte;
    }

    public LiveData<List<Coordinates>> getExcesosList() {
        return excesosList;
    }

    /**
     * Metodo para cargar los datos de los excesos de velocidad e ir capturando continuamente nuevos excesos
     */
    public void loadData() {
        DataHandler.getInstance().getExcesos(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                setExcesos(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                setExcesos(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(Constants.TAG1, Objects.requireNonNull(snapshot.getKey()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(Constants.TAG1, Objects.requireNonNull(snapshot.getKey()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(Constants.TAG1, Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    /**
     * metodo para asignar el valor del exeso capturado
     */
    private void setExcesos(DataSnapshot snapshot) {
        String[] parts = Objects.requireNonNull(snapshot.getKey()).split(Constants.SEPARATE_CHARACTER);
        SessionAccount sessionAccount = new SessionAccount();
        //Solo agregar los excesos capturados del usuario
        if (parts[0].equals(sessionAccount.getUser())) {
            Coordinates coordinates = new Coordinates();
            coordinates.setLat(Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.DB_LAT).getValue()).toString()));
            coordinates.setLng(Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.DB_LNG).getValue()).toString()));
            coordinates.setSpeed(Double.parseDouble(Objects.requireNonNull(snapshot.child(Constants.DB_SPEED).getValue()).toString()));
            coordinates.setCheckExceso(Boolean.valueOf(Objects.requireNonNull(snapshot.child(Constants.DB_CHECK_EXCESO).getValue()).toString()));
            coordinates.setIdExceso(snapshot.getKey());
            exceso.setValue(coordinates);
            List<Coordinates> listaActual = getExcesosList().getValue();
            if (listaActual != null) {
                boolean isUpdate = true;
                //Si el id del reporte existe es porque se va a actualizar el valor, si no existe añade uno nuevo
                for (Coordinates searchCoord : listaActual) {
                    if (searchCoord.getIdExceso().equals(coordinates.getIdExceso())) {
                        searchCoord.setCheckExceso(coordinates.getCheckExceso());
                        isUpdate = false;
                    }
                }
                if (isUpdate) {
                    listaActual.add(coordinates);
                    excesosList.setValue(listaActual);
                }
            }
        }
    }
}
