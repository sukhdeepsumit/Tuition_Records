package com.example.tuitionrecords.TeacherActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.example.tuitionrecords.R;

import java.util.Objects;

public class ShowTeacherActivity extends AppCompatActivity {
    ImageView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_teacher);

        message = findViewById(R.id.message_requests);
    }
}