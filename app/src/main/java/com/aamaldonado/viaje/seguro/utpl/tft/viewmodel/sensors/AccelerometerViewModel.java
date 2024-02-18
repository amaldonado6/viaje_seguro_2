package com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.sensors;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Accelerometer;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.service.AccelerometerService;

public class AccelerometerViewModel extends ViewModel {
    private LiveData<Accelerometer> sensorData;
    private AccelerometerService sensorService;

    public LiveData<Accelerometer> getSensorData() {
        return sensorData;
    }

    /**
     * Metodo para iniciar el servicio del sensor
     * */
    public void initAccelerometerViewModel(Context context) {
        sensorService = new AccelerometerService(context);
        cargarDatos();
    }

    /**
     * Metodo para obtener los datos obtenidos por el sensor
     * */
    private void cargarDatos() {
        // Inicia el escucha de los sensores
        sensorService.iniciarEscucha();

        // Actualiza el LiveData con los datos del sensor
        sensorData = sensorService.getSensorDataAcce();
    }
}
