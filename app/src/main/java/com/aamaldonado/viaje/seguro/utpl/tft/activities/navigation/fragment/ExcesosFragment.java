package com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentExcesosBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.model.sensors.Coordinates;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;
import com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.DbViewModel.DataBaseViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ExcesosFragment extends Fragment {

    private LinearLayout contenedorSwitches;
    private FragmentExcesosBinding binding;
    private Switch switchNuevo;
    DataBaseViewModel dataBaseViewModel;

    private Observer<Coordinates> observer;

    private ChildEventListener mChildEventListener;

    public ExcesosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseViewModel = new ViewModelProvider(requireActivity()).get(DataBaseViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExcesosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configLayout();
    }

    private void configLayout() {
        contenedorSwitches = binding.contenedorSwitches;
        //Observa los cambios de los excesos de velocidad
        List<Coordinates> excesosList = dataBaseViewModel.getExcesosList().getValue();
        if (excesosList != null) {
            for (Coordinates exceso : excesosList) {
                agregarSwitch(exceso);
            }
        }


        dataBaseViewModel.getExcesos().observe(requireActivity(), data -> {
            if(data != null){
                //agregarSwitch(data);
            }
        });

    }

    private void agregarSwitch(Coordinates coordinates){
        switchNuevo = new Switch(getContext());
        switchNuevo.setBackgroundResource(R.drawable.style_switch);
        switchNuevo.setTextColor(Color.WHITE);
        switchNuevo.setTypeface(null, Typeface.BOLD);
        switchNuevo.setText("\nExceso de velocidad: " + coordinates.getSpeed() + " Km/h\n");
        switchNuevo.setTag(coordinates.getIdExceso());
        // nuevo objeto LayoutParams
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // Configura los márgenes del LayoutParams
        layoutParams.setMargins(10, 10, 10, 10);
        // Aplica los LayoutParams al botón switch
        switchNuevo.setLayoutParams(layoutParams);
        // Crea un ColorStateList
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked}, // checked
                new int[] {-android.R.attr.state_checked}, // unchecked
        };
        int[] colors = new int[] {
                Color.WHITE, // The color for the checked state
                Color.LTGRAY, // The color for the unchecked state
        };
        ColorStateList myList = new ColorStateList(states, colors);
        // Configura el color del track y del thumb
        switchNuevo.setTrackTintList(myList);
        switchNuevo.setThumbTintList(myList);
        // Crear
        contenedorSwitches.addView(switchNuevo);

        //Accion del switchh
        switchNuevo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getContext(), "" + buttonView.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //DataHandler.getInstance().getExcesos2(rem);
    }
}