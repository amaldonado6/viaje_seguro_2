package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentOptionBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.database.DataBaseViewModel;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.sensors.LocationViewModel;

import java.util.Objects;


public class OptionFragment extends Fragment {

    private FragmentOptionBinding binding;

    private DataBaseViewModel dataBaseViewModel;
    private LocationViewModel locationViewModel;

    private long timerValue;



    public OptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseViewModel = new ViewModelProvider(requireActivity()).get(DataBaseViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
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
        dataBaseViewModel.loadData();
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
        binding.cardMapa.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToMapFragment()));
        //QR
        binding.cardQr.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToCodigoQrFragment()));
        //Reports
        binding.cardExcesos.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToExcesosFragment()));
        //Reportar conductor
        binding.cardReporte.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToReportFragment()));
        //boton info
        binding.cardInfo.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToInfoFragment()));
        //boton de panico
        binding.cardPanico.setOnClickListener(v -> {
            if(Objects.nonNull(locationViewModel.getLocationData().getValue())){
                double lat = Objects.requireNonNull(locationViewModel.getLocationData().getValue()).getLatitude();
                double lng = Objects.requireNonNull(locationViewModel.getLocationData().getValue()).getLongitude();

                if(timerValue > 0){
                    Toast.makeText(getActivity(), "Vuelva a intentarlo en: " + timerValue / 1000+" segundos", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Alerta enviada", Toast.LENGTH_LONG).show();
                    DataHandler.getInstance().setAlertaDePanico(lat, lng);
                    bloquearActivarBoton();
                }
            }
        });
    }

    /**
     * timer para volver a enviar una alerta
     * */
    private void bloquearActivarBoton() {
        binding.cardPanico.setCardBackgroundColor(Color.GRAY);
        //timer
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timerValue = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                timerValue = 0;
                binding.cardPanico.setCardBackgroundColor(Color.WHITE);
            }
        }.start();
    }
}