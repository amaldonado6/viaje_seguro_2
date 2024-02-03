package com.aamaldonado.viaje.seguro.utpl.tft.querylivedata.firestore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.aamaldonado.viaje.seguro.utpl.tft.model.account.DataClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

public class FireStoreSetDataLiveData extends LiveData<Boolean> {
    private final DataClient data;
    private final DocumentReference ref;
    public FireStoreSetDataLiveData(DocumentReference ref, DataClient data) {
        this.ref = ref;//query de la referencia
        this.data = data;
    }

    /*metodo escucha para captar el primer resultado*/

    OnSuccessListener<Void> listener = new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            setValue(true);
        }
    };

    OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setValue(false);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        ref.set(data)
                .addOnSuccessListener(listener)
                .addOnFailureListener(failureListener); //cuando se inicie el live data establecer el listener de la base de datos
    }
}
