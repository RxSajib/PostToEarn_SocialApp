package com.example.socialnapp.Common;

import com.example.socialnapp.Model.LastTime;
import com.example.socialnapp.Remote.APIservice;
import com.example.socialnapp.Remote.FCMretrofitClient;

import java.util.List;

public class Common {


    public static final String BaseUrl="https://fcm.googleapis.com/";




    public static APIservice getFCMClient(){
        return FCMretrofitClient.getClint(BaseUrl).create(APIservice.class);
    }

   public static List<LastTime>  last_time;

}
