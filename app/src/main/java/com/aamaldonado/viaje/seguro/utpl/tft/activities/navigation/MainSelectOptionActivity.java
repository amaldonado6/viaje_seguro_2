package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aamaldonado.viaje.seguro.utpl.tft.activities.MainActivity;
import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.ActivityMainSelectOptionBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.AuthProvider;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.service.BackgroundService;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.sensors.LocationViewModel;

public class MainSelectOptionActivity extends AppCompatActivity {

    private ActivityMainSelectOptionBinding binding;

    private LocationViewModel locationViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainSelectOptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //configuración del layout
        configLayout();
        iniciarLocation();
        Log.d("ERROR1","GG!!!");
    }

    private void configLayout() {
        //locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        //locationViewModel.startLocationUpdates(this);
        //Toast.makeText(this,"asd",Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, locationViewModel.getLocationData().getValue().getLatitude()+"asd", Toast.LENGTH_SHORT).show();

    }

    private void iniciarLocation() {
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainSelectOptionActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODA_LOCATION_PERMISSION
            );
        }else {
            //iniciar el servicio
            startLocationService();
        }
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            /*Intent intent = new Intent(this, BackgroundService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);*/
            locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
            locationViewModel.startLocationUpdates(this);
            Toast.makeText(this,"Localización activada",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(BackgroundService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        return  true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REQUEST_CODA_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else {
                Toast.makeText(this,"Permisos denegados",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logout() {
        AuthProvider authProvider = new AuthProvider();
        authProvider.logOut();
        Intent intent = new Intent(MainSelectOptionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

}