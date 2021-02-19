package com.app_devs.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeScreen extends AppCompatActivity {
    //comitt check
    private static final int SPLASH_TIME_OUT=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(() -> {
            Intent homeIntent=new Intent(WelcomeScreen.this,MainActivity.class);
            startActivity(homeIntent);
            finish();

        },SPLASH_TIME_OUT);
    }
}