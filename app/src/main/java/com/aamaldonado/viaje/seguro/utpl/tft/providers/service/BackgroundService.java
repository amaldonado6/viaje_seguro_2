package com.aamaldonado.viaje.seguro.utpl.tft.providers.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class BackgroundService extends Service {

    //GlobalClass globalClass;
    //ManejadorBdd mBdd;
    //sensor acelerometro
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    int movimiento = 0;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("ERROR1","ENTRO!!!");
            //current location
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                //Log.e("Location", latitude + " ------- "+longitude);
                updateUiValues(locationResult);
            }
        }
    };

    private void updateUiValues(LocationResult locationResult) {
        Location location = locationResult.getLastLocation();
        try {
            assert location != null;
            if (location.hasSpeed()) {
                //obetener la velocidad en k/h
                double sp = ((location.getSpeed() * 3600) / 1000);

                /*globalClass.setLatitud(location.getLatitude());
                globalClass.setLongitud(location.getLongitude());
                globalClass.setVelocidad(sp);

                mBdd.datosRecorrido(globalClass);*/
            }


        } catch (Exception e) {
            System.out.println("error GetLAst" + e);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ERROR1","CREA!!!");
        try {
            //mBdd = ManejadorBdd.getInstance();
            //globalClass = ((GlobalClass) getApplicationContext());

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Servicio de Localización");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Activo");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId, "Servicio L.", NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("Canal del servicio de localización");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    };

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ERROR1","STARTCOM!!!");
        try {
            //acceder a los sensores
            sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //escuchar el acelerometro
            sensorListerner();
        }catch (Exception e){
            System.out.println("error movimiento;"+e);
        }
        if(intent != null){
            String action = intent.getAction();
            if(action != null){
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                }else if(action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)){
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void sensorListerner() {
        sensorEventListener= new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //detectar cambios
                if(event.values[0]<-30 || event.values[1]<-30 || event.values[2]<-30 && movimiento==0 ){

                    movimiento++;
                }else {
                    if(event.values[0]>30 || event.values[1]>30 || event.values[2]>30 || movimiento==1 ){

                        movimiento++;
                    }
                }
                if(movimiento==2){
                    //mBdd.datosMovimiento(globalClass);
                    movimiento=0;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        //registrar el evento
        try {
            sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_GAME);
        }catch (Exception e){
            System.out.println("Error game"+e);
        }
    }
}
