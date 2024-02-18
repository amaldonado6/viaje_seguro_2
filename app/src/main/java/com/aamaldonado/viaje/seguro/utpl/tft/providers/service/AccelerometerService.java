package com.aamaldonado.viaje.seguro.utpl.tft.providers.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Accelerometer;

public class AccelerometerService {
    private final SensorManager sensorManager;
    private final Sensor acelerometro;
    private final Context context;
    private final MutableLiveData<Accelerometer> sensorData;
    private int movimiento = 0;
    private final Handler handler = new Handler();
    private boolean movimientoDetectado = false;

    public AccelerometerService(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorData = new MutableLiveData<>();
    }

    /**
     * Observable para mantener actualizados los datos
     * */
    public LiveData<Accelerometer> getSensorDataAcce() {
        return sensorData;
    }

    /**
     * Metodo escucha para las interacciones del sensor
     * */
    public void iniciarEscucha() {

        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //detectar movimiento fuerte cuando se mueve el eje en ambas direcciones
                if (event.values[0] < -30 || event.values[1] < -30 || event.values[2] < -30 && movimiento == 0) {
                    movimiento++;
                } else {
                    if (event.values[0] > 30 || event.values[1] > 30 || event.values[2] > 30 || movimiento == 1) {
                        movimiento++;
                    }
                }
                //asignar valores al reconocer un movimiento en ambas direcciones
                if (movimiento >= 2) {
                    movimiento = 0;
                    //Solo registrar el movimiento cada minuto
                    if(!movimientoDetectado){
                        Accelerometer accelerometer = new Accelerometer();
                        accelerometer.setEjeX(event.values[0]);
                        accelerometer.setEjeY(event.values[1]);
                        accelerometer.setEjeZ(event.values[2]);
                        sensorData.setValue(accelerometer);
                        movimientoDetectado = true;
                        //interaccion cada minuto
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                movimientoDetectado = false;
                            }
                        }, 60000);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //empty
            }
        };
        sensorManager.registerListener(sensorEventListener, acelerometro, SensorManager.SENSOR_DELAY_GAME);
    }

    public void detenerEscucha() {
        sensorManager.unregisterListener((SensorListener) context);
    }

}
