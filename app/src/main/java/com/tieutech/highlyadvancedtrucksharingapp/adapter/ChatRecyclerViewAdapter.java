package com.tieutech.highlyadvancedtrucksharingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tieutech.highlyadvancedtrucksharingapp.MessageActivity;
import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.model.Chat;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//ABOUT:
//RecyclerView Adapter for the Chats (to be displayed in the TruckDriverChatsActivity)
//FUNCTION:
//Links the data of each item to be displayed in the RecyclerView to the RecyclerView itself
public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatListViewHolder> {

    //======= DECLARE VARIABLES =======
    private Context context;
    private List<Chat> chats;

    //Constructor for the RecyclerView Adapter
    public ChatRecyclerViewAdapter(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    //======= DEFINE METHODS =======
    //Upon the creation of the ViewHolder of each item in the RecyclerView
    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout, parent, false);
        return new ChatListViewHolder(view);
    }

    //Modify the display of the view elements in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.ChatListViewHolder holder, int position) {

        //Obtain the Chat
        Chat chat = chats.get(position);

        //Obtain the date and time of the last message of the Chat
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateTime = sfd.format(new Date(chat.getLastMessageTime()));
        String[] splitString = dateTime.split(" ");
        String messageTime = splitString[1];

        //Obtain the username of User who sent the last message
        String messageSender = chat.getUsername();

        //Set Views
        holder.chatListTime.setText(messageTime);
        holder.chatListMessage.setText(chat.getLastMessage());
        holder.chatListName.setText(messageSender);

        //Click listener for the entire list item
        holder.chatListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the MessageActivity
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra(Util.MESSAGE_SENDER, messageSender); //Send data of the sender of the message to the MessageActivity
                context.startActivity(intent);
            }
        });
    }

    //Return the size of the dataset
    @Override
    public int getItemCount() {
        return chats.size();
    }

    //ViewHolder for each item in the RecyclerView
    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        //View variables
        LinearLayout chatListLayout;
        TextView chatListName;
        TextView chatListMessage;
        TextView chatListTime;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            chatListLayout = itemView.findViewById(R.id.chatListLayout);
            chatListName = itemView.findViewById(R.id.chatListName);
            chatListMessage = itemView.findViewById(R.id.chatListMessage);
            chatListTime = itemView.findViewById(R.id.chatListTime);
        }
    }
}
