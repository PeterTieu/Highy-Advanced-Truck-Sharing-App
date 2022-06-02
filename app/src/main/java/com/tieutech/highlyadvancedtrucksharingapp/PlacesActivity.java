package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

import java.util.Arrays;

//ABOUT: Activity to allow the user to select a Map location via Map autocompletion
public class PlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        //Initialise the places SDK
        Places.initialize(getApplicationContext(), Util.API_KEY);

        //Obtain the autocomplete fragment
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        //Aet fields of the location data
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        //Listener for the autocomplete
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            //Listener for error
            @Override
            public void onError(@NonNull Status status) {
                Util.makeToast(getApplicationContext(), "Error with API Key");
            }

            //listener for the selected location
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                //Obtain data of the selected location
                LatLng latLng = place.getLatLng();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                String placeName = place.getName();

                //Obtain the request code
                Intent intent1 = getIntent();
                int requestCode = intent1.getIntExtra("requestCode", 0);

                //Create intent to start (i.e. return) to NewOrder1Activity
                Intent intent2 = new Intent(getApplicationContext(), NewOrder1Activity.class);

                //If the request code is for the pickup location selection
                if (requestCode == Util.PICK_LOCATION_REQUEST)
                {
                    //Pack location data to the intent for NewOrder1Activity
                    intent2.putExtra(Util.AUTOCOMPLETE_PICKUP_LOCATION_LATITUDE, latitude);
                    intent2.putExtra(Util.AUTOCOMPLETE_PICKUP_LOCATION_LONGITUDE, longitude);
                    intent2.putExtra(Util.AUTOCOMPLETE_PICKUP_LOCATION, placeName);
                    setResult(RESULT_OK, intent2);
                    finish(); //Pop the activity off the stack
                }
                //If the request code is for the destination selection
                else if (requestCode == Util.PICK_DESTINATION_REQUEST)
                {
                    //Pack location data to the intent for NewOrder1Activity
                    intent2.putExtra(Util.AUTOCOMPLETE_DESTINATION_LATITUDE, latitude);
                    intent2.putExtra(Util.AUTOCOMPLETE_DESTINATION_LONGITUDE, longitude);
                    intent2.putExtra(Util.AUTOCOMPLETE_DESTINATION, placeName);
                    setResult(RESULT_OK, intent2);
                    finish(); //Pop the activity off the activity stack
                }
                else
                {
                    setResult(RESULT_CANCELED, intent2);
                    finish(); //Pop the activity off the activity stack
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}