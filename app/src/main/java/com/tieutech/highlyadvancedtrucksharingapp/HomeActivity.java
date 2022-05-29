package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.adapter.OrderRecyclerViewAdapter;
import com.tieutech.highlyadvancedtrucksharingapp.data.OrderDatabaseHelper;
import com.tieutech.highlyadvancedtrucksharingapp.model.Order;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;
import java.util.ArrayList;
import java.util.List;

//ABOUT: The activity that displays all the available trucks
public class HomeActivity extends AppCompatActivity implements OrderRecyclerViewAdapter.OnOrderListener{

    //RecyclerView variables
    RecyclerView orderRecyclerView;
    OrderRecyclerViewAdapter orderRecyclerViewAdapter;

    //Add Button view variable
    ImageView addImageView;

    //Database variable
    OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);

    //Data variables
    int orderID;
    byte[] senderImageBytesArray;
    String senderUsername;
    String receiverUsername;
    String pickupDate;
    String pickupTime;
    String pickupLocation;

    double pickupLocationLatitude;
    double pickupLocationLongitude;
    String destination;
    double destinationLatitude;
    double destinationLongitude;

    String goodType;
    String orderWeight;
    String orderWidth;
    String orderLength;
    String orderHeight;
    String orderVehicleType;
    String goodDescription;

    //List variables
    List<Order> allOrdersList = new ArrayList<>(); //List of all orders (includes sample orders and my orders)
    List<Order> myOrdersList = new ArrayList<>(); //List of my orders only

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

        //Obtain data from the OrderDataHelper database (database of my orders) and add them to myOrdersList
        try {
            Cursor cursor = orderDatabaseHelper.fetchOrderList(); //Obtain the cursor for the database

            //Cursor to the FIRST entry in the Order database
            if (cursor != null) {
                cursor.moveToFirst();

                //Obtain data from the first row of the database
                orderID = cursor.getInt(0);
                senderImageBytesArray = cursor.getBlob(1);
                senderUsername = cursor.getString(2);
                receiverUsername = cursor.getString(3);
                pickupDate = cursor.getString(4);
                pickupTime = cursor.getString(5);
                pickupLocation = cursor.getString(6);

                pickupLocationLatitude = cursor.getDouble(7);
                pickupLocationLongitude = cursor.getDouble(8);
                destination = cursor.getString(9);
                destinationLatitude = cursor.getDouble(10);
                destinationLongitude = cursor.getDouble(11);

                goodType = cursor.getString(12);
                orderWeight = cursor.getString(13);
                orderWidth = cursor.getString(14);
                orderLength = cursor.getString(15);
                orderHeight = cursor.getString(16);
                orderVehicleType = cursor.getString(17);
                goodDescription = cursor.getString(18);

                //Add all the obtained data from the FIRST order in the database to the myOrdersList
                myOrdersList.add(new Order(
                        senderImageBytesArray,
                        senderUsername,
                        receiverUsername,
                        goodDescription,
                        pickupDate,
                        pickupTime,
                        pickupLocation,

                        pickupLocationLatitude,
                        pickupLocationLongitude,
                        destination,
                        destinationLatitude,
                        destinationLongitude,

                        goodType,
                        orderWeight,
                        orderWidth,
                        orderLength,
                        orderHeight,
                        orderVehicleType));

                String str;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i=0; i<=13; i++) {
                    stringBuilder.append("cursor.getString(" + i + "),");
                }

            }

            //Cursor to the rest of the entries int he Order database
            while (cursor.moveToNext()) {

                //Obtain data from the first row of the database
                orderID = cursor.getInt(0);
                senderImageBytesArray = cursor.getBlob(1);
                senderUsername = cursor.getString(2);
                receiverUsername = cursor.getString(3);
                pickupDate = cursor.getString(4);
                pickupTime = cursor.getString(5);
                pickupLocation = cursor.getString(6);

                pickupLocationLatitude = cursor.getDouble(7);
                pickupLocationLongitude = cursor.getDouble(8);
                destination = cursor.getString(9);
                destinationLatitude = cursor.getDouble(10);
                destinationLongitude = cursor.getDouble(11);

                goodType = cursor.getString(12);
                orderWeight = cursor.getString(13);
                orderWidth = cursor.getString(14);
                orderLength = cursor.getString(15);
                orderHeight = cursor.getString(16);
                orderVehicleType = cursor.getString(17);
                goodDescription = cursor.getString(18);

                //Add all the obtained data from the FIRST order in the database to the myOrdersList
                myOrdersList.add(new Order(
                        senderImageBytesArray,
                        senderUsername,
                        receiverUsername,
                        goodDescription,
                        pickupDate,
                        pickupTime,
                        pickupLocation,

                        pickupLocationLatitude,
                        pickupLocationLongitude,
                        destination,
                        destinationLatitude,
                        destinationLongitude,

                        goodType,
                        orderWeight,
                        orderWidth,
                        orderLength,
                        orderHeight,
                        orderVehicleType));
            }
        }
        catch (Exception e) {
            Log.i("Error", "An error has occurred");
        }

        //If myOrdersList is not empty - i.e. there exists some Orders added by the user
        if (!myOrdersList.isEmpty()) {
            allOrdersList.addAll(myOrdersList); //Add the myOrdersList to the allOrdersList
        }

        //RecyclerViewAdapter to link the RecyclerView for Orders to the data
        orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(allOrdersList, this, this, this); //Instantiate the Recyclerview Adapter
        orderRecyclerView.setAdapter(orderRecyclerViewAdapter); //Set the Adapter to the RecyclerView

        //LinearLayoutManager to set the layout of the RecyclerView (and make it horizontal)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        orderRecyclerView.setLayoutManager(layoutManager); //Link the LayoutManager to the RecyclerView
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

        //Open OrderDetailsActivity
        startActivity(orderDetailsIntent);
    }

    //Listener for the selection of a Share icon
    @Override
    public void onShareClick(int position) {

        //Start the intent to share details of the associated Order
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, goodDescription);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}