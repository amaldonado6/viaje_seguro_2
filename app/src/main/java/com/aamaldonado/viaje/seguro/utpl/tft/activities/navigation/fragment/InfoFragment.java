package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentInfoBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.SessionAccount;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.ValidateData;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.sensors.LocationViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;

    private double lat = 0.0;
    private double lng = 0.0;
    private double speed = 0.0;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationViewModel locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        lat = Objects.requireNonNull(locationViewModel.getLocationData().getValue()).getLatitude();
        lng = Objects.requireNonNull(locationViewModel.getLocationData().getValue()).getLongitude();
        speed = ValidateData.getSpeed(locationViewModel.getLocationData().getValue());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configLayout();
    }

    private void configLayout() {
        binding.shareLatitud.setText(String.valueOf(lat));
        binding.shareLongitud.setText(String.valueOf(lng));
        binding.shareVelocidad.setText(String.valueOf(speed));
        binding.shareUbicacion.setText(obtenerUbicacion(lat, lng));
        //btn
        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartirUbicacion();
            }
        });
    }

    private void compartirUbicacion() {
        String uri = "http://maps.google.com/maps?saddr=" + lat + "," + lng;
        SessionAccount sessionVM = new SessionAccount();
        uri += getString(R.string.sharing_id).concat(sessionVM.getUser());
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, uri);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private String obtenerUbicacion(double latitude, double longitude) {
        String direccionCompleta;
        //obtener la direccion
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException | IllegalArgumentException ioException) {
            Log.d(Constants.TAG1, Objects.requireNonNull(ioException.getMessage()));
        }
        // Maneja el caso en el que no se encontró ninguna dirección.
        assert addresses != null;
        Address address = addresses.get(0);
        // Aquí puedes obtener la dirección completa
        direccionCompleta = address.getAddressLine(0);
        return direccionCompleta;
    }

}