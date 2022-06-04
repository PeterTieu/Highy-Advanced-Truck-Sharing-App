package com.tieutech.highlyadvancedtrucksharingapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.model.Message;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//ABOUT:
//RecyclerView Adapter for the Messages (to be displayed in the MessagesActivityy)
//FUNCTION:
//Links the data of each item to be displayed in the RecyclerView to the RecyclerView itself
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder> {

    //======= DECLARE VARIABLES =======
    private ArrayList<Message> messages;
    private Context context;
    private FirebaseAuth firebaseAuth;

    //Constructor for the RecyclerView Adapter
    public MessageRecyclerViewAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;

    }

    //======= DEFINE METHODS =======
    //Upon the creation of the ViewHolder of each item in the RecyclerView
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false); //Create the view of the ViewHolder
        return new MessageViewHolder(view); //Link the ViewHolder to the RecyclerView Adapter
    }

    //Modify the display of the view elements in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        //Obtain the Message
        Message message = messages.get(position);

        //Obtain the FirebaseAuthentication instance
        firebaseAuth = FirebaseAuth.getInstance();

        //Obtain username of User of the user currently logged in (from Shared Preferences)
        SharedPreferences prefs = context.getSharedPreferences(Util.SHARED_PREF_DATA, context.MODE_PRIVATE);
        String loggedInUsername = prefs.getString(Util.SHARED_PREF_ACTIVE_USERNAME, "");

        //Obtain the sent/received message
        String messageSender = message.getMessage_sender();

        //Obtain the date and time of the last message of the Chat
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateTime = sfd.format(new Date(message.getMessage_time()));
        String[] splitString = dateTime.split(" ");
        String messageTime = splitString[1];

        //Decided how to display the message (left or right) of the screen
        //If the username of the logged in user is the same as that of the message sender (display on the right)
        if (loggedInUsername.equals(messageSender)) {
            holder.sentLayout.setVisibility(View.VISIBLE);      //Set the sent message layout as VISIBLE
            holder.receivedLayout.setVisibility(View.GONE);     //Set the received message layout as GONE
            holder.sentTextView.setText(message.getMessage());  //Set the message View
            holder.sentTimeTextView.setText(messageTime);       //Set the message time View
        }
        //If the username of the logged in user is the NOT the same as that of the message sender (display on the left)
        else
        {
            holder.receivedLayout.setVisibility(View.VISIBLE);      //Set the sent message layout as VISIBLE
            holder.sentLayout.setVisibility(View.GONE);             //Set the received message layout as GONE
            holder.receivedTextView.setText(message.getMessage());  //Set the message View
            holder.receivedTimeTextView.setText(messageTime);       //Set the message time View
        }
    }

    //Return the size of the dataset
    @Override
    public int getItemCount() {
        return messages.size();
    }

    //ViewHolder for each item in the RecyclerView
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        //View variables
        ConstraintLayout messageLayout;

            //Sent message (sent by the logged in user). Location: RIGHT side of the screen
        LinearLayout sentLayout;
        TextView sentTextView;
        TextView sentTimeTextView;

            //Received message (received by the logged in user). Location: LEFT side of the screen
        LinearLayout receivedLayout;
        TextView receivedTextView;
        TextView receivedTimeTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLayout = itemView.findViewById(R.id.messageLayout);

            //Set Views for SENT message. Location: RIGHT side of the screen
            sentLayout = itemView.findViewById(R.id.sentLayout);
            sentTextView = itemView.findViewById(R.id.sentTextView);
            sentTimeTextView = itemView.findViewById(R.id.sentTimeTextView);

            //Set Views for RECEIVED message. Location: LEFT side of the screen
            receivedLayout = itemView.findViewById(R.id.receivedLayout);
            receivedTextView = itemView.findViewById(R.id.receivedTextView);
            receivedTimeTextView = itemView.findViewById(R.id.receivedTimeTextView);
        }
    }
}