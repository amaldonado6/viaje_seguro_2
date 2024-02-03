package com.aamaldonado.viaje.seguro.utpl.tft.providers;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthProvider {
    private FirebaseAuth firebaseAuth;
    /*constructor*/
    public  AuthProvider(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /*Method to get client ID
     * @return , client db uid (firebase)
     * */
    public String getClientId() {
        return firebaseAuth.getCurrentUser().getUid();
    }
    /*Method to get client phone number
     * @return , client db phone number (firebase)
     * */
    public String getClientNumber() {
        return firebaseAuth.getCurrentUser().getPhoneNumber(); }
    /*Method to close session */
    public void logOut(){
        firebaseAuth.signOut();
    }
    /*Method to verify that session are logged in
     * @return , session status
     * */
    public boolean existSession(){
        boolean exist= false;
        if(firebaseAuth.getCurrentUser() != null){
            exist= true;
        }
        return  exist;
    }
    /*Method to log in
     * @param verificationId , client id
     * @param code           , verification code for the new session
     * */
    public Task<AuthResult> signInPhone(String verificationId, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        return firebaseAuth.signInWithCredential(credential);
    }
    /*Method to sends a text message that containing the verification code
     * @param phone        , phone number
     * @param callbacks    , PhoneAuthProvider "callbacks"
     * @param getactivity  , activity that is running
     * */
    public void sendCodeforVerification(String phone, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks, FragmentActivity getactivity){
        firebaseAuth.setLanguageCode("es");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getactivity)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
