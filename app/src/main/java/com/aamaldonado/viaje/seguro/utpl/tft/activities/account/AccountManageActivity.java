package com.aamaldonado.viaje.seguro.utpl.tft.activities.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.aamaldonado.viaje.seguro.utpl.tft.databinding.ActivityAccountManageBinding;

public class AccountManageActivity extends AppCompatActivity {

    ActivityAccountManageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountManageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}