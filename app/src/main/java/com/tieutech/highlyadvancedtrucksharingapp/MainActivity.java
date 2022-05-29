package com.tieutech.highlyadvancedtrucksharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.data.UserDatabaseHelper;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

//ABOUT: Where the user enters their username and password to sign in
public class MainActivity extends AppCompatActivity {

    UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this); //Database
    String activeUsername; //Currently active username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtain Views
        EditText userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button signUpButton = (Button) findViewById(R.id.signUpButton);

        //loginButton Listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean result = userDatabaseHelper.fetchUser(userNameEditText.getText().toString(), passwordEditText.getText().toString());

                if (result == true) {

                    //Obtain the active username
                    activeUsername = userNameEditText.getText().toString();

                    //Store the active username into the Shared Preferences - to be retrieved in later activities
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE); //Create SharedPreference object to access hard drive
                    SharedPreferences.Editor editor = sharedPreferences.edit(); //Create SharedPreferences.Editor to edit the SharedPreference
                    editor.putString(Util.SHARED_PREF_ACTIVE_USERNAME, activeUsername); //Add the key-value pair for the active username to the SharedPreference
                    editor.apply(); //Commit SharedPreferences changes to hard drive

                    makeToast("Successfully Logged In!");
                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                }
                else {
                    makeToast("The user does not exist!");
                }
            }
        });

        //signUpButton Listener
        //Open up the SignUpActivity
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    //Make Toast
    public void makeToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}