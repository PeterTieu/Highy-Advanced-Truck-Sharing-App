package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tieutech.highlyadvancedtrucksharingapp.adapter.OrderRecyclerViewAdapter;
import com.tieutech.highlyadvancedtrucksharingapp.data.OrderDatabaseHelper;
import com.tieutech.highlyadvancedtrucksharingapp.model.Order;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//ABOUT: Activity to display ONLY the Orders that the user created
public class MyOrdersActivity extends AppCompatActivity implements OrderRecyclerViewAdapter.OnOrderListener{

    //View variables
    RecyclerView myOrderRecyclerView;
    OrderRecyclerViewAdapter myOrderRecyclerViewAdapter;

    //List variables
    List<Order> myOrdersArrayList = new ArrayList<>();
    List<Order> dbOrderList = new ArrayList<>();

    //Database variable
    OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);

    //Text-to-speech variable
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        //Obtain Views
        myOrderRecyclerView = (RecyclerView) findViewById(R.id.myOrderRecyclerView); //RecyclerView to contain Orders

        //Fetch the list of all Orders in the database of Orders
        List<Order> orderList = orderDatabaseHelper.fetchAllOrders();

        //Sift through all Order objects in the list of Orders
        for (Order order : orderList) {
            myOrdersArrayList.add(order); //Add all the Order objects to the myOrdersList
        }

        //If myOrdersList is not empty - i.e. there exists some Orders added by the user
        if (!dbOrderList.isEmpty()) {
            myOrdersArrayList.addAll(dbOrderList); //Add the myOrdersList to the allOrdersList
        }
        //RecyclerViewAdapter to link the RecyclerView for Orders to the data
        myOrderRecyclerViewAdapter = new OrderRecyclerViewAdapter(myOrdersArrayList, this, this, this, this); //Instantiate the Recyclerview Adapter
        myOrderRecyclerView.setAdapter(myOrderRecyclerViewAdapter); //Set the Adapter to the RecyclerView

        //LinearLayoutManager to set the layout of the RecyclerView (and make it horizontal)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        myOrderRecyclerView.setLayoutManager(layoutManager); //Link the LayoutManager to the RecyclerView

        //Instantiate Text to Speech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            //Upon the initialisation of the TextToSpeech implementation
            @Override
            public void onInit(int status) {

                //If the status of the initialisation of the TextToSpeech implementation is correct
                if (status == TextToSpeech.SUCCESS) {

                    int result = textToSpeech.setLanguage(Locale.ENGLISH); //Set language to English

                    //If the language is not found or supported
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                }
                else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    //Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Define actions for selected options menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent mainActivityIntent = new Intent(MyOrdersActivity.this, HomeActivity.class);
                startActivity(mainActivityIntent);
                return true;
            case R.id.action_account:
                Intent accountActivityIntent = new Intent(MyOrdersActivity.this, AccountActivity.class);
                startActivity(accountActivityIntent);
                return true;
            case R.id.action_orders:
                Intent myOrdersActivity = new Intent(MyOrdersActivity.this, MyOrdersActivity.class);
                startActivity(myOrdersActivity);
                return true;
            case R.id.action_logout:
                Intent mainActivity = new Intent(MyOrdersActivity.this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Listener for the selection of an Order item
    @Override
    public void onOrderClick(int position) {

        //Begin intent to open OrderDetailsActivity
        Intent orderDetailsIntent = new Intent(MyOrdersActivity.this, OrderDetailsActivity.class);

        //Send data to the OrderDetailsActivity
        orderDetailsIntent.putExtra(Util.DATA_SENDER_IMAGE, myOrdersArrayList.get(position).getSenderImage());
        orderDetailsIntent.putExtra(Util.DATA_SENDER_NAME, myOrdersArrayList.get(position).getSenderUsername());
        orderDetailsIntent.putExtra(Util.DATA_RECEIVER_NAME, myOrdersArrayList.get(position).getReceiverUsername());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_DATE, myOrdersArrayList.get(position).getOrderPickupDate());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_TIME, myOrdersArrayList.get(position).getOrderPickupTime());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_LOCATION, myOrdersArrayList.get(position).getOrderPickupLocation());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, myOrdersArrayList.get(position).getOrderPickupLatitude());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, myOrdersArrayList.get(position).getOrderPickupLongitude());
        orderDetailsIntent.putExtra(Util.DATA_DESTINATION, myOrdersArrayList.get(position).getOrderDestination());
        orderDetailsIntent.putExtra(Util.DATA_DESTINATION_LATITUDE, myOrdersArrayList.get(position).getOrderDestinationLatitude());
        orderDetailsIntent.putExtra(Util.DATA_DESTINATION_LONGITUDE, myOrdersArrayList.get(position).getOrderDestinationLongitude());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_TYPE, myOrdersArrayList.get(position).getGoodType());
        orderDetailsIntent.putExtra(Util.DATA_WEIGHT, myOrdersArrayList.get(position).getOrderWeight());
        orderDetailsIntent.putExtra(Util.DATA_WIDTH, myOrdersArrayList.get(position).getOrderWidth());
        orderDetailsIntent.putExtra(Util.DATA_LENGTH, myOrdersArrayList.get(position).getOrderLength());
        orderDetailsIntent.putExtra(Util.DATA_HEIGHT, myOrdersArrayList.get(position).getOrderHeight());
        orderDetailsIntent.putExtra(Util.DATA_VEHICLE_TYPE, myOrdersArrayList.get(position).getOrderVehicleType());
        orderDetailsIntent.putExtra(Util.DATA_LENGTH, myOrdersArrayList.get(position).getOrderLength());
        orderDetailsIntent.putExtra(Util.DATA_HEIGHT, myOrdersArrayList.get(position).getOrderHeight());
        orderDetailsIntent.putExtra(Util.DATA_VEHICLE_TYPE, myOrdersArrayList.get(position).getOrderVehicleType());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_IMAGE, myOrdersArrayList.get(position).getGoodImage());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION, myOrdersArrayList.get(position).getGoodClassification());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION_CONFIDENCE, myOrdersArrayList.get(position).getGoodClassificationConfidence());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_DESCRIPTION, myOrdersArrayList.get(position).getGoodDescription());

        //Open OrderDetailsActivity
        startActivity(orderDetailsIntent);
    }

    //Listener for the selection of a Share icon
    @Override
    public void onSpeakClick(int position) {
        String text = myOrdersArrayList.get(position).getGoodDescription(); //Obtain the speech from the text
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    //Listener for the selection of a Share icon
    @Override
    public void onShareClick(int position) {

        //Start the intent to share details of the associated Order
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, myOrdersArrayList.get(position).getGoodDescription());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    protected void onDestroy() {

        //If the TextToSpeech exists, stop it and shut it down
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}
