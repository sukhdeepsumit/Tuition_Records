package com.example.tuitionrecords.TeacherActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tuitionrecords.R;

public class LogInTeacherActivity extends AppCompatActivity {

    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_teacher);

        signUp = findViewById(R.id.signup);

        signUp.setOnClickListener(view -> startActivity(new Intent(LogInTeacherActivity.this, SignUpActivity.class)));
    }
}