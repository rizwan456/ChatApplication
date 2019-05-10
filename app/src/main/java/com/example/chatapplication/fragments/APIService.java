package com.example.chatapplication.fragments;

import com.example.chatapplication.notification.MyResponse;
import com.example.chatapplication.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "content-Type:application/json",
                    "Authorization:key=AAAA09A8O-c:APA91bF18GE7_deCQejg-RKry-jaEt8qz9Q5iCURcApdLTl3W36-E8qb8-QlZocetUbLnnnHiDyE6NpYSa1RoOFYvD7Y4prq8l-ZAl0MpYUh6wJ_ebA6-p5_jzPFvQ--NThhq9r7un0v"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
