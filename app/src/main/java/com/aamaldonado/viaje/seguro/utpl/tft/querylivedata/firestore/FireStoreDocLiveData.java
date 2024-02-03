package com.aamaldonado.viaje.seguro.utpl.tft.querylivedata.firestore;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class FireStoreDocLiveData extends LiveData<Task<DocumentSnapshot>> {
    private final DocumentReference ref;
    private final String TAG = "error:";

    public FireStoreDocLiveData(DocumentReference ref) {
        this.ref = ref; //query de la referencia
    }

    /*metodo escucha para captar el primer resultado*/

    OnCompleteListener<DocumentSnapshot> listener = new OnCompleteListener<DocumentSnapshot>() {

        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.isSuccessful()){
                setValue(task);
            }
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        ref.get().addOnCompleteListener(listener); //cuando se inicie el live data establecer el listener de la base de datos
    }
}
