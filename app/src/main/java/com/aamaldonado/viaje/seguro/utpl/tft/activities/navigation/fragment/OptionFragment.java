package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentOptionBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.database.DataBaseViewModel;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.sensors.LocationViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.Objects;


public class OptionFragment extends Fragment {

    private FragmentOptionBinding binding;

    private DataBaseViewModel dataBaseViewModel;
    private LocationViewModel locationViewModel;

    private SharedPreferences sharedPrefTutorial;

    private SharedPreferences.Editor editorTutorial;

    private long timerValue;

    private boolean tieneExcesos; //variable para controlar si el usuario presenta excesos de velocidad
    private boolean tieneTransporte; //variable para controlar si se esta registrado en un transporte

    public OptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseViewModel = new ViewModelProvider(requireActivity()).get(DataBaseViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        configPreferences(); //config the SharedPref
    }

    private void configPreferences() {
        sharedPrefTutorial = requireContext().getSharedPreferences(getString(R.string.preference_tutorial), MODE_PRIVATE);
        editorTutorial = sharedPrefTutorial.edit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOptionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //guia de usuario
        guiaDeUsuario();
    }

    private void guiaDeUsuario() {

        if (sharedPrefTutorial.getString(String.valueOf(R.string.tutorial_key), "").isEmpty()) {
            binding.counterEx.setVisibility(View.VISIBLE);
            new TapTargetSequence(requireActivity())
                    .targets(
                            TapTarget.forView(binding.cardReporte, Constants.GUIA_REPORTE)
                                    .transparentTarget(true),
                            TapTarget.forView(binding.cardMapa, Constants.GUIA_MAPA)
                                    .transparentTarget(true),
                            TapTarget.forView(binding.cardExcesos, Constants.GUIA_EXCESOS)
                                    .transparentTarget(true),
                            TapTarget.forView(binding.counterEx, Constants.GUIA_COUNTER)
                                    .transparentTarget(true),
                            TapTarget.forView(binding.cardQr, Constants.GUIA_QR)
                                    .transparentTarget(true),
                            TapTarget.forView(binding.cardInfo, Constants.GUIA_INFO)
                                    .transparentTarget(true),
                            TapTarget.forView(binding.cardPanico, Constants.GUIA_ALERTA)
                                    .transparentTarget(true)
                    )
                    .listener(new TapTargetSequence.Listener() {
                        @Override
                        public void onSequenceFinish() {
                            binding.counterEx.setVisibility(View.GONE);
                            editorTutorial.putString(String.valueOf(R.string.tutorial_key), "done");
                            editorTutorial.apply();
                            configLayout();
                            locationViewModel.setGuiaStatus(true);
                        }

                        @Override
                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                            //secuencia
                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {
                            //secuencia
                        }
                    }).start();
        } else {
            configLayout();
        }
    }

    private void configLayout() {
        tieneExcesos = false;
        observers();
        //redirect navigation between Fragments
        //Card mapa
        binding.cardMapa.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToMapFragment()));
        //QR
        binding.cardQr.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToCodigoQrFragment()));
        //Reports
        binding.cardExcesos.setOnClickListener(v -> {
            if (tieneExcesos) {
                Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToExcesosFragment());
            } else {
                Toast.makeText(getActivity(), getString(R.string.no_hay_excesos), Toast.LENGTH_LONG).show();
            }
        });
        //Reportar conductor
        binding.cardReporte.setOnClickListener(v -> {
            if(tieneTransporte){
                Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToReportFragment());
            }else{
                Toast.makeText(getActivity(), getString(R.string.debe_estar_registrado_trans), Toast.LENGTH_LONG).show();
            }

        });
        //boton info
        binding.cardInfo.setOnClickListener(v -> {
            if (Objects.nonNull(locationViewModel.getLocationData().getValue())) {
                Navigation.findNavController(requireView()).navigate(OptionFragmentDirections.actionOptionFragmentToInfoFragment());
            } else {
                Toast.makeText(getActivity(), getString(R.string.vuelva_a_intentarlo), Toast.LENGTH_LONG).show();
            }
        });
        //boton de panico
        binding.cardPanico.setOnClickListener(v -> {
            if (Objects.nonNull(locationViewModel.getLocationData().getValue())) {
                double lat = Objects.requireNonNull(locationViewModel.getLocationData().getValue()).getLatitude();
                double lng = Objects.requireNonNull(locationViewModel.getLocationData().getValue()).getLongitude();

                if (timerValue > 0) {
                    Toast.makeText(getActivity(), "Vuelva a intentarlo en: " + timerValue / 1000 + " segundos", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Alerta enviada", Toast.LENGTH_LONG).show();
                    DataHandler.getInstance().setAlertaDePanico(lat, lng);
                    bloquearActivarBoton();
                }
            }
        });
    }

    /**
     * Metodo para controlar los observables para el transportista y los excesos de velocidad
     * */
    private void observers() {
        //obtener los datos del transporte
        dataBaseViewModel.getTieneTransporte().observe(getViewLifecycleOwner(), observer -> {
            tieneTransporte = observer;
            dataBaseViewModel.loadData();
        });
        //Contador de exesos de velocidad en el cardview (solo si esta en un transporte)
        dataBaseViewModel.getExcesosList().observe(getViewLifecycleOwner(), observer -> {
            if (observer != null) {
                int countEx = observer.size();
                if (countEx == 0) {
                    binding.counterEx.setVisibility(View.GONE);
                } else {
                    tieneExcesos = true;
                    binding.counterEx.setVisibility(View.VISIBLE);
                    binding.counterEx.setText(String.valueOf(observer.size()));
                }
            }
        });
    }

    /**
     * timer para volver a enviar una alerta
     */
    private void bloquearActivarBoton() {
        binding.cardPanico.setCardBackgroundColor(Color.GRAY);
        //timer
        new CountDownTimer(60000, 1000) {

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