package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.data.UserDatabaseHelper;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

import java.io.FileNotFoundException;
import java.io.InputStream;

//ABOUT: Activity to view user account details and/or update them
public class AccountActivity extends AppCompatActivity {

    //View variables
    ImageView accountUserImageImageView;
    EditText accountUserFullNameEditText;
    EditText accountUsernameEditText;
    EditText accountPasswordEditText;
    EditText accountConfirmPasswordEditText;
    EditText accountPhoneNumberEditText;
    Button accountSaveChangesButton;

    //Data variables
    int userID;
    byte[] userImageBytesArray;
    String userFullName;
    String username;
    String password;
    String passwordConfirm;
    String phoneNumber;

    //Other variables
    String activeUsername;
    Bitmap userImageBitmap;

    //Request variable
    final int GALLERY_REQUEST = 100;

    //Database variable
    UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Obtain views
        accountUserImageImageView = (ImageView) findViewById(R.id.accountUserImageImageView);
        accountUserFullNameEditText = (EditText) findViewById(R.id.accountUserFullNameEditText);
        accountUsernameEditText = (EditText) findViewById(R.id.accountUsernameEditText);
        accountPasswordEditText = (EditText) findViewById(R.id.accountPasswordEditText);
        accountConfirmPasswordEditText = (EditText) findViewById(R.id.accountConfirmPasswordEditText);
        accountPhoneNumberEditText = (EditText) findViewById(R.id.accountPhoneNumberEditText);
        accountSaveChangesButton = (Button) findViewById(R.id.accountSaveChangesButton);

        accountUserImageImageView.setImageResource(R.drawable.add_image_icon);

        //Obtain the actively signed in user from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE);
        activeUsername = prefs.getString(Util.SHARED_PREF_ACTIVE_USERNAME, ""); //Obtain the active username

        //Retrieve user data from the User database and display them
        try {
            Cursor cursor = userDatabaseHelper.fetchUserList(); //Obtain the cursor for the database

            //Cursor to the FIRST entry in the User database
            if (cursor != null) {

                cursor.moveToFirst();//Move the cursor to the FIRST row of the database
                username = cursor.getString(3); //Obtain the username of the FIRST row of the database

                //If the username obtained from the first User in the database is the same as the active user
                if (username.equals(activeUsername)) {

                    //Obtain data from the first row of the database
                    userID = cursor.getInt(0);
                    userImageBytesArray = cursor.getBlob(1);
                    userFullName = cursor.getString(2);
                    password = cursor.getString(4);
                    phoneNumber = cursor.getString(5);

                    //Display the data in the Views
                    accountUserFullNameEditText.setText(userFullName);
                    accountUsernameEditText.setText(username);
                    accountPasswordEditText.setText(password);
                    accountConfirmPasswordEditText.setText(password);
                    accountPhoneNumberEditText.setText(phoneNumber);
                    accountUserImageImageView.setImageBitmap(Util.getBitmapFromBytesArray(userImageBytesArray));
                }
            }

            //Cursor to the rest of entries in the User database
            while (cursor.moveToNext()) {

                username = cursor.getString(3); //Obtain the username of the cursored row of the database

                //If the username obtained from the cursored User in the database is the same as the active user
                if (username.equals(activeUsername)) {

                    //Obtain data from the cursored row in the database
                    userID = cursor.getInt(0);
                    userImageBytesArray = cursor.getBlob(1);
                    userFullName = cursor.getString(2);
                    password = cursor.getString(4);
                    phoneNumber = cursor.getString(5);

                    //Display the data in the Views
                    accountUserFullNameEditText.setText(userFullName);
                    accountUsernameEditText.setText(username);
                    accountPasswordEditText.setText(password);
                    accountConfirmPasswordEditText.setText(password);
                    accountPhoneNumberEditText.setText(phoneNumber);
                    accountUserImageImageView.setImageBitmap(Util.getBitmapFromBytesArray(userImageBytesArray));
                }
            }
        }
        catch (Exception e) {
            Log.i("Error", "An error has occurred");
        }
    }

    //Listener to add a display picture
    public void accountAddDisplayPicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    //Check for results returned from the Gallery application
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (reqCode) {
                case GALLERY_REQUEST:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        userImageBitmap = BitmapFactory.decodeStream(imageStream);
                        accountUserImageImageView.setImageBitmap(userImageBitmap);

                        userImageBytesArray = Util.getBytesArrayFromBitmap(userImageBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        makeToast("Something went wrong!");
                    }
            }
        }else {
            makeToast("You haven't picked an image yet!");
        }
    }

    //Listener for the "Save Changes" Button
    public void saveChangesButton(View view) {

        //Obtain user entered data and save them to data variables
        userFullName = accountUserFullNameEditText.getText().toString();
        username = accountUsernameEditText.getText().toString();
        password = accountPasswordEditText.getText().toString();
        passwordConfirm = accountConfirmPasswordEditText.getText().toString();
        phoneNumber = accountPhoneNumberEditText.getText().toString();

        //If both passwords entered are the same
        if (password.equals(passwordConfirm)) {

            //Set up ContentValues to gather data and update a row in the User database
            ContentValues contentValues = new ContentValues();
            contentValues.put(Util.USER_IMAGE, userImageBytesArray);
            contentValues.put(Util.USER_FULL_NAME, userFullName);
            contentValues.put(Util.USERNAME, username);
            contentValues.put(Util.PASSWORD, password);
            contentValues.put(Util.PHONE_NUMBER, phoneNumber);

            //Update data according to the userID
            userDatabaseHelper.getWritableDatabase().update(Util.USER_TABLE_NAME, contentValues, Util.USER_ID + " = ?", new String[]{String.valueOf(userID)});

            //Determine whether or not the new entry exists in the User database
            boolean isChangesSaved = userDatabaseHelper.fetchUser(username, password);

            //If a Row ID exists, i.e. the data has been added to the SQLiteDatabase
            if (isChangesSaved) {

                //Store the active username into the Shared Preferences - to be retrieved in later activities
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE); //Create SharedPreference object to access hard drive
                SharedPreferences.Editor editor = sharedPreferences.edit(); //Create SharedPreferences.Editor to edit the SharedPreference
                editor.putString(Util.SHARED_PREF_ACTIVE_USERNAME, username); //Add the key-value pair for the active username to the SharedPreference
                editor.apply(); //Commit SharedPreferences changes to hard drive

                makeToast("Changes saved successfully");
            }
            //If a Row ID DOES NOT exist, i.e. the data was not added to the SQLiteDatabase
            else {
                makeToast("An error has occurred");
            }
        }
        else {
            makeToast("Two passwords do not match!");
        }
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
                Intent mainActivityIntent = new Intent(AccountActivity.this, HomeActivity.class);
                startActivity(mainActivityIntent);
                return true;
            case R.id.action_account:
                Intent accountActivityIntent = new Intent(AccountActivity.this, AccountActivity.class);
                startActivity(accountActivityIntent);
                return true;
            case R.id.action_orders:
                Intent myOrdersActivity = new Intent(AccountActivity.this, MyOrdersActivity.class);
                startActivity(myOrdersActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Make Toast
    public void makeToast(String message) {
        Toast.makeText(AccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}
