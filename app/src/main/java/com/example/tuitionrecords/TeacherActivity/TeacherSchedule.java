package com.example.tuitionrecords.TeacherActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.R;

public class TeacherSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TeacherSchedule.this,ShowTeacherActivity.class));
    }

}