package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aamaldonado.viaje.seguro.utpl.tft.activities.MainActivity;
import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.ActivityMainSelectOptionBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.AuthProvider;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.LocationPermissionChecker;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.service.BackgroundService;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.ValidateData;
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
        //Location viewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        //get location
        iniciarLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODA_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, getString(R.string.permiso_denegado), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void iniciarLocation() {
        LocationPermissionChecker permissionChecker = new LocationPermissionChecker(this);
        if (!permissionChecker.hasLocationPermission()) {
            permissionChecker.requestLocationPermission();
        } else {
            //iniciar el servicio
            startLocationService();
        }
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            locationViewModel.startLocationUpdates(this);
            //Observer
            //Asignar velocidad
            locationViewModel.getLocationData().observe(this, loc -> binding.velocidadActual.setText(String.valueOf(ValidateData.getSpeed(loc))));
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (BackgroundService.class.getName().equals(service.service.getClassName()) && (service.foreground)) {
                        return true;
                }
            }
            return false;
        }
        return false;
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
        getMenuInflater().inflate(R.menu.menu_options, menu);
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