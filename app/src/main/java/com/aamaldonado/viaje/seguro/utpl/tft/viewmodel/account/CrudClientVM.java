package com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.aamaldonado.viaje.seguro.utpl.tft.model.account.DataClient;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.CrudProviderClient;
import com.aamaldonado.viaje.seguro.utpl.tft.querylivedata.firestore.FireStoreDocLiveData;
import com.aamaldonado.viaje.seguro.utpl.tft.querylivedata.firestore.FireStoreSetDataLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class CrudClientVM extends ViewModel {
    private DocumentReference reference;
    private CrudProviderClient providerClient;
    private LiveData<Task<DocumentSnapshot>> liveDataCrud; //livedata de los datos obtenidos de la bbdd
    private LiveData<Boolean> liveDataSaveProfile; //livedata de los datos obtenidos de la bbdd

    public CrudClientVM() {
        /*inicializar el provider*/
        providerClient = new CrudProviderClient();
    }

    public LiveData<Task<DocumentSnapshot>> getDataClient() {
        if (liveDataCrud == null && reference != null) {
            liveDataCrud = new FireStoreDocLiveData(reference);
        }
        return liveDataCrud;
    }

    public void setDataClientRef(String idClient) {
        reference = providerClient.getClient(idClient);
    }

    public LiveData<Boolean> getSaveProfile(DataClient dataProfile){
        if(liveDataSaveProfile == null && reference != null){
            liveDataSaveProfile = new FireStoreSetDataLiveData(reference,dataProfile);
        }
        return liveDataSaveProfile;
    }
}
