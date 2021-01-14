package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionrecords.MainActivity;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.ResetActivity;
import com.example.tuitionrecords.TeacherActivity.LogInTeacherActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInStudentActivity extends AppCompatActivity {

    TextView signUp, reset;
    Button login;
    EditText myEmail, myPassword;
    private FirebaseAuth myAuth;
    ProgressBar progressBar;

    public static SharedPreferences sharedPreferences;
    //int AUTO_SAVE;

    RelativeLayout checkInternet;
    ImageView close;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_student);

        checkInternet = findViewById(R.id.check_internet);
        close = findViewById(R.id.close);

        layout = findViewById(R.id.main_layout);

        checkInternet();

        signUp=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        myEmail=findViewById(R.id.email_text);
        myPassword=findViewById(R.id.password_text);
        progressBar=findViewById(R.id.progressBar);
        reset = findViewById(R.id.reset);

        myAuth=FirebaseAuth.getInstance();

        sharedPreferences = getApplicationContext().getSharedPreferences("auto_login_student", Context.MODE_PRIVATE);
        /*int pref = sharedPreferences.getInt("key_student", 0);


        if (pref > 0) {
            startActivity(new Intent(getApplicationContext(), ShowStudentActivity.class));
        }*/

        /*if (myAuth.getCurrentUser() != null) {
            Log.i("CHECK_USER", myAuth.getCurrentUser().toString());
            startActivity(new Intent(LogInStudentActivity.this, ShowStudentActivity.class));
            finish();
        }*/

        signUp.setOnClickListener(v -> startActivity(new Intent(LogInStudentActivity.this,SignUpStudentActivity.class)));

        login.setOnClickListener(v -> {
            hideKeybaord(v);
            logInWithFirebase();
        });

        reset.setOnClickListener(view -> {
            startActivity(new Intent(LogInStudentActivity.this, ResetActivity.class));
            finish();
        });
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
    private void logInWithFirebase()
    {
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        if(email.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "Logging you in...", Toast.LENGTH_SHORT).show();

        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("LOGIN", "Was user logged in : " + task.isSuccessful());
                if (!task.isSuccessful()) {
                    showErrorBox();
                    progressBar.setVisibility(View.GONE);
                    Log.i("FINDCODE", "Message : " + task.getException());
                }
                else {
                    //AUTO_SAVE = 1;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("key_student", 1);
                    editor.apply();

                    Toast.makeText(getApplicationContext(),"Logged in",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInStudentActivity.this,ShowStudentActivity.class));
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
    private void showErrorBox() {
        new AlertDialog.Builder(this)
                .setTitle("Ooooops!!")
                .setMessage("There was a problem logging in")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LogInStudentActivity.this, MainActivity.class);
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