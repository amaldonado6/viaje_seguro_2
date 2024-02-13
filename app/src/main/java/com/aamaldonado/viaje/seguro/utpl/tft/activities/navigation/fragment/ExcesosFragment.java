package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentExcesosBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Coordinates;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.database.DataBaseViewModel;

import java.util.List;

public class ExcesosFragment extends Fragment {
    private FragmentExcesosBinding binding;
    private DataBaseViewModel dataBaseViewModel;

    private Observer<List<Coordinates>> observer;

    public ExcesosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inicializar el viewmodel
        dataBaseViewModel = new ViewModelProvider(requireActivity()).get(DataBaseViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExcesosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //configuraciones iniciales
        configLayout();
    }

    private void configLayout() {
        //Configurar el observer cuando se crea la vista
        observer = new Observer<List<Coordinates>>() {
            @Override
            public void onChanged(List<Coordinates> coordinates) {
                if (coordinates != null) {
                    binding.contenedorSwitches.removeAllViews(); //limpiar toda la lista de excesos
                    for (Coordinates exceso : coordinates) {
                        //Agregar un switch por cada exceso de velocidad
                        agregarSwitch(exceso);
                    }
                }
            }
        };
        dataBaseViewModel.getExcesosList().observe(requireActivity(), observer);
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private void agregarSwitch(Coordinates coordinates) {
        //configurar boton para obtener la ubicacion del exceso de velocidad
        Button btnMapExcesos = new Button(getContext());
        btnMapExcesos.setText(R.string.mostrar_ubicacion);
        btnMapExcesos.setBackgroundResource(com.google.firebase.database.collection.R.drawable.common_google_signin_btn_icon_light_normal_background);
        btnMapExcesos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(ExcesosFragmentDirections.actionExcesosFragmentToMapExcesoFragment((float) coordinates.getLat(), (float) coordinates.getLng()));
            }
        });
        int paddingVert = 30;
        int paddingHor = 80;
        btnMapExcesos.setPadding(paddingHor, paddingVert, paddingHor, paddingVert);
        // nuevo objeto LayoutParams
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // Configura los márgenes del LayoutParams
        btnMapExcesos.setLayoutParams(layoutParams);
        //configurar el switch
        Switch switchNuevo = new Switch(getContext());
        switchNuevo.setBackgroundResource(R.drawable.style_switch);
        switchNuevo.setTextColor(Color.WHITE);
        switchNuevo.setTypeface(null, Typeface.BOLD);
        switchNuevo.setText(getString(R.string.txt_excesos)
                .concat("\n")
                .concat(String.valueOf(coordinates.getSpeed()))
                .concat(getString(R.string.txt_kmh))
        );
        switchNuevo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        switchNuevo.setTag(coordinates.getIdExceso());
        switchNuevo.setGravity(Gravity.CENTER);
        //asignar el estado del reporte
        switchNuevo.setChecked(coordinates.getCheckExceso() != null ? coordinates.getCheckExceso() : Boolean.FALSE);
        // nuevo objeto LayoutParams
        LinearLayout.LayoutParams layoutParamsBtn = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // Configura los márgenes del LayoutParams
        layoutParamsBtn.setMargins(10, 70, 10, 10);
        // Aplica los LayoutParams al botón switch
        switchNuevo.setLayoutParams(layoutParamsBtn);
        // Crea un ColorStateList
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked}, // checked
                new int[]{-android.R.attr.state_checked}, // unchecked
        };
        int[] colors = new int[]{
                Color.WHITE, // The color for the checked state
                Color.LTGRAY, // The color for the unchecked state
        };
        ColorStateList myList = new ColorStateList(states, colors);
        // Configura el color del track y del thumb
        switchNuevo.setTrackTintList(myList);
        switchNuevo.setThumbTintList(myList);
        // asignar switch
        binding.contenedorSwitches.addView(switchNuevo);
        //asignar boton
        binding.contenedorSwitches.addView(btnMapExcesos);

        //Accion del switchh
        switchNuevo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getContext(), getString(R.string.txt_exceso_reportado), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.txt_exceso_anulado), Toast.LENGTH_SHORT).show();
                }
                DataHandler.getInstance().setEstadoDelExcesoDeVelocidad(isChecked, String.valueOf(buttonView.getTag()));

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remover el observador
        dataBaseViewModel.getExcesosList().removeObserver(observer);
    }
}