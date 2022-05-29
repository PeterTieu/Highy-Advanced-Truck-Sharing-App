package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

//ABOUT: Activity 1 of 2 to create a new Order
public class NewOrder1Activity extends AppCompatActivity {

    //View variables
    EditText receiverNameEditText;
    CalendarView pickupDateCalendarView;
    TimePicker pickupTimeTimePicker;
    EditText pickupLocationEditText;
    EditText destinationEditText;

    //Data variables
    String receiverName;
    String pickupDate;
    String pickupTime;
    String pickupLocation = null;

    // variables for order details
    String destination = null;

    double locationLatitude, locationLongitude;
    double destinationLatitude, destinationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_1);

        //Obtain views
        receiverNameEditText = (EditText) findViewById(R.id.receiverNameEditText);
        pickupDateCalendarView = (CalendarView) findViewById(R.id.pickupDateCalendarView);
        pickupTimeTimePicker = (TimePicker) findViewById(R.id.pickupTimeTimePicker);
        pickupLocationEditText = (EditText) findViewById(R.id.pickupLocationEditText);
        destinationEditText = (EditText) findViewById(R.id.destinationEditText);


        //Listener for the CalendarView
        pickupDateCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Obtain the date data selected
                String  dayOfMonthSelected = String.valueOf(dayOfMonth);
                String  monthSelected = String.valueOf(month+1);
                String  yearSelected = String.valueOf(year);

                //Obtain a presentable String variable of the date data selected
                pickupDate = dayOfMonthSelected+"/"+monthSelected+"/"+yearSelected;
            }
        });

        //Listener for the TimePicker
        pickupTimeTimePicker.setOnTimeChangedListener( new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                pickupTime = String.valueOf(hourOfDay).toString() + ":" + String.valueOf(minute).toString();
            }
        });
    }

    //Listener for the "Pickup Location" EditText
    public void locationClick(View view)
    {
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.putExtra("requestCode", Util.PICK_LOCATION_REQUEST);
        startActivityForResult(intent, Util.PICK_LOCATION_REQUEST);
    }

    //Listener for the "Destination" EditText
    public void destinationClick(View view)
    {
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.putExtra("requestCode", Util.PICK_DESTINATION_REQUEST);
        startActivityForResult(intent, Util.PICK_DESTINATION_REQUEST);
    }

    //Check results returned from the locationClick and destinationClick
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Util.PICK_LOCATION_REQUEST && resultCode == RESULT_OK)
        {
            pickupLocation = data.getStringExtra(Util.AUTOCOMPLETE_PICKUP_LOCATION);
            pickupLocationEditText.setText(pickupLocation);
            locationLatitude = data.getDoubleExtra(Util.AUTOCOMPLETE_PICKUP_LOCATION_LATITUDE, 0);
            locationLongitude = data.getDoubleExtra(Util.AUTOCOMPLETE_PICKUP_LOCATION_LONGITUDE, 0);
        }
        else if (requestCode == Util.PICK_DESTINATION_REQUEST && resultCode == RESULT_OK)
        {
            destination = data.getStringExtra(Util.AUTOCOMPLETE_DESTINATION);
            destinationEditText.setText(destination);
            destinationLatitude = data.getDoubleExtra(Util.AUTOCOMPLETE_DESTINATION_LATITUDE, 0);
            destinationLongitude = data.getDoubleExtra(Util.AUTOCOMPLETE_DESTINATION_LONGITUDE, 0);
        }
    }

    //Listener for the Next button
    public void nextButtonClick(View view) {

        //Obtain the receiver name entered
        receiverName = receiverNameEditText.getText().toString();

        //Obtain the pickup date entered
        if (pickupDate == null) {
            Date date = new Date(pickupDateCalendarView.getDate());
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
            pickupDate = df2.format(date);
        }

        //Obtain the pickup Time entered
        if (pickupTime == null) {
            pickupTime = String.valueOf(pickupTimeTimePicker.getHour()) + ":" + String.valueOf(pickupTimeTimePicker.getMinute());
        }

//        //Obtain the receiver name entered
//        pickupLocation = pickupLocationEditText.getText().toString();


        // mandatory data
        if (receiverName == "" || pickupDate == null || pickupTime == null || pickupLocation == "" || destination == "")
        {
            Util.makeToast(getApplicationContext(), "Please fill in all required fields!");
        }
        //Start the NewOrder2Activity and send data to it
        Intent newOrderActivityIntent = new Intent(NewOrder1Activity.this, NewOrder2Activity.class);
        newOrderActivityIntent.putExtra(Util.DATA_RECEIVER_NAME, receiverName);
        newOrderActivityIntent.putExtra(Util.DATA_PICKUP_DATE, pickupDate);
        newOrderActivityIntent.putExtra(Util.DATA_PICKUP_TIME, pickupTime);


        newOrderActivityIntent.putExtra(Util.DATA_PICKUP_LOCATION, pickupLocation);
        newOrderActivityIntent.putExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, locationLatitude);
        newOrderActivityIntent.putExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, locationLongitude);
        newOrderActivityIntent.putExtra(Util.DATA_DESTINATION, destination);
        newOrderActivityIntent.putExtra(Util.DATA_DESTINATION_LATITUDE, destinationLatitude);
        newOrderActivityIntent.putExtra(Util.DATA_DESTINATION_LONGITUDE, destinationLongitude);

        startActivity(newOrderActivityIntent);
    }
}