package com.example.socialnapp.Model;

public class Message {

    String date, from, message, time, type, to, message_PushID;

    public Message(){

    }

    public Message(String date, String from, String message, String time, String type, String to, String message_PushID) {
        this.date = date;
        this.from = from;
        this.message = message;
        this.time = time;
        this.type = type;
        this.to = to;
        this.message_PushID = message_PushID;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage_PushID() {
        return message_PushID;
    }

    public void setMessage_PushID(String message_PushID) {
        this.message_PushID = message_PushID;
    }
}
