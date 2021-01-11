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
import android.widget.RelativeLayout;

import com.example.tuitionrecords.StudentActivity.LogInStudentActivity;
import com.example.tuitionrecords.TeacherActivity.LogInTeacherActivity;

// Commit check check check check mkc
public class MainActivity extends AppCompatActivity {

    RelativeLayout teacher, student;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkInternet();

        teacher = findViewById(R.id.teacher_layout);
        student = findViewById(R.id.student_layout);

        teacher.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LogInTeacherActivity.class)));

        student.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LogInStudentActivity.class)));
    }


    private void checkInternet()
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
                    showAlertDialog();

                }
                handler.postDelayed(this,3000);
            }
        });
    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("No Network Connection").setCancelable(false)
                .setIcon(R.drawable.error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}