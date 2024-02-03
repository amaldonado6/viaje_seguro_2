package com.aamaldonado.viaje.seguro.utpl.tft.querylivedata.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreColletLiveData extends LiveData<Task<QuerySnapshot>> {
    private final Query query;
    private final String TAG = "error:";

    public FirestoreColletLiveData(Query query) {
        this.query = query; //query de la referencia
    }

    /*metodo escucha para captar el primer resultado*/
    OnCompleteListener<QuerySnapshot> listener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                setValue(task); //establecer el valor de la respuesta al liveData
            } else {
                Log.d(TAG, "Error getting documents firestore: ", task.getException());
            }

        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        query.get().addOnCompleteListener(listener); //cuando se inicie el live data establecer el listener de la base de datos
    }
}
