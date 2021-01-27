package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.example.tuitionrecords.TeacherActivity.TeacherBatches.MyClasses;

public class DayTimeTable extends AppCompatActivity {

    RelativeLayout monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    String user;

    //commit check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_time_table);

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