package com.example.tuitionrecords.TeacherActivity.Authentication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionrecords.MainActivity;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.ResetActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

public class LogInTeacherActivity extends AppCompatActivity {

    TextView signUp, reset;
    TextInputEditText email, password;
    AppCompatButton login;
    ProgressBar progressBar;

    FirebaseAuth myAuth;

    public static SharedPreferences sharedPreferences;
    int AUTO_SAVE;

    RelativeLayout checkInternet;
    ImageView close;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_teacher);

        checkInternet = findViewById(R.id.check_internet);
        close = findViewById(R.id.close);

        layout = findViewById(R.id.main_layout);

        checkInternet();

        myAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.signup);
        reset = findViewById(R.id.reset);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.pg);

        progressBar.setVisibility(View.GONE);

        email = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);

        sharedPreferences = getApplicationContext().getSharedPreferences("auto_login_teacher", Context.MODE_PRIVATE);

        /*int pref = sharedPreferences.getInt("key_teacher", 0);

        if (pref > 0) {
            startActivity(new Intent(getApplicationContext(), ShowTeacherActivity.class));
        }

       if (myAuth.getCurrentUser() != null) {
            startActivity(new Intent(LogInTeacherActivity.this, ShowTeacherActivity.class));
            finish();
        }*/

        signUp.setOnClickListener(view -> startActivity(new Intent(LogInTeacherActivity.this, SignUpTeacherActivity.class)));

        login.setOnClickListener(view -> {
            hideKeyboard(view);
            loginWithFirebase();
        });

        reset.setOnClickListener(view -> {
            Intent intent = new Intent(LogInTeacherActivity.this, ResetActivity.class);
            intent.putExtra("email", Objects.requireNonNull(email.getText()).toString());
            intent.putExtra("user", "teacher");
            startActivity(intent);
            finish();
        });
    }

    private void loginWithFirebase() {

        String myEmail = Objects.requireNonNull(email.getText()).toString();
        String myPassword = Objects.requireNonNull(password.getText()).toString();
//        if(myEmail.equals("") || myPassword.equals("")) {
//            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(!isValidEmail(myEmail))
        {
            email.setError("Wrong email");
            email.requestFocus();
            return;
        }


        if (myPassword.equals("")) {
            password.setError("");
            password.requestFocus();
            return;
        }



        Toast.makeText(getApplicationContext(),  "Logging you in...", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);

        myAuth.signInWithEmailAndPassword(myEmail, myPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        //AUTO_SAVE = 1;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("key_teacher", 1);
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
    private boolean isValidEmail(String target)
    {
        return EMAIL_ADDRESS_PATTERN.matcher(target).matches();
    }
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

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
        Intent intent = new Intent(LogInTeacherActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
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
                    layout.setVisibility(View.GONE);
                    showInternetWarning();
                }
                else {
                    layout.setVisibility(View.VISIBLE);
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
    protected void onPause() {
        super.onPause();
        finish();
    }
}