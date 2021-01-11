package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tuitionrecords.TeacherActivity.LogInTeacherActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetActivity extends AppCompatActivity {

    TextInputEditText email;
    AppCompatButton reset;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        email = findViewById(R.id.email_reset);

        reset.setOnClickListener(view -> {
            String sendEmail = Objects.requireNonNull(email.getText()).toString();

            if (sendEmail.equals("")) {
                Toast.makeText(this, "Field is Empty", Toast.LENGTH_SHORT).show();
            }
            else {
                firebaseAuth.sendPasswordResetEmail(sendEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetActivity.this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetActivity.this, LogInTeacherActivity.class));
                    }
                    else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}