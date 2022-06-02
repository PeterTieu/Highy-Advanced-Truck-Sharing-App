package com.tieutech.highlyadvancedtrucksharingapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

//ABOUT: Utility class for the project
public class Util{

    //API Key
    public static final String API_KEY = "AIzaSyCz7_53ntdf1uUm6D74So04Bm4ICMsp3Aw";

    //Map Autocomplete
    public static final String AUTOCOMPLETE_PICKUP_LOCATION = "autocomplete_pickup_location";
    public static final String AUTOCOMPLETE_PICKUP_LOCATION_LATITUDE = "autocomplete_pickup_location_latitude";
    public static final String AUTOCOMPLETE_PICKUP_LOCATION_LONGITUDE = "autocomplete_pickup_location_longitude";
    public static final String AUTOCOMPLETE_DESTINATION = "autocomplete_destination";
    public static final String AUTOCOMPLETE_DESTINATION_LATITUDE = "autocomplete_destination_latitude";
    public static final String AUTOCOMPLETE_DESTINATION_LONGITUDE = "autocomplete_destination_longitude";

    //Map
    public static final int PICK_LOCATION_REQUEST = 200;
    public static final int PICK_DESTINATION_REQUEST = 300;

    //Shared Preferences
    public static final String SHARED_PREF_DATA = "shared_pref_data";
    public static final String SHARED_PREF_ACTIVE_USERNAME = "shared_pref_active_username";

    //User Database
    public static final int USER_DATABASE_VERSION = 1;
    public static final String USER_DATABASE_NAME = "user_db";
    public static final String USER_TABLE_NAME = "users_table";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_ID = "user_id";
    public static final String USER_FULL_NAME = "user_full_name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PHONE_NUMBER = "phone_number";

    //Order Database
    public static final int ORDER_DATABASE_VERSION = 1;
    public static final String ORDER_DATABASE_NAME = "order_db";
    public static final String ORDER_TABLE_NAME = "orders_table";

    //Order Database column names
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_SENDER_IMAGE = "sender_image";
    public static final String ORDER_SENDER_USERNAME = "sender_username";
    public static final String ORDER_RECEIVER_USERNAME = "receiver_username";
    public static final String ORDER_PICKUP_DATE = "order_pickup_date";
    public static final String ORDER_PICKUP_TIME = "order_pickup_time";
    public static final String ORDER_PICKUP_LOCATION = "order_pickup_location";
    public static final String ORDER_PICKUP_LOCATION_LATITUDE = "order_pickup_location_latitude";
    public static final String ORDER_PICKUP_LOCATION_LONGITUDE = "order_pickup_location_longitude";
    public static final String ORDER_DESTINATION = "order_destination";
    public static final String ORDER_DESTINATION_LATITUDE = "order_destination_latitude";
    public static final String ORDER_DESTINATION_LONGITUDE = "order_destination_longitude";
    public static final String ORDER_GOOD_TYPE = "order_good_type";
    public static final String ORDER_WEIGHT = "order_weight";
    public static final String ORDER_WIDTH = "order_width";
    public static final String ORDER_LENGTH = "order_length";
    public static final String ORDER_HEIGHT = "order_height";
    public static final String ORDER_VEHICLE_TYPE = "order_vehicle_type";
    public static final String ORDER_GOOD_DESCRIPTION = "order_good_description";
    public static final String ORDER_GOOD_IMAGE = "order_good_image";
    public static final String ORDER_GOOD_CLASSIFICATION = "order_good_classification";
    public static final String ORDER_GOOD_CLASSIFICATION_CONFIDENCE = "order_good_classification_confidence";

    //Order data for putExtra (for data passed between activities)
    public static final String DATA_SENDER_IMAGE = "data_sender_image";
    public static final String DATA_SENDER_NAME = "data_sender_name";
    public static final String DATA_RECEIVER_NAME = "data_receiver_name";
    public static final String DATA_PICKUP_DATE = "data_pickup_date";
    public static final String DATA_PICKUP_TIME = "data_pickup_time";
    public static final String DATA_PICKUP_LOCATION = "data_pickup_location";
    public static final String DATA_PICKUP_LOCATION_LATITUDE = "data_pickup_location_latitude";
    public static final String DATA_PICKUP_LOCATION_LONGITUDE = "data_pickup_location_longitude";
    public static final String DATA_DESTINATION = "data_destination";
    public static final String DATA_DESTINATION_LATITUDE = "data_destination_latitude";
    public static final String DATA_DESTINATION_LONGITUDE = "data_destination_longitude";
    public static final String DATA_GOOD_TYPE = "data_good_type";
    public static final String DATA_WEIGHT = "data_weight";
    public static final String DATA_WIDTH = "data_width";
    public static final String DATA_LENGTH = "data_length";
    public static final String DATA_HEIGHT = "data_weight";
    public static final String DATA_VEHICLE_TYPE = "data_vehicle_type";
    public static final String DATA_GOOD_IMAGE = "data_good_image";
    public static final String DATA_GOOD_CLASSIFICATION = "data_good_classification";
    public static final String DATA_GOOD_CLASSIFICATION_CONFIDENCE = "data_good_classification_confidence";
    public static final String DATA_GOOD_DESCRIPTION = "data_good_description";

    //Spinner data
    public static final int SPINNER_GOOD_TYPE = 1;
    public static final int SPINNER_VEHICLE_TYPE = 2;

    //Chat
    public static final String CHATS = "chats";
    public static final String USERS = "users";
    public static final String TIME_STAMP = "time_stamp";
    public static final String LAST_MESSAGE = "last_message";
    public static final String LAST_MESSAGE_TIME = "last_message_time";
    public static final String UNREAD_COUNT = "unread_count";

    //Message
    public static final String MESSAGE = "message";
    public static final String MESSAGE_ID = "message_ID";
    public static final String MESSAGE_TIME = "message_time";
    public static final String MESSAGE_SENDER = "message_sender";
    public static final String MESSAGES = "messages";

    // function to create a toast
    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //Convert from byte array to String
    public static String getStringFromByteArray(byte[] bytes) {
        String str;
        if (bytes != null ) {
            str = new String(bytes, StandardCharsets.UTF_8); // for UTF-8 encoding
        }
        else {
            str = "";
        }
        return str;
    }

    //Convert from Bitmap to byte array
    public static byte[] getBytesArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    //Convert from byte array to Bitmap
    public static Bitmap getBitmapFromBytesArray(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //Convert from Bitmap to Drawable
    public static Bitmap getBitmapFromDrawable(Context context, int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        return bitmap;
    }

    @SuppressLint("MissingPermission")
    public  static  boolean connectionAvailable(Context context){
        ConnectivityManager connectivityManager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager !=null && connectivityManager.getActiveNetwork() !=null)
        {
            return  connectivityManager.getActiveNetworkInfo().isAvailable();
        }
        else {
            return  false;
        }
    }
}
