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

    //Listener for the ImageView to add a display picture
    public void addDisplayPicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    //Check for results returned from the image selection application
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        //If the RESULT_OK result code is received from the image selection application
        if (resultCode == RESULT_OK) {

            //Switch through the different request codes
            switch (reqCode) {

                //Request code received for the image selected
                case GALLERY_REQUEST:
                    try {
                        final Uri imageUri = data.getData(); //Get the Uri of the selected image
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri); //Get the InputStream from the Uri of the selected image
                        userImageBitmap = BitmapFactory.decodeStream(imageStream); //Obtain the Bitmap of the InputStream of the selected image

                        userDisplayPictureImageView.setImageBitmap(userImageBitmap); //Set the ImageView of the user to the obtained bitmap
                        userImageBytes = Util.getBytesArrayFromBitmap(userImageBitmap); //Obtain the bytes array of the selected image
                    }
                    //Catch any FileNotFoundException (i.e. if the image is not found)
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Util.makeToast(this, "Something went wrong!");
                    }
            }
        }
        else {
            Util.makeToast(this, "You haven't picked an image yet!");
        }

        //Request code received for the auto-filling of the fields
        if (reqCode == AUTOFILL_REQUEST)
        {
            FirebaseVisionImage image; //Create FirebaseVisionImage

            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData()); //Obtain the image

                FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer(); //Create the FirebaseVisionTextRecognizer object

                //Start a task to process text recognition from the selected image
                Task<FirebaseVisionText> firebaseVisionTextTask = textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {

                    //Listener for the success of the task
                    @Override
                    public void onSuccess(FirebaseVisionText result) {

                        //Traverse all text blocks (i.e. blocks of texts in the selected image)
                        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {

                            //Traverse all the lines within a text block
                            for (FirebaseVisionText.Line line: block.getLines()) {

                                int lineCount = line.getElements().size(); //Count of the line
                                int index = 0; //Index of the line

                                //For as long as the index of the line is lower than the line count
                                while (index < lineCount) {
                                    FirebaseVisionText.Element element = line.getElements().get(index); //Get the elements in the line
                                    String text = element.getText().toLowerCase(Locale.ROOT); //Obtain lowercase characters of the entire line

                                    //If the line is related the name of the user
                                    if (text.contains("name") || element.getText().contains("Name") || element.getText().contains("NAME")) {
                                        //Traverse all the lines
                                        for (int i = index + 1; i < lineCount - 1; i++) {
                                            fullNameEditText.append(line.getElements().get(i).getText() + " "); //Get the elements of the line and add it to the fullNameEditText
                                        }
                                        fullNameEditText.append(line.getElements().get(lineCount - 1).getText());
                                        break;
                                    }

                                    //If the line is related the username of the user
                                    else if (text.contains("user") || element.getText().contains("User") || element.getText().contains("USER")) {
                                        //Traverse all the lines
                                        for (int i = index + 1; i < lineCount - 1; i++) {
                                            signUpUserNameEditText.append(line.getElements().get(i).getText() + " "); //Get the elements of the line and add it to the signUpUserNameEditText
                                        }
                                        signUpUserNameEditText.append(line.getElements().get(lineCount - 1).getText());
                                        break;
                                    }

                                    //If the line is related to the phone number of the user
                                    else if (text.contains("phone") || element.getText().contains("Phone") || element.getText().contains("PHONE") || element.getText().contains("Ph")) {
                                        phoneNumberEditText.append(line.getElements().get(index+1).getText()); //Get the elements of the line and add it to the phoneNumberEditText
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
                        //Listener for the failure of the text recognition autocomplete
                        .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Util.makeToast(getApplicationContext(), "an error has occurred!");
                            }
                        });
            }
            //Catch for any IOException
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Listener for the "Autofill fields using image" text
    public void autofillFieldsClick(View view) {
        Intent intent = new Intent(); //Create a new intent
        intent.setType("image/*"); //Set the type of the intent to pick images
        intent.setAction(Intent.ACTION_GET_CONTENT); //Set action to get content
        startActivityForResult(Intent.createChooser(intent, "Select Image"), AUTOFILL_REQUEST); //Start the activity and send the AUTOFILL_REQUEST request code
    }
}