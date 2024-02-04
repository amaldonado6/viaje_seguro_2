package com.aamaldonado.viaje.seguro.utpl.tft.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.aamaldonado.viaje.seguro.utpl.tft.R;
import com.aamaldonado.viaje.seguro.utpl.tft.activities.navigation.MainSelectOptionActivity;
import com.aamaldonado.viaje.seguro.utpl.tft.activities.account.AccountManageActivity;
import com.aamaldonado.viaje.seguro.utpl.tft.databinding.ActivityMainBinding;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.SessionAccount;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; //viewBinding
    //tiempo muerto
    private final Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //animacion
        animation();
        /*inicializar los viewModel*/
        SessionAccount sessionVM = new SessionAccount();
        timeForAnimation(sessionVM.getSessionExist());
    }

    private void timeForAnimation(boolean sessionExist) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //conocer el estado de la session del usuario
                if (sessionExist) {
                    switchActivity("exist");
                } else {
                    switchActivity("empty");
                }
            }
        }, 2000);
    }

    private void animation() {
        //animacion del fondo out-in
        Animation animationScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        binding.maCardBackground.setAnimation(animationScale);
        animationScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.maLogo.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_out)); //animacion del logo in-out
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //ocultar el fondo
                binding.maCardBackground.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
    private void switchActivity(String value) {
        //redirigir a la actividad pertinente si existe una sesión activa
        //cambiar de actividad
        Intent intent;
        switch (value) {
            case "exist":
                intent = new Intent(MainActivity.this, MainSelectOptionActivity.class); //existen datos del usuario
                break;
            case "empty":
                intent = new Intent(MainActivity.this, AccountManageActivity.class); //no hay usuario
                break;
            default:
                return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}