package com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataHandler {

    private static DataHandler instance;
    private final FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private final SessionAccount sessionVM;
    private String idBus;
    private String idViaje;
    private String idCompania;

    private MutableLiveData<Boolean> tieneTransporte;

    private DataHandler() {
        tieneTransporte = new MutableLiveData<>();
        sessionVM = new SessionAccount();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference(Constants.USER_DATA_REF).child(sessionVM.getUser());
        getdataBus();
    }

    public static DataHandler getInstance() {
        if (instance == null) {
            instance = new DataHandler();
        }
        return instance;
    }

    private void getdataBus() {
        DatabaseReference myRefBus = mDatabase.getReference(Constants.USER_DATA_REF).child(sessionVM.getUser());
        myRefBus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.nonNull(snapshot.child(Constants.ID_BUS).getValue())) {
                    idBus = Objects.requireNonNull(snapshot.child(Constants.ID_BUS).getValue()).toString();
                }
                if (Objects.nonNull(snapshot.child(Constants.ID_VIAJE).getValue())) {
                    idViaje = Objects.requireNonNull(snapshot.child(Constants.ID_VIAJE).getValue()).toString();
                }
                if (Objects.nonNull(snapshot.child(Constants.ID_COMPANIA).getValue())) {
                    idCompania = Objects.requireNonNull(snapshot.child(Constants.ID_COMPANIA).getValue()).toString();
                    tieneTransporte.setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error al leer el valor
                Log.d(TAG, Constants.MENSAJE_ERROR_BDD, error.toException());
            }
        });
    }

    public LiveData<Boolean> getTransporte() {
        return tieneTransporte;
    }

    /**
     * Metodo para actualizar los datos individuales del usuario en un transporte (en el nodo del transporte)
     */
    public void setCurrentClientData(UserCurrentData data) {
        if (sessionVM.getSessionExist()) {
            myRef = mDatabase.getReference(Constants.USER_DATA_REF).child(sessionVM.getUser());
            // Crea un mapa con los campos que quieres actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put(Constants.FECHA, data.getFecha());
            updates.put(Constants.ID_BUS, data.getIdBus());
            updates.put(Constants.ID_VIAJE, data.getIdViaje());
            updates.put(Constants.ID_COMPANIA, data.getIdCompania());
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
            updates.put(Constants.COORDENADAS, coordinates);
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
            DatabaseReference busRef = mDatabase.getReference(Constants.BUS_DATA_REF)
                    .child(idCompania)
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
        if (!Strings.isNullOrEmpty(idBus) && !Strings.isNullOrEmpty(idViaje) && !Strings.isNullOrEmpty(idCompania)) {
            DatabaseReference excesosRef = mDatabase.getReference(Constants.BUS_DATA_REF)
                    .child(idCompania)
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
    public void setEstadoDelExcesoDeVelocidad(Boolean status, String key) {
        DatabaseReference newRef = mDatabase.getReference(Constants.BUS_DATA_REF)
                .child(idCompania)
                .child(idBus)
                .child(Constants.CHILD_REPORTS)
                .child(ValidateData.getDateTime(false))
                .child(idViaje)
                .child(key);
        if (sessionVM.getSessionExist()) {
            // Crea un mapa con los campos que quieres actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put(Constants.DB_CHECK_EXCESO, status);
            // Actualiza los campos del usuario en la base de datos
            newRef.updateChildren(updates);
        }
    }

    /**
     * Metodo para guardar el reporte personalizado a la unidad de transporte
     */
    public void setReportePersonalizado(Map<String, Object> updates) {
        DatabaseReference personalReportRef = mDatabase.getReference(Constants.BUS_DATA_REF)
                .child(idCompania)
                .child(idBus)
                .child(Constants.CHILD_PERSONAL_REPORTS)
                .child(ValidateData.getDateTime(false))
                .child(idViaje);
        personalReportRef.updateChildren(updates);
    }

    public void setAlertaDePanico(double latitude, double longitude) {

        DatabaseReference alertaDePanicoRef = mDatabase.getReference(Constants.ALERT_PANIC_DATA);
        //nuevo registro con id aleatorio
        DatabaseReference newRef = alertaDePanicoRef.push();
        //datos a guardar
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.DB_LAT, latitude);
        updates.put(Constants.DB_LNG, longitude);
        updates.put(Constants.DB_ID_USUARIO, sessionVM.getUser());
        if (!Strings.isNullOrEmpty(idViaje) && !Strings.isNullOrEmpty(idBus) && !Strings.isNullOrEmpty(idCompania)) {
            updates.put(Constants.ID_VIAJE, idViaje);
            updates.put(Constants.ID_COMPANIA, idCompania);
            updates.put(Constants.ID_BUS, idBus);
        }
        //guarda los datos
        newRef.setValue(updates);

    }
}
