package com.aamaldonado.viaje.seguro.utpl.tft.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.aamaldonado.viaje.seguro.utpl.tft.common.Constants;
import com.aamaldonado.viaje.seguro.utpl.tft.providers.firebase.DataHandler;

public class SharedPreferencesData {

    SharedPreferences sharedPreferences;
    private static SharedPreferencesData INSTANCE;
    public static SharedPreferencesData getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SharedPreferencesData();
        }
        return INSTANCE;
    }
    private SharedPreferencesData(){}

    public void setData(Activity activity, String data){
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_ID,data);
        editor.apply();
    }

}
