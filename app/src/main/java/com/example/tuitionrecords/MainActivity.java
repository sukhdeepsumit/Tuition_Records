package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.tuitionrecords.StudentActivity.Authentication.LogInStudentActivity;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.TeacherActivity.Authentication.LogInTeacherActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;


// Commit check check check check mkc
public class MainActivity extends AppCompatActivity {

    RelativeLayout teacher, student, checkInternet;
    ImageView close;

    SharedPreferences sharedPreferences_teacher, sharedPreferences_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences_teacher = getSharedPreferences("auto_login_teacher", Context.MODE_PRIVATE);
        int pref_teacher = sharedPreferences_teacher.getInt("key_teacher", 0);

        sharedPreferences_student = getSharedPreferences("auto_login_student", Context.MODE_PRIVATE);
        int pref_student = sharedPreferences_student.getInt("key_student", 0);

        checkInternet();

        if (pref_teacher > 0) {
            startActivity(new Intent(getApplicationContext(), ShowTeacherActivity.class));
            finish();
        }
        else if (pref_student > 0) {
            startActivity(new Intent(getApplicationContext(), ShowStudentActivity.class));
            finish();
        }

        teacher = findViewById(R.id.teacher_layout);
        student = findViewById(R.id.student_layout);
        checkInternet = findViewById(R.id.check_internet);

        close = findViewById(R.id.close);

        teacher.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LogInTeacherActivity.class));
            SharedPreferences.Editor editor = getSharedPreferences("CHECK_USER", MODE_PRIVATE).edit();
            editor.putString("who", "teacher");
            editor.apply();
            finish();
        });

        student.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LogInStudentActivity.class));
            SharedPreferences.Editor editor = getSharedPreferences("CHECK_USER", MODE_PRIVATE).edit();
            editor.putString("who", "student");
            editor.apply();
            finish();
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}