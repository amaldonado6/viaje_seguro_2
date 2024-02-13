package com.aamaldonado.viaje.seguro.utpl.tft.activities.account.navigationfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aamaldonado.viaje.seguro.utpl.tft.activities.MainActivity;
import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.FragmentConfirmationCodeBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.AuthProvider;
import com.aamaldonado.viaje.seguro.utpl.tft.utils.AlertComponents;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;

public class ConfirmationCodeFragment extends Fragment {
    private String extraPhone, strFullCode;
    private Button btnSendCode;
    private EditText etxtCode1, etxtCode2, etxtCode3, etxtCode4, etxtCode5, etxtCode6;
    private LinearLayout linearHeader;
    //variable de solicitud
    private String verificationID;
    //providers
    private AuthProvider authProvider;

    //view blinding
    private FragmentConfirmationCodeBinding binding;
    //.
    private AlertComponents alertComponents;
    private ProgressDialog progressDialog;

    public ConfirmationCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            authProvider = new AuthProvider(); //provider de autenticacion
            alertComponents = new AlertComponents(); //alertas en el layout
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        extraPhone = ConfirmationCodeFragmentArgs.fromBundle(getArguments()).getPhoneArg();//variable capturada del fragment anterior
        // Inflate the layout for this fragment
        binding = FragmentConfirmationCodeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //configuración del layout
        configLayout();
        //dialog de espera
        mShowDialog(true);
        //solicitar el codigo por sms del numero ingresado
        authProvider.sendCodeforVerification(extraPhone, callbacks, getActivity());
    }

    private void mShowDialog(boolean data) {
        if(Objects.isNull(progressDialog)){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.cargando));
        }
        if(data){
            progressDialog.show();
        }else {
            progressDialog.hide();
        }
    }

    private void configLayout() {
        //establecer los componentes activos de la interfaz
        btnSendCode = binding.ccBtnSendCode;
        etxtCode1 = binding.ccEdit1;
        etxtCode2 = binding.ccEdit2;
        etxtCode3 = binding.ccEdit3;
        etxtCode4 = binding.ccEdit4;
        etxtCode5 = binding.ccEdit5;
        etxtCode6 = binding.ccEdit6;
        linearHeader = binding.ccLinearHeader;
        linearHeader.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide));//animacion del header
        focusEdText();//saltar de un edidText a otro
        //accion click del boton
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtener el codigo completo
                strFullCode = etxtCode1.getText().toString() + etxtCode2.getText().toString() + etxtCode3.getText().toString() + etxtCode4.getText().toString() + etxtCode5.getText().toString() + etxtCode6.getText().toString();
                if (!strFullCode.equals("") && strFullCode.length() == 6 && extraPhone != null) {
                    singIn(strFullCode);
                } else {
                    alertComponents.setSnakbar(getView(),getString(R.string.cod_incompleto)); //snackBar
                }

            }
        });
    }

    private void singIn(String code) {
        mShowDialog(true);
        authProvider.signInPhone(verificationID, code).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mShowDialog(false);
                    //usuario ya esta logueado
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    mShowDialog(false);
                    alertComponents.setSnakbar(getView(),getString(R.string.loggin_fail));
                }
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //cuando ya se autenticó exitosamente
            String code = phoneAuthCredential.getSmsCode(); //obtiene el codigo del mensaje fisico
            if (code != null) {
                etxtCode1.setText(code);
                singIn(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            //falla de envio
            Log.d("error", "verification failed: " + e);
            mShowDialog(false);
            alertComponents.setSnakbar(getView(),"Error al enviar el mensaje\nIntentalo nuevamente más tarde");
            goBack();
        }

        private void goBack() {
            Navigation.findNavController(requireView()).navigate(R.id.validatePhoneFragment);
        }

        @Override
        public void onCodeSent(@NonNull String verification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verification, forceResendingToken);
            //cuando el codigo es solicitado por mensaje de texto
            mShowDialog(false);
            alertComponents.setSnakbar(getView(),getString(R.string.codigo_enviado));
            verificationID = verification;
        }
    };

    private void focusEdText() {
        etxtCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtCode1.getText().toString().length() == 1) {
                    etxtCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etxtCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtCode2.getText().toString().length() == 1) {
                    etxtCode3.requestFocus();
                }
                if (etxtCode2.getText().toString().length() == 0) {
                    etxtCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etxtCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtCode3.getText().toString().length() == 1) {
                    etxtCode4.requestFocus();
                }
                if (etxtCode3.getText().toString().length() == 0) {
                    etxtCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etxtCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtCode4.getText().toString().length() == 1) {
                    etxtCode5.requestFocus();
                }
                if (etxtCode4.getText().toString().length() == 0) {
                    etxtCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etxtCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtCode5.getText().toString().length() == 1) {
                    etxtCode6.requestFocus();
                }
                if (etxtCode5.getText().toString().length() == 0) {
                    etxtCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etxtCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtCode6.getText().toString().length() == 0) {
                    etxtCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}