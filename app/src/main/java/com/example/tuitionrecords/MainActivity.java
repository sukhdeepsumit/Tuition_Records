package com.example.tuitionrecords;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.tuitionrecords.StudentActivity.LogInStudentActivity;
import com.example.tuitionrecords.TeacherActivity.LogInTeacherActivity;
import com.example.tuitionrecords.TeacherActivity.SignUpTeacherActivity;

// Commit check check check check mkc
public class MainActivity extends AppCompatActivity {

    RelativeLayout teacher, student, checkInternet;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkInternet();

        teacher = findViewById(R.id.teacher_layout);
        student = findViewById(R.id.student_layout);
        checkInternet = findViewById(R.id.check_internet);

        close = findViewById(R.id.close);

        teacher.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LogInTeacherActivity.class)));

        student.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LogInStudentActivity.class)));
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
                    showInternetWarning();

                }
                handler.postDelayed(this,3000);
            }
        });
    }
    public void showInternetWarning() {
        checkInternet.setVisibility(View.VISIBLE);
        close.setOnClickListener(view -> checkInternet.setVisibility(View.GONE));
    }

}