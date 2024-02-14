package com.aamaldonado.viaje.seguro.utpl.tft.activities.account.navigationfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentValidatePhoneBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.AlertComponents;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.PhoneValidate;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

public class ValidatePhoneFragment extends Fragment {

    private TextInputEditText phoneB;
    private CountryCodePicker countryCodeB;
    private FragmentValidatePhoneBinding binding;

    public ValidatePhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentValidatePhoneBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*configuracion del layout*/
        configLayout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void configLayout() {
        //establecer componentes de acción interface
        phoneB = binding.fvpTxtInputPhone;
        countryCodeB = binding.fvpIdCountry;
        binding.fvpLinearHeader.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide));//animacion del header
        //accion click para el boton
        binding.fvpBtnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelRegister();
            }
        });
    }

    private void viewModelRegister() {
        //validar el numero de telefono ingresado
        boolean acceptPhone = PhoneValidate.dataModelPhone(phoneB.getEditableText().toString()); //verificar el numero ingresado
        if (acceptPhone) {
            /*si es correcto cambiar de actividad para poder enviar el codigo de verificación*/
            sendData();
        } else {
            /*si es incorrecto notificar al usuario*/
            AlertComponents alertDialog = new AlertComponents();
            String title = getString(R.string.warning);
            String description = getString(R.string.message_warning_register_phone);
            alertDialog.alertDialogMD(getActivity(),title,description).create().show();
        }
    }

    private void sendData() {
        //establecer datos de la interfaz para enviarlos al siguiente activity
        String strPhone = phoneB.getEditableText().toString();
        String srtCountryCode = countryCodeB.getSelectedCountryCodeWithPlus();

        NavController navController = Navigation.findNavController(requireView());
        NavDirections action = ValidatePhoneFragmentDirections.actionValidatePhoneFragmentToConfirmationCodeFragment(srtCountryCode+strPhone);
        navController.navigate(action);
    }
}