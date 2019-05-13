package com.example.chatapplication.Model;

import java.sql.Timestamp;

public class Chat {
    private String sender;
    private String Receiver;
    private String message;
    private String time;

    public Chat(String sender, String receiver, String message, String time) {
        this.sender = sender;
        Receiver = receiver;
        this.message = message;
        this.time = time;
    }

    public Chat(){

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
