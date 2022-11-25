package Servidor;//SD Protocol

import java.sql.Timestamp;

enum MessageType {
    SCOOTER_REQUEST,
    START_TRIP,
    END_TRIP,
    REGISTER,
    LOGIN
}


public class Message {
    private MessageType type;
    private String messageID;

    private String userID;
    private String scooterID;
    private Timestamp timestamp;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getScooterID() {
        return scooterID;
    }

    public void setScooterID(String scooterID) {
        this.scooterID = scooterID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Message(MessageType type, String messageID, String userID, String scooterID, Timestamp timestamp) {
        this.type = type;
        this.messageID = messageID;
        this.userID = userID;
        this.scooterID = scooterID;
        this.timestamp = timestamp;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        return "Servidor.SDP{" +
                "type=" + type +
                ", messageID='" + messageID + '\'' +
                ", userID='" + userID + '\'' +
                ", scooterID='" + scooterID + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}