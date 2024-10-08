package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

//ABOUT: The activity that displays all the available trucks
public class HomeActivity extends AppCompatActivity implements OrderRecyclerViewAdapter.OnOrderListener{

    //RecyclerView variables
    RecyclerView orderRecyclerView;
    OrderRecyclerViewAdapter orderRecyclerViewAdapter;

    //Add Button view variable
    ImageView addImageView;

    //Database variable
    OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);

    //List variables
    List<Order> allOrdersList = new ArrayList<>(); //List of all orders (includes sample orders and my orders)
    List<Order> myOrdersList = new ArrayList<>(); //List of my orders only

    //Text-to-speech variable
    private TextToSpeech textToSpeech;

    //======> SAMPLE ORDERS (data) <======
    //Image resources
    int[] imageList = {
            R.drawable.order_image_1,
            R.drawable.order_image_5,
    };

    String[] senderUsernameList = {
            "Sample Truck 1",
            "Sample Truck 2",
    };

    String[] receiverUsernameList = {
            "Sample Receiver 1",
            "Sample Receiver 2",
    };

    String[] goodDescriptionList = {
            "Sample Good Description 1",
            "Sample Good Description 2",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Obtain Views
        orderRecyclerView = (RecyclerView) findViewById(R.id.myOrderRecyclerView); //RecyclerView to contain Orders
        addImageView = (ImageView) findViewById(R.id.addImageView);

        addImageView.setImageResource(R.drawable.ic_add);

        //Instantiate all the sample Order objects and add them to the ordersArrayList
        for (int i = 0; i < imageList.length; i++) {
            Bitmap bitmap = Util.getBitmapFromDrawable(this, imageList[i]);
            byte[] byteImage = Util.getBytesArrayFromBitmap(bitmap);
            Order order = new Order(byteImage, senderUsernameList[i], receiverUsernameList[i], goodDescriptionList[i]);

            allOrdersList.add(order);
        }

        //Fetch the list of all Orders in the database of Orders
        List<Order> orderList = orderDatabaseHelper.fetchAllOrders();

        //Sift through all Order objects in the list of Orders
        for (Order order : orderList) {
            myOrdersList.add(order); //Add all the Order objects to the myOrdersList
        }

        //If myOrdersList is not empty - i.e. there exists some Orders added by the user
        if (!myOrdersList.isEmpty()) {
            allOrdersList.addAll(myOrdersList); //Add the myOrdersList to the allOrdersList
        }

        //RecyclerViewAdapter to link the RecyclerView for Orders to the data
        orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(allOrdersList, this, this, this, this); //Instantiate the Recyclerview Adapter
        orderRecyclerView.setAdapter(orderRecyclerViewAdapter); //Set the Adapter to the RecyclerView

        //LinearLayoutManager to set the layout of the RecyclerView (and make it horizontal)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        orderRecyclerView.setLayoutManager(layoutManager); //Link the LayoutManager to the RecyclerView

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
                Intent mainActivityIntent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(mainActivityIntent);
                return true;
            case R.id.action_account:
                Intent accountActivityIntent = new Intent(HomeActivity.this, AccountActivity.class);
                startActivity(accountActivityIntent);
                return true;
            case R.id.action_orders:
                Intent myOrdersActivity = new Intent(HomeActivity.this, MyOrdersActivity.class);
                startActivity(myOrdersActivity);
                return true;
            case R.id.action_logout:
                Intent mainActivity = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Listener for the button to add orders
    public void addOrder(View view) {
        Intent newOrderActivityIntent = new Intent(HomeActivity.this, NewOrder1Activity.class);
        startActivity(newOrderActivityIntent);
    }

    //Listener for the selection of an Order item
    @Override
    public void onOrderClick(int position) {

        //Begin intent to open OrderDetailsActivity
        Intent orderDetailsIntent = new Intent(HomeActivity.this, OrderDetailsActivity.class);

        //Send data to the OrderDetailsActivity
        orderDetailsIntent.putExtra(Util.DATA_SENDER_IMAGE, allOrdersList.get(position).getSenderImage());
        orderDetailsIntent.putExtra(Util.DATA_SENDER_NAME, allOrdersList.get(position).getSenderUsername());
        orderDetailsIntent.putExtra(Util.DATA_RECEIVER_NAME, allOrdersList.get(position).getReceiverUsername());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_DATE, allOrdersList.get(position).getOrderPickupDate());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_TIME, allOrdersList.get(position).getOrderPickupTime());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_LOCATION, allOrdersList.get(position).getOrderPickupLocation());
        orderDetailsIntent.putExtra(Util.DATA_DESTINATION, allOrdersList.get(position).getOrderDestination());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, allOrdersList.get(position).getOrderPickupLatitude());
        orderDetailsIntent.putExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, allOrdersList.get(position).getOrderPickupLongitude());
        orderDetailsIntent.putExtra(Util.DATA_DESTINATION_LATITUDE, allOrdersList.get(position).getOrderDestinationLatitude());
        orderDetailsIntent.putExtra(Util.DATA_DESTINATION_LONGITUDE, allOrdersList.get(position).getOrderDestinationLongitude());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_TYPE, allOrdersList.get(position).getGoodType());
        orderDetailsIntent.putExtra(Util.DATA_WEIGHT, allOrdersList.get(position).getOrderWeight());
        orderDetailsIntent.putExtra(Util.DATA_WIDTH, allOrdersList.get(position).getOrderWidth());
        orderDetailsIntent.putExtra(Util.DATA_LENGTH, allOrdersList.get(position).getOrderLength());
        orderDetailsIntent.putExtra(Util.DATA_HEIGHT, allOrdersList.get(position).getOrderHeight());
        orderDetailsIntent.putExtra(Util.DATA_VEHICLE_TYPE, allOrdersList.get(position).getOrderVehicleType());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_IMAGE, allOrdersList.get(position).getGoodImage());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION, allOrdersList.get(position).getGoodClassification());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION_CONFIDENCE, allOrdersList.get(position).getGoodClassificationConfidence());
        orderDetailsIntent.putExtra(Util.DATA_GOOD_DESCRIPTION, allOrdersList.get(position).getGoodDescription());

        //Open OrderDetailsActivity
        startActivity(orderDetailsIntent);
    }

    //Listener for the selection of a Share icon
    @Override
    public void onSpeakClick(int position) {
        String text = allOrdersList.get(position).getGoodDescription(); //Obtain the speech from the text
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    //Listener for the selection of a Share icon
    @Override
    public void onShareClick(int position) {

        //Start the intent to share details of the associated Order
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, allOrdersList.get(position).getGoodDescription());
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