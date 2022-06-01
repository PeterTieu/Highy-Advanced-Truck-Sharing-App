package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.data.UserDatabaseHelper;
import com.tieutech.highlyadvancedtrucksharingapp.model.User;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

//ABOUT: Allows the user to sign up/create a new account
public class SignUpActivity extends AppCompatActivity {

    //View variables
    ImageView userDisplayPictureImageView;
    EditText fullNameEditText;
    EditText signUpUserNameEditText;
    EditText signUpPasswordEditText;
    EditText signUpConfirmPasswordEditText;
    EditText phoneNumberEditText;
    Button saveButton;

    //Database variable
    UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this);

    //Data variables
    byte[] userImageBytes;
    String userFullName;
    String userName;
    String password;
    String phoneNumber;
    Bitmap userImageBitmap;

    //Request variable
    final int GALLERY_REQUEST = 100;
    public static final int AUTOFILL_REQUEST = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Obtain views
        userDisplayPictureImageView = (ImageView) findViewById(R.id.accountUserImageImageView);
        userDisplayPictureImageView.setImageResource(R.drawable.add_image_icon);
        fullNameEditText = (EditText) findViewById(R.id.accountUserFullNameEditText);
        signUpUserNameEditText = (EditText) findViewById(R.id.accountUsernameEditText);
        signUpPasswordEditText = (EditText) findViewById(R.id.accountPasswordEditText);
        signUpConfirmPasswordEditText = (EditText) findViewById(R.id.accountConfirmPasswordEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.accountPhoneNumberEditText);
        saveButton = (Button) findViewById(R.id.accountSaveChangesButton);

        //Onclick listener for the "Save" button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userFullName = fullNameEditText.getText().toString();
                userName = signUpUserNameEditText.getText().toString();
                password = signUpPasswordEditText.getText().toString();
                String confirmPassword = signUpConfirmPasswordEditText.getText().toString();
                phoneNumber = phoneNumberEditText.getText().toString();

                //If the password entries match
                if (password.equals(confirmPassword)) {
                    long rowID = userDatabaseHelper.insertUser(new User(userImageBytes, userFullName, userName, password, phoneNumber)); //Insert the new user to the userDatabaseHelper

                    //If a Row ID exists, i.e. the data has been added to the SQLiteDatabase
                    if (rowID > 0) {
                        Toast.makeText(SignUpActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    }
                    //If a Row ID DOES NOT exist, i.e. the data was not added to the SQLiteDatabase
                    else {
                        Toast.makeText(SignUpActivity.this, "Registration error!", Toast.LENGTH_SHORT).show();
                    }
                }
                //If the password entries do not match
                else {
                    Toast.makeText(SignUpActivity.this, "Two passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Make toast
    public void makeToast(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    //Listener for the ImageView to add a display picture
    public void addDisplayPicture(View view) {
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
                        userDisplayPictureImageView.setImageBitmap(userImageBitmap);

                        userImageBytes = Util.getBytesArrayFromBitmap(userImageBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        makeToast("Something went wrong!");
                    }
            }
        }else {
            makeToast("You haven't picked an image yet!");
        }

        if (reqCode == AUTOFILL_REQUEST)
        {
            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());
                FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                        .getOnDeviceTextRecognizer();
                Task<FirebaseVisionText> firebaseVisionTextTask = textRecognizer.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText result) {
                                for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
                                    for (FirebaseVisionText.Line line: block.getLines()) {
                                        int lineCount = line.getElements().size();
                                        int index = 0;
                                        while (index < lineCount) {
                                            FirebaseVisionText.Element element = line.getElements().get(index);
                                            String text = element.getText().toLowerCase(Locale.ROOT);

                                            if (text.contains("name") || element.getText().contains("Name") || element.getText().contains("NAME")) {
                                                for (int i = index + 1; i < lineCount - 1; i++) {
                                                    fullNameEditText.append(line.getElements().get(i).getText() + " ");
                                                }
                                                fullNameEditText.append(line.getElements().get(lineCount - 1).getText());
                                                break;
                                            }
                                            else if (text.contains("user") || element.getText().contains("User") || element.getText().contains("USER")) {
                                                for (int i = index + 1; i < lineCount - 1; i++) {
                                                    signUpUserNameEditText.append(line.getElements().get(i).getText() + " ");
                                                }
                                                signUpUserNameEditText.append(line.getElements().get(lineCount - 1).getText());
                                                break;
                                            }
                                            else if (text.contains("phone") || element.getText().contains("Phone") || element.getText().contains("PHONE") || element.getText().contains("Ph")) {
                                                phoneNumberEditText.append(line.getElements().get(index+1).getText());
                                                break;
                                            }
                                            else {
                                                index++;
                                            }
                                        }
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Util.makeToast(getApplicationContext(), "an error has occurred!");
                                    }
                                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void autofillFieldsClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), AUTOFILL_REQUEST);
    }
}