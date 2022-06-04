package com.tieutech.highlyadvancedtrucksharingapp.model;

//ABOUT: Class for Message object. It represents a single message sent or received to or from a User of any username from and to the User of the "truck driver" username, respectively.
// The vice versa scenario applies.
//USAGE: MessagesActivity
public class Message {

    //Variables
    private String message;         //The message sent or received
    private String message_ID;      //The unique ID of the message in Firebase Realtime Database
    private String message_sender;  //The username of the sender of the message
    private long message_time;      //The time the message was sent or received

    //Constructor #1
    public Message() { }

    //Constructor #2
    public Message(String message, String message_ID, String message_sender, long message_time) {
        this.message = message;
        this.message_ID = message_ID;
        this.message_sender = message_sender;
        this.message_time = message_time;
    }

    //Getters
    public String getMessage() {
        return message;
    }
    public String getMessage_ID() {
        return message_ID;
    }
    public String getMessage_sender() {
        return message_sender;
    }
    public long getMessage_time() {
        return message_time;
    }

    //Setters
    public void setMessage(String message) {
        this.message = message;
    }
    public void setMessage_ID(String message_ID) {
        this.message_ID = message_ID;
    }
    public void setMessage_sender(String message_sender) {
        this.message_sender = message_sender;
    }
    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }
}
