package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentCodigoQrBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.model.account.UserCurrentData;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.CaptureQr;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.ValidateData;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Date;
import java.util.Objects;

public class CodigoQrFragment extends Fragment {

    private FragmentCodigoQrBinding binding;

    public CodigoQrFragment() {
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
        binding = FragmentCodigoQrBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configLayout();
        binding.txtQr.setText(R.string.escanea_un_registro);
        guardarDatos("");
    }

    private void configLayout() {
        binding.btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQr();
            }
        });
    }

    private void scanQr() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.escaner_de_codigo_qr));
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureQr.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
       if(result.getContents() != null){
           Toast.makeText(getActivity(), getString(R.string.datos_capturados), Toast.LENGTH_SHORT).show();
           guardarDatos(result.getContents());
       }
    });

    private void guardarDatos(String contents) {
        UserCurrentData userCurrentData = new UserCurrentData();
        userCurrentData.setFecha(ValidateData.getDate());
        userCurrentData.setIdBus("IB123");
        userCurrentData.setIdViaje("IV123");
        DataHandler.getInstance().setCurrentClientData(userCurrentData);
    }
}