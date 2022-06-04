package com.tieutech.highlyadvancedtrucksharingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.tieutech.highlyadvancedtrucksharingapp.adapter.MessageRecyclerViewAdapter;
import com.tieutech.highlyadvancedtrucksharingapp.model.Message;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;
import java.util.ArrayList;
import java.util.HashMap;

//ABOUT: Activity to display the message thread between a specific User and the Truck Driver
public class MessageActivity extends AppCompatActivity {

    //View variables
    private SwipeRefreshLayout messagesSwipeRefresh;
    private ImageView sendIconImageView;
    private EditText messageEditText;

    //RecyclerView variables
    private ArrayList<Message> messagesList = new ArrayList<>();
    private MessageRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView messagesRecyclerView;

    //Firebase Realtime Database variables
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRootReference;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    //Data variables
    private String loggedInUsername;
    private String driverUsername = "truck driver"; //Define the Truck Driver's user name as "truck driver"
    private String receiverUsername;

    //Other variables
    private int currentPage = 1; //Key for the current page - used to refresh the page
    private static final int RECORD_PER_PAGE = 25; //Number of records in the page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Obtain views
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView); //RecyclerView to display messages in the message thread
        messagesSwipeRefresh = findViewById(R.id.messagesSwipeRefresh); //Swipe refresh
        messageEditText = findViewById(R.id.enterMessageEditText); //EditText to enter a message
        sendIconImageView = findViewById(R.id.sendIconImageView); //Icon to send the message

        // get the logged in user's username from shared preferences and assign it to the respective variable
        SharedPreferences prefs = getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE);
        loggedInUsername = prefs.getString(Util.SHARED_PREF_ACTIVE_USERNAME, "");

        //If the user logged in is "truck driver"
        if (loggedInUsername.equals("truck driver"))
        {
            Intent intent = getIntent();
            receiverUsername = intent.getStringExtra(Util.MESSAGE_SENDER); //Get the value of the receiver from the ChatRecyclerViewAdapter
        }
        //If the user logged in is anyone but the "truck driver"
        else
        {
            receiverUsername = driverUsername;
        }

        //Obtain the FirebaseAuthentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        //Obtain a reference to the FirebaseAuthentication instance
        mRootReference = FirebaseDatabase.getInstance().getReference();

        //Set up RecyclerView
        recyclerViewAdapter = new MessageRecyclerViewAdapter(messagesList, this); //Link the recyclerViewAdapter to the list of messages
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //Set the layout of the RecyclerView
        messagesRecyclerView.setAdapter(recyclerViewAdapter); //Link the adapter to the RecyclerView

        loadMessages(); //Load all the messages in the message thread

        messagesRecyclerView.scrollToPosition(messagesList.size()); //Scroll to the bottom of the RecyclerView

        //Add listener to refresh the list
        messagesSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage++;
                loadMessages(); //Load all the messages in the message thread
            }
        });

        //Listener for the "Send" Button
        sendIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If there is a connection with the
                if (Util.connectionAvailable(getApplicationContext()))
                {
                    String message = messageEditText.getText().toString(); //Obtain the text typed into the messageEditText

                    DatabaseReference userMessagePush = mRootReference.child(Util.MESSAGES).child(loggedInUsername).child(driverUsername).push(); //Push the message to the Firebase Realtime Database
                    String messageID = userMessagePush.getKey(); //Obtain the messageID of the sent message
                    sendMessage(message, messageID); //Send the message
                }
                else
                {
                    Util.makeToast(getApplicationContext(), "No internet");
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
            case R.id.action_home: //HomeActivity
                Intent mainActivityIntent = new Intent(MessageActivity.this, HomeActivity.class);
                startActivity(mainActivityIntent);
                return true;
            case R.id.action_account: //AccountActivity
                Intent accountActivityIntent = new Intent(MessageActivity.this, AccountActivity.class);
                startActivity(accountActivityIntent);
                return true;
            case R.id.action_orders: //MyOrdersActivity
                Intent myOrdersActivity = new Intent(MessageActivity.this, MyOrdersActivity.class);
                startActivity(myOrdersActivity);
                return true;
            case R.id.action_logout: //MainActivity
                Intent mainActivity = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Load all the messages in the message thread
    private void loadMessages()
    {
        messagesList.clear(); //Clear the list of messages
        databaseReference = mRootReference.child(Util.MESSAGES).child(loggedInUsername).child(receiverUsername); //Obtain the database reference

        //Obtain the reference to the "messages" node of the Firebase Realtime Database
        Query messageQuery = databaseReference.limitToLast(currentPage * RECORD_PER_PAGE);

        //If the listener for the reference to the "messages" node of the Firebase Realtime Database
        if (childEventListener != null)
        {
            messageQuery.removeEventListener(childEventListener); //Remove the listener
        }

        //Define the listener for the reference to the "messages" node of the Firebase Realtime Database
        childEventListener = new ChildEventListener() {

            //If a new child is added to the "message" node
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                messagesList.add(message); //Add the message
                recyclerViewAdapter.notifyDataSetChanged(); //Update the RecyclerView
                messagesRecyclerView.scrollToPosition(messagesList.size()-1); //Scroll to the bottom of the RecyclerView
                messagesSwipeRefresh.setRefreshing(false); //Do not invoke the refreshing of the message thread
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messagesSwipeRefresh.setRefreshing(false);
            }
        };

        messageQuery.addChildEventListener(childEventListener); //Link the listener of the node child (defined above) to the "messages" node child
    }

    //Send the message
    private void sendMessage(String message, String messageID)
    {
        try {

            //If the message does exists (i.e characters were typed into the enterMessageEditText immediately before selection of the sendIconImageView)
            if (!message.equals(""))
            {
                //Declare HashMaps for the Message and Chats to store values
                HashMap messageMap = new HashMap();     //HashMap for the Message
                HashMap chatsMap = new HashMap();       //HashMap for the Chat of the Message

                //Add values to the Message HashMap
                messageMap.put(Util.MESSAGE, message); //Add message text
                messageMap.put(Util.MESSAGE_ID, messageID); //Add messageID
                messageMap.put(Util.MESSAGE_SENDER, loggedInUsername); //Add username of the sender
                messageMap.put(Util.MESSAGE_TIME, ServerValue.TIMESTAMP); //Add time the message was sent

                //Add values to the Chats HashMap
                chatsMap.put(Util.MESSAGE, message); //Add the message
                chatsMap.put(Util.MESSAGE_SENDER, loggedInUsername); //Add the username of the sender
                chatsMap.put(Util.MESSAGE_TIME, ServerValue.TIMESTAMP); //Add the time the message was sent

                //Define unique identifiers of the senders
                String currentUserReference = Util.MESSAGES + "/" + loggedInUsername + "/" + receiverUsername; //Username chatting with the Truck Driver
                String truckDriverReference = Util.MESSAGES + "/" + receiverUsername + "/" + loggedInUsername; //Username of the Truck Driver

                //Get the current user of the Chat
                String chatCurrentUserRef = Util.CHATS + "/" + loggedInUsername;

                //Declare HashMap for the user
                HashMap messageUserMap = new HashMap();
                messageUserMap.put(currentUserReference + "/" + messageID, messageMap);
                messageUserMap.put(truckDriverReference + "/" + messageID, messageMap); //Add the HashMap of the Truck Driver

                messageEditText.setText(""); //Reset the text of the messageEditText to a blank one

                //If the logged in username is not that of the driver's
                if (!loggedInUsername.equals(driverUsername))
                {
                    HashMap chatMap = new HashMap(); //Declare HashMap for the chat

                    chatMap.put(chatCurrentUserRef, chatsMap); //Add the HashMap of the chat

                    //Check if there are any errors related to updating the Chat associated with the message thread
                    mRootReference.updateChildren(chatMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null)
                            {
                                Util.makeToast(getApplicationContext(), "Failed to send message " + error.getMessage());
                            }
                        }
                    });
                }

                //Check if there are any errors related to updating the message thread
                mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null)
                        {
                            Util.makeToast(getApplicationContext(), "Failed to send message " + error.getMessage());
                        }
                    }
                });
            }
        }
        catch (Exception ex){
            Util.makeToast(getApplicationContext(), "Failed to send message " + ex.getMessage());
        }
    }
}