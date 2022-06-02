package com.tieutech.highlyadvancedtrucksharingapp.model;

//ABOUT: Class for each Order object
public class Order {

    //Variables
    int orderId;
    byte[] senderImage; //Sender display picture
    String senderUsername; //Sender username
    String receiverUsername; //Receiver username
    String orderPickupDate; //Pickup date
    String orderPickupTime; //Pickup time
    String orderPickupLocation; //Pickup location
    double orderPickupLatitude; //Pickup location latitude
    double orderPickupLongitude; //Pickup location longitude
    String orderDestination; //Destination
    double orderDestinationLatitude; //Destination latitude
    double orderDestinationLongitude; //Destination longitude
    String goodType; //Good type
    String orderWeight; //Weight
    String orderWidth; //Width
    String orderLength; //Length
    String orderHeight; //Height
    String orderVehicleType; //Vehicle type
    String goodDescription; //Good description
    byte[] goodImage; //Image of the good
    String goodClassification; //Classification of the good from image recognition
    double goodClassificationConfidence; //Classification confidence of the good from image recognition

    //Constructor #1
    public Order() {
    }

    //Constructor #2
    public Order(byte[] senderImage, String senderUsername, String receiverUsername, String goodDescription) {
        this.senderImage = senderImage;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.goodDescription = goodDescription;
    }

    //Constructor #3
    public Order(byte[] senderImage, String senderUsername, String receiverUsername,
                 String goodDescription, String orderPickupDate, String orderPickupTime,
                 String orderPickupLocation, double orderPickupLatitude, double orderPickupLongitude,
                 String orderDestination, double orderDestinationLatitude, double orderDestinationLongitude,
                 String goodType, String orderWeight, String orderWidth, String orderLength, String orderHeight, String orderVehicleType) {

        this.senderImage = senderImage;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.goodDescription = goodDescription;
        this.orderPickupDate = orderPickupDate;
        this.orderPickupTime = orderPickupTime;
        this.orderPickupLocation = orderPickupLocation;
        this.orderPickupLatitude = orderPickupLatitude;
        this.orderPickupLongitude = orderPickupLongitude;
        this.orderDestination = orderDestination;
        this.orderDestinationLatitude = orderDestinationLatitude;
        this.orderDestinationLongitude = orderDestinationLongitude;
        this.goodType = goodType;
        this.orderWeight = orderWeight;
        this.orderWidth = orderWidth;
        this.orderLength = orderLength;
        this.orderHeight = orderHeight;
        this.orderVehicleType = orderVehicleType;
    }

    //Constructor #4
    public Order(byte[] senderImage, String senderUsername, String receiverUsername,
                 String goodDescription, String orderPickupDate, String orderPickupTime,
                 String orderPickupLocation, double orderPickupLatitude, double orderPickupLongitude,
                 String orderDestination, double orderDestinationLatitude, double orderDestinationLongitude,
                 String goodType, String orderWeight, String orderWidth, String orderLength, String orderHeight, String orderVehicleType,
                 byte[] goodImage, String goodClassification, double goodClassificationConfidence) {

        this.senderImage = senderImage;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.goodDescription = goodDescription;
        this.orderPickupDate = orderPickupDate;
        this.orderPickupTime = orderPickupTime;
        this.orderPickupLocation = orderPickupLocation;
        this.orderPickupLatitude = orderPickupLatitude;
        this.orderPickupLongitude = orderPickupLongitude;
        this.orderDestination = orderDestination;
        this.orderDestinationLatitude = orderDestinationLatitude;
        this.orderDestinationLongitude = orderDestinationLongitude;
        this.goodType = goodType;
        this.orderWeight = orderWeight;
        this.orderWidth = orderWidth;
        this.orderLength = orderLength;
        this.orderHeight = orderHeight;
        this.orderVehicleType = orderVehicleType;
        this.goodImage = goodImage;
        this.goodClassification = goodClassification;
        this.goodClassificationConfidence = goodClassificationConfidence;
    }

    //Getters
    public int getOrderId() { return orderId; }
    public byte[] getSenderImage() { return senderImage; }
    public String getSenderUsername() {
        return senderUsername;
    }
    public String getReceiverUsername() {
        return receiverUsername;
    }
    public String getOrderPickupDate() { return orderPickupDate; }
    public String getOrderPickupTime() { return orderPickupTime; }
    public String getOrderPickupLocation() { return orderPickupLocation; }
    public double getOrderPickupLatitude() { return orderPickupLatitude; }
    public double getOrderPickupLongitude() { return orderPickupLongitude; }
    public String getOrderDestination() { return orderDestination; }
    public double getOrderDestinationLatitude() { return orderDestinationLatitude; }
    public double getOrderDestinationLongitude() { return orderDestinationLongitude; }
    public String getGoodType() { return goodType; }
    public String getOrderWeight() { return orderWeight; }
    public String getOrderWidth() { return orderWidth; }
    public String getOrderLength() { return orderLength; }
    public String getOrderHeight() { return orderHeight; }
    public String getOrderVehicleType() { return orderVehicleType; }
    public String getGoodDescription() {
        return goodDescription;
    }
    public byte[] getGoodImage() { return goodImage; }
    public String getGoodClassification() { return goodClassification; }
    public double getGoodClassificationConfidence() { return goodClassificationConfidence; }

    //Setters
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setSenderImage(byte[] senderImage) { this.senderImage = senderImage; }
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }
    public void setOrderPickupDate(String orderPickupDate) { this.orderPickupDate = orderPickupDate; }
    public void setOrderPickupTime(String orderPickupTime) { this.orderPickupTime = orderPickupTime; }
    public void setOrderPickupLocation(String orderPickupLocation) { this.orderPickupLocation = orderPickupLocation; }
    public void setOrderPickupLatitude(double orderPickupLatitude) { this.orderPickupLatitude = orderPickupLatitude; }
    public void setOrderPickupLongitude(double orderPickupLongitude) { this.orderPickupLongitude = orderPickupLongitude; }
    public void setOrderDestination(String orderDestination) { this.orderDestination = orderDestination; }
    public void setOrderDestinationLatitude(double orderDestinationLatitude) { this.orderDestinationLatitude = orderDestinationLatitude; }
    public void setOrderDestinationLongitude(double orderDestinationLongitude) { this.orderDestinationLongitude = orderDestinationLongitude; }
    public void setGoodType(String goodType) { this.goodType = goodType; }
    public void setOrderWeight(String orderWeight) { this.orderWeight = orderWeight; }
    public void setOrderWidth(String orderWidth) { this.orderWidth = orderWidth; }
    public void setOrderLength(String orderLength) { this.orderLength = orderLength; }
    public void setOrderHeight(String orderHeight) { this.orderHeight = orderHeight; }
    public void setOrderVehicleType(String orderVehicleType) { this.orderVehicleType = orderVehicleType; }
    public void setGoodDescription(String goodDescription) { this.goodDescription = goodDescription; }
    public void setGoodImage(byte[] goodImage) { this.goodImage = goodImage; }
    public void setGoodClassification(String goodClassification) { this.goodClassification = goodClassification; }
    public void setGoodClassificationConfidence(double goodClassificationConfidence) { this.goodClassificationConfidence = goodClassificationConfidence; }
}