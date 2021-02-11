package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.example.tuitionrecords.TeacherActivity.ClassesAndBatches.MyClasses;

public class DayTimeTable extends AppCompatActivity {

    RelativeLayout monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    String user;

    RelativeLayout checkInternet;

    ImageView close;

    //commit check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_time_table);

        checkInternet = findViewById(R.id.check_internet);
        close = findViewById(R.id.close);
        checkInternet();


        user = getIntent().getStringExtra("user");

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);

        monday.setOnClickListener(view -> {
            Intent intent = new Intent(DayTimeTable.this, MyClasses.class);
            intent.putExtra("day", "Monday");
            startActivity(intent);
        });

        tuesday.setOnClickListener(view -> {
            Intent intent = new Intent(DayTimeTable.this, MyClasses.class);
            intent.putExtra("day", "Tuesday");
            startActivity(intent);
        });

        wednesday.setOnClickListener(view -> {
            Intent intent = new Intent(DayTimeTable.this, MyClasses.class);
            intent.putExtra("day", "Wednesday");
            startActivity(intent);
        });

        thursday.setOnClickListener(view -> {
            Intent intent = new Intent(DayTimeTable.this, MyClasses.class);
            intent.putExtra("day", "Thursday");
            startActivity(intent);
        });

        friday.setOnClickListener(view -> {
            Intent intent = new Intent(DayTimeTable.this, MyClasses.class);
            intent.putExtra("day", "Friday");
            startActivity(intent);
        });

        saturday.setOnClickListener(view -> {
            Intent intent = new Intent(DayTimeTable.this, MyClasses.class);
            intent.putExtra("day", "Saturday");
            startActivity(intent);
        });

        sunday.setOnClickListener(view -> {
            Intent intent = new Intent(DayTimeTable.this, MyClasses.class);
            intent.putExtra("day", "Sunday");
            startActivity(intent);
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
    public void onBackPressed() {
        if (user.equals("teacher")) {
            startActivity(new Intent(DayTimeTable.this, ShowTeacherActivity.class));
        }
        else {
            startActivity(new Intent(DayTimeTable.this, ShowStudentActivity.class));
        }
        finish();
    }
}