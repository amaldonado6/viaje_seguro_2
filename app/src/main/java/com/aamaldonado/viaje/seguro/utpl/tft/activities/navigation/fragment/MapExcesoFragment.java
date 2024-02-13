package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapExcesoFragment extends Fragment {

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            float lat = MapExcesoFragmentArgs.fromBundle(getArguments()).getLatitud();
            float lng = MapExcesoFragmentArgs.fromBundle(getArguments()).getLongitud();
            LatLng coordenadas = new LatLng(lat, lng);
            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(coordenadas).title(obtenerDireccion(lat, lng)))).showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 20));
        }
    };

    private String obtenerDireccion(float lat, float lng) {
        String direccionCompleta;
        //obtener la direccion
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException ioException) {
            // Error de red u otro error de E/S
        } catch (IllegalArgumentException illegalArgumentException) {
            // Latitud o longitud no válidas
        }
        // Maneja el caso en el que no se encontró ninguna dirección.
        if (addresses == null || addresses.size() == 0) {
            direccionCompleta = getString(R.string.txt_exceso_reportado);
        } else {
            Address address = addresses.get(0);
            // Aquí puedes obtener la dirección completa
            direccionCompleta = address.getAddressLine(0);
        }
        return direccionCompleta;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_exceso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapExcesos);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}