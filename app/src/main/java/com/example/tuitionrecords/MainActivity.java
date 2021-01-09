package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        teacher = findViewById(R.id.teacher_layout);
        student = findViewById(R.id.student_layout);

        teacher.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LogInTeacherActivity.class)));

        student.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LogInStudentActivity.class)));
    }
}