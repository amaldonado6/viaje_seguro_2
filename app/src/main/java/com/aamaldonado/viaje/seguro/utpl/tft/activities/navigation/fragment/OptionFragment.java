package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentOptionBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.DbViewModel.DataBaseViewModel;


public class OptionFragment extends Fragment {

    private FragmentOptionBinding binding;

    private DataBaseViewModel dataBaseViewModel;



    public OptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseViewModel = new ViewModelProvider(requireActivity()).get(DataBaseViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        //Contador de exesos de velocidad en el cardview
        dataBaseViewModel.getExcesosList().observe(getViewLifecycleOwner(),observer->{
            if(observer != null){
                int countEx = observer.size();
                if(countEx == 0){
                    binding.counterEx.setVisibility(View.GONE);
                }else{
                    binding.counterEx.setVisibility(View.VISIBLE);
                    binding.counterEx.setText(String.valueOf(observer.size()));
                }
            }
        });
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