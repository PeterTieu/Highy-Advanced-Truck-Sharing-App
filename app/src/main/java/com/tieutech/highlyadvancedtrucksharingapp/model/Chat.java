package com.tieutech.highlyadvancedtrucksharingapp.model;

//ABOUT: Class for Chat object. It represents an entire messaging thread between a User of any username and the User with "truck driver" username
//USAGE: TruckDriverChatsActivity
public class Chat {

    //Variables
    private String username; //Username of the user in which a messaging thread exists with the "truck driver"
    private String lastMessage; //String of the last message in the messaging thread
    private long lastMessageTime; //Time of the last message sent

    //Constructure #1
    public Chat(String username, String lastMessage, long lastMessageTime) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    //Getters
    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getLastMessage() {
        return lastMessage;
    }

    //Setters
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public long getLastMessageTime() {
        return lastMessageTime;
    }
    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
