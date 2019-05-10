package com.example.chatapplication.Model;

public class Chat {
    private String sender;
    private String Receiver;
    private String message;

    public Chat(){

    }

    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        Receiver = receiver;
        this.message = message;
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
