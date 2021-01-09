package com.example.tuitionrecords.TeacherActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LogInTeacherActivity extends AppCompatActivity {

    TextView signUp, reset;
    TextInputEditText email, password;
    AppCompatButton login;
    ProgressBar progressBar;

    FirebaseAuth myAuth;

    public static SharedPreferences sharedPreferences;
    int AUTO_SAVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_teacher);

        myAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.signup);
        reset = findViewById(R.id.reset);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.pg);

        progressBar.setVisibility(View.GONE);

        email = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int pref = sharedPreferences.getInt("key", 0);

        if (pref > 0) {
            startActivity(new Intent(LogInTeacherActivity.this, ShowTeacherActivity.class));
        }

        signUp.setOnClickListener(view -> startActivity(new Intent(LogInTeacherActivity.this, SignUpTeacherActivity.class)));

        login.setOnClickListener(view -> {
            hideKeyboard(view);
            loginWithFirebase();
        });
    }

    private void loginWithFirebase() {

        String myEmail = Objects.requireNonNull(email.getText()).toString();
        String myPassword = Objects.requireNonNull(password.getText()).toString();

        if (myEmail.equals("")) {
            email.setError("Empty");
        }
        if (myPassword.equals("")) {
            password.setError("");
        }

        Toast.makeText(getApplicationContext(),  "Logging you in...", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);

        myAuth.signInWithEmailAndPassword(myEmail, myPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AUTO_SAVE = 1;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("key", AUTO_SAVE);
                        editor.apply();

                        Intent intent = new Intent(LogInTeacherActivity.this, ShowTeacherActivity.class);
                        finish();
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        showErrorBox();
                        progressBar.setVisibility(View.GONE);
                        Log.i("LOGIN_ERROR", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void showErrorBox() {
        new AlertDialog.Builder(this)
                .setTitle("Ooooops!!")
                .setMessage("You entered the wrong email ID or password")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}