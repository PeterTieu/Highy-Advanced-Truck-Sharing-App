package com.tieutech.highlyadvancedtrucksharingapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.tieutech.highlyadvancedtrucksharingapp.model.Order;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

//ABOUT: Database class for Orders
public class OrderDatabaseHelper extends SQLiteOpenHelper {

    public OrderDatabaseHelper(@Nullable Context context) {
        super(context, Util.ORDER_DATABASE_NAME, null, Util.ORDER_DATABASE_VERSION);
    }

    //Create the database with SQL commands
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE "
                + Util.ORDER_TABLE_NAME + " ("

                + Util.ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + Util.ORDER_SENDER_IMAGE + " BLOB , "
                + Util.ORDER_SENDER_USERNAME + " TEXT , "
                + Util.ORDER_RECEIVER_USERNAME + " TEXT , "
                + Util.ORDER_PICKUP_DATE + " TEXT , "
                + Util.ORDER_PICKUP_TIME + " TEXT , "
                + Util.ORDER_PICKUP_LOCATION + " TEXT , "
                + Util.ORDER_PICKUP_LOCATION_LATITUDE + " REAL , "
                + Util.ORDER_PICKUP_LOCATION_LONGITUDE + " REAL , "
                + Util.ORDER_DESTINATION + " TEXT , "
                + Util.ORDER_DESTINATION_LATITUDE + " REAL , "
                + Util.ORDER_DESTINATION_LONGITUDE + " REAL, "
                + Util.ORDER_GOOD_TYPE + " TEXT , "
                + Util.ORDER_WEIGHT + " TEXT , "
                + Util.ORDER_WIDTH + " TEXT , "
                + Util.ORDER_LENGTH + " TEXT , "
                + Util.ORDER_HEIGHT + " TEXT , "
                + Util.ORDER_VEHICLE_TYPE + " TEXT , "
                + Util.ORDER_GOOD_DESCRIPTION + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    //2nd param: old version
    //3rd param: new version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        String DROP_USER_TABLE = "DROP TABLE IF EXISTS ";
        sqLiteDatabase.execSQL(DROP_USER_TABLE, new String[]{Util.ORDER_TABLE_NAME});

        onCreate(sqLiteDatabase); //Call onCreate(..) as defined above
    }

    //Called by listener of Create Order Button in NewOrder2Activity
    //Function: Inserts an order to the SQLiteDatabase, then returns the rowID
    public long insertOrder(Order order) {

        //NOTE: get-WRITABLE-Database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Gather all data to be inserted as a single row entry in the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.ORDER_SENDER_IMAGE, order.getSenderImage());
        contentValues.put(Util.ORDER_SENDER_USERNAME, order.getSenderUsername());
        contentValues.put(Util.ORDER_RECEIVER_USERNAME, order.getReceiverUsername());
        contentValues.put(Util.ORDER_PICKUP_DATE, order.getOrderPickupDate());
        contentValues.put(Util.ORDER_PICKUP_TIME, order.getOrderPickupTime());
        contentValues.put(Util.ORDER_PICKUP_LOCATION, order.getOrderPickupLocation());
        contentValues.put(Util.ORDER_PICKUP_LOCATION_LATITUDE, order.getOrderPickupLatitude());
        contentValues.put(Util.ORDER_PICKUP_LOCATION_LONGITUDE, order.getOrderPickupLongitude());
        contentValues.put(Util.ORDER_DESTINATION, order.getOrderDestination());
        contentValues.put(Util.ORDER_DESTINATION_LATITUDE, order.getOrderDestinationLatitude());
        contentValues.put(Util.ORDER_DESTINATION_LONGITUDE, order.getOrderDestinationLongitude());
        contentValues.put(Util.ORDER_GOOD_TYPE, order.getGoodType());
        contentValues.put(Util.ORDER_WEIGHT, order.getOrderWeight());
        contentValues.put(Util.ORDER_WIDTH, order.getOrderWidth());
        contentValues.put(Util.ORDER_LENGTH, order.getOrderLength());
        contentValues.put(Util.ORDER_HEIGHT, order.getOrderHeight());
        contentValues.put(Util.ORDER_VEHICLE_TYPE, order.getOrderVehicleType());
        contentValues.put(Util.ORDER_GOOD_DESCRIPTION, order.getGoodDescription());

        //Insert the all gathered data as a single row entry in to the database
        long rowId = sqLiteDatabase.insert(Util.ORDER_TABLE_NAME, null, contentValues);

        //Close the database
        sqLiteDatabase.close();

        //Return the row ID if insert was successful, and -1 if it failed
        return rowId;
    }


    // Called by listener of loginButton in MainActivity
    // Checks to see if the user exists in the SQLiteDatabase - and returns a boolean
    public boolean fetchOrder(String senderImage, String senderUsername, String receiverUsername, String goodDescription) {

        //NOTE: get-READABLE-Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                Util.ORDER_TABLE_NAME, //Table name
                new String[]{Util.ORDER_ID}, //Primary key column
                Util.ORDER_SENDER_IMAGE + "=? and "
                        + Util.ORDER_SENDER_USERNAME + "=? and "
                        + Util.ORDER_RECEIVER_USERNAME + "=? and "
                        + Util.ORDER_PICKUP_DATE + "=? and "
                        + Util.ORDER_PICKUP_TIME + "=? and "
                        + Util.ORDER_PICKUP_LOCATION + "=? and "
                        + Util.ORDER_PICKUP_LOCATION_LATITUDE + "=? and "
                        + Util.ORDER_PICKUP_LOCATION_LONGITUDE + "=? and "
                        + Util.ORDER_DESTINATION + "=? and "
                        + Util.ORDER_DESTINATION_LATITUDE + "=? and "
                        + Util.ORDER_DESTINATION_LONGITUDE + "=? and "
                        + Util.ORDER_GOOD_TYPE + "=? and "
                        + Util.ORDER_WEIGHT + "=? and "
                        + Util.ORDER_WIDTH + "=? and "
                        + Util.ORDER_LENGTH + "=? and "
                        + Util.ORDER_HEIGHT + "=? and "
                        + Util.ORDER_VEHICLE_TYPE + "=? and "
                        + Util.ORDER_GOOD_DESCRIPTION + "=?", //Columns to be identified
                new String[]{senderImage, senderUsername, receiverUsername, goodDescription}, //Values of the columns to match
                null, //Grouping of the rows
                null, //Filtering of the grows
                null); //Sorting of the order

        int numberOfRows = cursor.getCount(); //Get number of rows with the SAME values as username and password (most likely return just 1)

        sqLiteDatabase.close();

        if (numberOfRows > 0) {
            return true;
        }
        else {
            return false;
        }

    }

    //Obtain the Cursor for all data in the database
    public Cursor fetchOrderList() {

        //Obtain the column names
        String [] columns = new String[] {
                Util.ORDER_ID,
                Util.ORDER_SENDER_IMAGE,
                Util.ORDER_SENDER_USERNAME,
                Util.ORDER_RECEIVER_USERNAME,
                Util.ORDER_PICKUP_DATE,
                Util.ORDER_PICKUP_TIME,
                Util.ORDER_PICKUP_LOCATION,
                Util.ORDER_PICKUP_LOCATION_LATITUDE,
                Util.ORDER_PICKUP_LOCATION_LONGITUDE,
                Util.ORDER_DESTINATION,
                Util.ORDER_DESTINATION_LATITUDE,
                Util.ORDER_DESTINATION_LONGITUDE,
                Util.ORDER_GOOD_TYPE,
                Util.ORDER_WEIGHT,
                Util.ORDER_WIDTH,
                Util.ORDER_LENGTH,
                Util.ORDER_HEIGHT,
                Util.ORDER_VEHICLE_TYPE,
                Util.ORDER_GOOD_DESCRIPTION};

        //NOTE: get-READABLE-Database
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                Util.ORDER_TABLE_NAME,
                columns, // The array of columns to return (pass null to get all)
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
