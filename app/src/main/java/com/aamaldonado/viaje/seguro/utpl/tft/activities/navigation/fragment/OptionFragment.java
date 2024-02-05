package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentOptionBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.sensors.LocationViewModel;

import java.util.Objects;


public class OptionFragment extends Fragment {

    private FragmentOptionBinding binding;



    public OptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOptionBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configLayout();

    }

    private void configLayout() {
        //redirect navigation between Fragments

        //Card mapa
        binding.cardMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToMapFragment());
            }
        });

        //QR
        binding.cardQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToCodigoQrFragment());
            }
        });

        //Reports
        binding.cardExcesos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToExcesosFragment());
            }
        });
    }
}