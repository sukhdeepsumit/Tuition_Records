package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.tuitionrecords.StudentActivity.Authentication.LogInStudentActivity;
import com.example.tuitionrecords.TeacherActivity.Authentication.LogInTeacherActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetActivity extends AppCompatActivity {

    TextInputEditText email;
    AppCompatButton reset;

    FirebaseAuth firebaseAuth;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        email = findViewById(R.id.email_reset);
        reset = findViewById(R.id.reset);

        firebaseAuth = FirebaseAuth.getInstance();

        String intentEmail = getIntent().getStringExtra("email");
        user = getIntent().getStringExtra("user");
        email.setText(intentEmail);

        reset.setOnClickListener(view -> {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

            String sendEmail = Objects.requireNonNull(email.getText()).toString();

            if (sendEmail.equals("")) {
                Toast.makeText(this, "Field is Empty", Toast.LENGTH_SHORT).show();
            }
            else {
                firebaseAuth.sendPasswordResetEmail(sendEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetActivity.this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
                        if (user.equals("teacher")) {
                            startActivity(new Intent(ResetActivity.this, LogInTeacherActivity.class));
                        }
                        else {
                            startActivity(new Intent(ResetActivity.this, LogInStudentActivity.class));
                        }
                        finish();
                    }
                    else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (user.equals("teacher")) {
            startActivity(new Intent(getApplicationContext(), LogInTeacherActivity.class));
        }
        else {
            startActivity(new Intent(getApplicationContext(), LogInStudentActivity.class));
        }
        finish();
    }
}