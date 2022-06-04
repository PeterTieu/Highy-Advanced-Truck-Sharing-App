package com.tieutech.highlyadvancedtrucksharingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tieutech.highlyadvancedtrucksharingapp.adapter.ChatRecyclerViewAdapter;
import com.tieutech.highlyadvancedtrucksharingapp.model.Chat;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;
import java.util.ArrayList;
import java.util.List;

//ABOUT: Class that displays a list of all Chats between all users the TruckDriver
public class TruckerDriverChatsActivity extends AppCompatActivity {

    //View variables
    RecyclerView chatsListRecyclerView;
    TextView emptyChatsTextView;
    ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    List<Chat> chatsList;

    //Database reference variables
    private DatabaseReference databaseReferenceChats;
    private DatabaseReference databaseReferenceUsers;
    private ChildEventListener childEventListener;
    private Query messageTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_chats);

        //Obtain views
        chatsListRecyclerView = findViewById(R.id.chatsRecyclerView);
        emptyChatsTextView = findViewById(R.id.emptyChatsTextView);

        //Obtain list of all Chats
        chatsList = new ArrayList<>();
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(this, chatsList);

        //Linear Layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        chatsListRecyclerView.setLayoutManager(linearLayoutManager);
        chatsListRecyclerView.setAdapter(chatRecyclerViewAdapter);

        //Obtain Database references to the nodes of the Firebase Realtime Database - "user", "chats"
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child(Util.USERS);
        databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child(Util.CHATS);

        //Obtain the "message_time" child from the "chats" node
        messageTime = databaseReferenceChats.orderByChild(Util.MESSAGE_TIME);

        //Create listener for the "message_time" child
        childEventListener = new ChildEventListener() {

            //Listener for when the "message_time" is added
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateList(snapshot, true, snapshot.getKey()); //Update the Chat list
            }

            //Listener for when the "message_time" is changed
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateList(snapshot, true, snapshot.getKey()); //Update the Chat list
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        messageTime.addChildEventListener(childEventListener); //Link the listener of the node child (defined above) to the "message_time" node child

        //Display text to notify the User that there is no message
        emptyChatsTextView.setVisibility(View.VISIBLE);
    }

    //Create menu on tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Listener for selected options from the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            //Logout option to go back to the MainActivity
            case R.id.logoutMenu:
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Update the Chat list
    private void updateList(DataSnapshot dataSnapshot, boolean isNew, String username)
    {
        emptyChatsTextView.setVisibility(View.GONE); //Remove the text to notify the user that there is no Chat

        //Declare variables
        final String lastMessage;
        final long lastMessageTime;

        //If the last message in the Firebase Realtime Database exists
        if(dataSnapshot.child(Util.MESSAGE).getValue()!=null)
            lastMessage = dataSnapshot.child(Util.MESSAGE).getValue().toString(); //Get the value of the last message of the Chat
        //If the last message in the Firebase Realtime Database DOES NOT exist
        else {
            lastMessage = "";
        }

        //If the the message time in the Firebase Realtime Database exists
        if(dataSnapshot.child(Util.MESSAGE_TIME).getValue()!=null)
            lastMessageTime = (long) dataSnapshot.child(Util.MESSAGE_TIME).getValue(); //Get the value of the last message time of the Chat
        //If the the message time in the Firebase Realtime Database DOES NOT exist
        else {
            lastMessageTime = 0;
        }

        //Define change listeners to the last user of the CHat
        databaseReferenceUsers.child(username).addListenerForSingleValueEvent(new ValueEventListener() {

            //Listener for any changes made to the user of the Chat (i.e. the User who sent the last message)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = dataSnapshot.child(Util.MESSAGE_SENDER).getValue() != null?
                        dataSnapshot.child(Util.MESSAGE_SENDER).getValue().toString(): "";
                Chat chat = new Chat(username, lastMessage, lastMessageTime);

                chatsList.add(chat);
                chatRecyclerViewAdapter.notifyDataSetChanged();
            }

            //If the Chat is not able to be loaded
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Util.makeToast(getApplicationContext(), "Failed to fetch chats");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageTime.removeEventListener(childEventListener); //Remove the listener of the node child (defined above) to the "message_time" node child
    }
}