package com.example.socialnapp.Remote;


import com.example.socialnapp.Model.Myresponce;
import com.example.socialnapp.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by HASIB on 12/16/2017.
 */

public interface APIservice {
    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA9FOx5WA:APA91bH_AY4hwc4jhdqoy9RCLeNqSgGdP32ne71bL7uDq4pmvUKiTyixgT0FvrZqi6bAbFA8_5sfxb8nLLdYTzB7O-iciAa8t6G2k1c5xnX-eQZhSpAVS5PHeyo_4f5B0z4w5Xw5XbDl"
            }
    )
    @POST("fcm/send")
    Call<Myresponce> sendNotification(@Body Sender body);



    //Call<Myresponce>

}