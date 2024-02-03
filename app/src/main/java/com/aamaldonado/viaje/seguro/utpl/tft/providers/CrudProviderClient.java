package com.aamaldonado.viaje.seguro.utpl.tft.providers;

import com.aamaldonado.viaje.seguro.utpl.tft.model.account.DataClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CrudProviderClient {
    //variables de configuracion
    private FirebaseFirestore database;
    //constructor inicial que establece la instancia que apunta al nodo del conductor
    public CrudProviderClient(){
        database = FirebaseFirestore.getInstance();
    }
    //metodo para guardar los datos del conductor en la bdd
    /*public Task<Void> create(DataClient client){
        return  databaseReference.child(client.getId()).setValue(client);
    }*/
    //metodo para actualizar los datos del conductor
    public Task<Void> update(DataClient client){/*
        //para solo extraer datos especificos
        Map<String, Object> map = new HashMap<>();
        map.put("name", client.getName());
        map.put("image",client.getImage());
        map.put("email",client.getEmail());
        return  databaseReference.child(client.getId()).updateChildren(map);*/
        return null;
    }
    //metodo para obtener los datos del id especifico de un cliente
    public DocumentReference getClient (String idDriver){
        return database.collection("users").document(idDriver);
    }
}
