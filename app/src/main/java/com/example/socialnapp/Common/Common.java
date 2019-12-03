package com.example.socialnapp.Common;

import com.example.socialnapp.Remote.APIservice;
import com.example.socialnapp.Remote.FCMretrofitClient;

public class Common {


    public static final String BaseUrl="https://fcm.googleapis.com/";




    public static APIservice getFCMClient(){
        return FCMretrofitClient.getClint(BaseUrl).create(APIservice.class);
    }

}
