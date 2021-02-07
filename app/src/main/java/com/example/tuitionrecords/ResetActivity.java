package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tuitionrecords.StudentActivity.Authentication.LogInStudentActivity;
import com.example.tuitionrecords.TeacherActivity.Authentication.LogInTeacherActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetActivity extends AppCompatActivity {

    TextInputEditText email;
    AppCompatButton reset;

    FirebaseAuth firebaseAuth;
    String user;

    RelativeLayout checkInternet;
    LinearLayout layout;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        checkInternet = findViewById(R.id.check_internet);
        layout = findViewById(R.id.main_layout);
        close = findViewById(R.id.close);
        checkInternet();


        email = findViewById(R.id.email_reset);
        reset = findViewById(R.id.reset);

        firebaseAuth = FirebaseAuth.getInstance();

        String intentEmail = getIntent().getStringExtra("email");
        user = getIntent().getStringExtra("user");
        email.setText(intentEmail);

        reset.setOnClickListener(view -> {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

            String sendEmail = Objects.requireNonNull(email.getText()).toString();

            if (sendEmail.equals("")) {
                Toast.makeText(this, "Field is Empty", Toast.LENGTH_SHORT).show();
            }
            else {
                firebaseAuth.sendPasswordResetEmail(sendEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetActivity.this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
                        if (user.equals("teacher")) {
                            startActivity(new Intent(ResetActivity.this, LogInTeacherActivity.class));
                        }
                        else {
                            startActivity(new Intent(ResetActivity.this, LogInStudentActivity.class));
                        }
                        finish();
                    }
                    else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void checkInternet()
    {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm =
                        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected)
                {
                    layout.setVisibility(View.GONE);
                    showInternetWarning();
                }
                else {
                    layout.setVisibility(View.VISIBLE);
                }
                handler.postDelayed(this,3000);
            }
        });
    }
    public void showInternetWarning() {
        checkInternet.setVisibility(View.VISIBLE);
        close.setOnClickListener(view -> checkInternet.setVisibility(View.GONE));
    }
    @Override
    public void onBackPressed() {
        if (user.equals("teacher")) {
            startActivity(new Intent(getApplicationContext(), LogInTeacherActivity.class));
        }
        else {
            startActivity(new Intent(getApplicationContext(), LogInStudentActivity.class));
        }
        finish();
    }
}