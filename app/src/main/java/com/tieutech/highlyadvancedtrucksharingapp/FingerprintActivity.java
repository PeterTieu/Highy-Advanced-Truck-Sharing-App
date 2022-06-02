package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

import java.util.concurrent.Executor;

//ABOUT: Activity to verify the user's fingerprint prior to proceeding to booking the Order
public class FingerprintActivity extends AppCompatActivity {

    //Data variables
    private double destinationLatitude, destinationLongitude;
    private double pickupLocationLatitude, pickupLocationLongitude;
    private String pickupLocation, destination;
    byte[] senderImageByteArray;
    String senderName;
    String pickupTime;
    String pickupDate;
    String receiverName;
    String goodType;
    String vehicleType;
    String weight;
    String height;
    String width;
    String length;
    byte[] goodImage;
    String goodClassification;
    double goodClassificationConfidence;

    //Other variable
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        // Initialising msgtext and loginbutton
        TextView msgtex = findViewById(R.id.msgtext);
        final Button verifyButton = findViewById(R.id.verifyButton);

        //Obtain data passed from MapsActivity
        Intent intent = getIntent();
        senderImageByteArray = intent.getByteArrayExtra(Util.DATA_SENDER_IMAGE);
        senderName = intent.getStringExtra(Util.DATA_SENDER_NAME);
        pickupDate = intent.getStringExtra(Util.DATA_PICKUP_DATE);
        pickupTime = intent.getStringExtra(Util.DATA_PICKUP_TIME);
        pickupLocation = intent.getStringExtra(Util.DATA_PICKUP_LOCATION);
        pickupLocationLatitude = intent.getDoubleExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, 0);
        pickupLocationLongitude = intent.getDoubleExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, 0);
        destination = intent.getStringExtra(Util.DATA_DESTINATION);
        destinationLatitude = intent.getDoubleExtra(Util.DATA_DESTINATION_LATITUDE, 0);
        destinationLongitude = intent.getDoubleExtra(Util.DATA_DESTINATION_LONGITUDE, 0);
        receiverName = intent.getStringExtra(Util.DATA_RECEIVER_NAME);
        goodType = intent.getStringExtra(Util.DATA_GOOD_TYPE);
        vehicleType = intent.getStringExtra(Util.DATA_VEHICLE_TYPE);
        weight = intent.getStringExtra(Util.DATA_WEIGHT);
        height = intent.getStringExtra(Util.DATA_HEIGHT);
        width = intent.getStringExtra(Util.DATA_WIDTH);
        length = intent.getStringExtra(Util.DATA_LENGTH);
        goodImage = intent.getByteArrayExtra(Util.DATA_GOOD_IMAGE);
        goodClassification = intent.getStringExtra(Util.DATA_GOOD_CLASSIFICATION);
        goodClassificationConfidence = intent.getDoubleExtra(Util.DATA_GOOD_CLASSIFICATION_CONFIDENCE, 0);

        //Create a variable for the BiometricManager
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);

        //Check if the user can use biometric sensor
        switch (biometricManager.canAuthenticate()) {

            //If the biometric sensor can be used
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("You can use the fingerprint sensor to verify");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            //If the device does not have the biometric sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("This device does not have a fingerprint sensor");
                verifyButton.setVisibility(View.GONE);
                break;

            //If the sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("The biometric sensor is currently unavailable");
                verifyButton.setVisibility(View.GONE);
                break;

            //If the device does not contain the fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("Your device doesn't have fingerprint saved,please check your security settings");
                verifyButton.setVisibility(View.GONE);
                break;
        }

        //Create a variable for the Executor
        Executor executor = ContextCompat.getMainExecutor(this);

        //Obtain a result from the authentication
        biometricPrompt = new BiometricPrompt(FingerprintActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            //Listener for a successful authentication
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Util.makeToast(getApplicationContext(), "Verification Successful"); //Display toast to indicate a successful authentication

                verifyButton.setText("Verification Successful"); //Display text to indicate a successful authentication

                //Start the CheckoutActivity
                Intent checkoutIntent = new Intent(FingerprintActivity.this, CheckoutActivity.class);

                //Send data to the OrderDetailsActivity
                checkoutIntent.putExtra(Util.DATA_SENDER_IMAGE, senderImageByteArray);
                checkoutIntent.putExtra(Util.DATA_SENDER_NAME, senderName);
                checkoutIntent.putExtra(Util.DATA_RECEIVER_NAME, receiverName);
                checkoutIntent.putExtra(Util.DATA_PICKUP_DATE, pickupDate);
                checkoutIntent.putExtra(Util.DATA_PICKUP_TIME, pickupTime);
                checkoutIntent.putExtra(Util.DATA_PICKUP_LOCATION, pickupLocation);
                checkoutIntent.putExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, pickupLocationLatitude);
                checkoutIntent.putExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, pickupLocationLongitude);
                checkoutIntent.putExtra(Util.DATA_DESTINATION, destination);
                checkoutIntent.putExtra(Util.DATA_DESTINATION_LATITUDE, destinationLatitude);
                checkoutIntent.putExtra(Util.DATA_DESTINATION_LONGITUDE, destinationLongitude);
                checkoutIntent.putExtra(Util.DATA_GOOD_TYPE, goodType);
                checkoutIntent.putExtra(Util.DATA_WEIGHT, weight);
                checkoutIntent.putExtra(Util.DATA_WIDTH, width);
                checkoutIntent.putExtra(Util.DATA_LENGTH, length);
                checkoutIntent.putExtra(Util.DATA_HEIGHT, height);
                checkoutIntent.putExtra(Util.DATA_VEHICLE_TYPE, vehicleType);
                checkoutIntent.putExtra(Util.DATA_GOOD_IMAGE, goodImage);
                checkoutIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION, goodClassification);
                checkoutIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION_CONFIDENCE, goodClassificationConfidence);

                startActivity(checkoutIntent); //Start the activity
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        //Variable for the Dialog to prompt the user to undergo fingerprint verification
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Fingerprint Verification").setDescription("Use your fingerprint to verify ").setNegativeButtonText("Cancel").build();

        //Listener for the
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    //Listener for the Verify Button
    public void verifyClick(View view) {
        biometricPrompt.authenticate(promptInfo);
    }

}