package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentReportBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.google.common.base.Strings;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;


    public ReportFragment() {
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
        binding = FragmentReportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configLayout();
    }

    private void configLayout() {
        binding.btnRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarReporte();
            }
        });
    }

    private void guardarReporte() {
        Toast.makeText(requireActivity(), getText(R.string.reporte_enviado), Toast.LENGTH_SHORT).show();
        ArrayList<String> reports = new ArrayList<>();
        if (binding.radio11.isChecked()) {
            reports.add("r1");
        }
        if (binding.radio22.isChecked()) {
            reports.add("r2");
        }
        if (binding.radio33.isChecked()) {
            reports.add("r3");
        }
        if (binding.radio44.isChecked()) {
            reports.add("r4");
        }
        if (binding.radio55.isChecked()) {
            reports.add("r5");
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("reports", reports);
        if (!Strings.isNullOrEmpty(binding.txtTv.getText().toString())) {
            updates.put("comment", binding.txtTv.getText().toString());
        }
        DataHandler.getInstance().setReportePersonalizado(updates);
    }
}