package com.example.tuitionrecords.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAA6yBrNQg:APA91bF-A-B9oFZAqFEjwtx9qECe1MUHbQ_pf2caOdpwrSXGyBMEokWVrtXnD4g8XDDvUsr9YRPDHLhB9dzM9M_zAg_1hApoJCDhu4-ndqxPl6yVHkDgTUlDYK4xwX3B3uZ90ZymZ8I4"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
