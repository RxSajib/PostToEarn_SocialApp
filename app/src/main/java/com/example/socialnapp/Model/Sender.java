package com.example.socialnapp.Model;

import java.util.HashMap;
import java.util.Map;

public class Sender {

    private   String to;
    private  Notification notification;


    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }
}