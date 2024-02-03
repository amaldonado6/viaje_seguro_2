package com.aamaldonado.viaje.seguro.utpl.tft.viewmodel.account;

import androidx.lifecycle.ViewModel;

import com.aamaldonado.viaje.seguro.utpl.tft.utils.PhoneValidate;

public class ValidatePhoneVM extends ViewModel {
    private final PhoneValidate registerUserModel;
    private String phoneNumber;

    public ValidatePhoneVM() {
        registerUserModel = new PhoneValidate();
    }

    public boolean validatePhoneNumber() {
        return registerUserModel.dataModelPhone(phoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
