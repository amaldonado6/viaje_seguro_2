package com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.DbViewModel;

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
import java.util.Objects;

public class DataBaseViewModel extends ViewModel {

    private MutableLiveData<Coordinates> exceso;

    public DataBaseViewModel(){
        exceso = new MutableLiveData<>();
        loadData();
    }

    public LiveData<Coordinates> getExcesos(){
        return exceso;
    }

    /**
     * Metodo para cargar los datos de los excesos de velocidad e ir capturando continuamente nuevos excesos
     * */
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

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * metodo para asignar el valor del exeso capturado
     * */
    private void setExcesos(DataSnapshot snapshot) {
        String[] parts = Objects.requireNonNull(snapshot.getKey()).split(Constants.SEPARATE_CHARACTER);
        SessionAccount sessionAccount = new SessionAccount();
        //Solo agregar los excesos capturados del usuario
        if(parts[0].equals(sessionAccount.getUser())){
            Coordinates.getInstance().setLng(Double.valueOf(Objects.requireNonNull(snapshot.child(Constants.DB_LAT).getValue()).toString()));
            Coordinates.getInstance().setLng(Double.valueOf(Objects.requireNonNull(snapshot.child(Constants.DB_LNG).getValue()).toString()));
            Coordinates.getInstance().setSpeed(Double.valueOf(Objects.requireNonNull(snapshot.child(Constants.DB_SPEED).getValue()).toString()));
            Coordinates.getInstance().setCheckExceso(Boolean.valueOf(Objects.requireNonNull(snapshot.child(Constants.DB_CHECK_EXCESO).getValue()).toString()));
            Coordinates.getInstance().setIdExceso(snapshot.getKey());
            exceso.setValue(Coordinates.getInstance());
        }
    }
}
