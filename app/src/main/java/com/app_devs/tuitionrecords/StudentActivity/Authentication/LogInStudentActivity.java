
package com.app_devs.tuitionrecords.StudentActivity.Authentication;

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

import com.app_devs.tuitionrecords.MainActivity;
import com.app_devs.tuitionrecords.R;
import com.app_devs.tuitionrecords.ResetActivity;
import com.app_devs.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.app_devs.tuitionrecords.TeacherActivity.Authentication.LogInTeacherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class LogInStudentActivity extends AppCompatActivity {

    TextView signUp, reset;
    Button login;
    EditText myEmail, myPassword;
    FirebaseAuth myAuth;
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

        sharedPreferences = getSharedPreferences("auto_login_student", Context.MODE_PRIVATE);

        /*int pref = sharedPreferences.getInt("key_student", 0);

        if (pref > 0) {
            startActivity(new Intent(getApplicationContext(), ShowStudentActivity.class));
        }

        if (myAuth.getCurrentUser() != null) {
            Log.i("CHECK_USER", myAuth.getCurrentUser().toString());
            startActivity(new Intent(LogInStudentActivity.this, ShowStudentActivity.class));
            finish();
        }*/

        signUp.setOnClickListener(v -> startActivity(new Intent(LogInStudentActivity.this, SignUpStudentActivity.class)));

        login.setOnClickListener(v -> {
            hideKeybaord(v);
            logInWithFirebase();
        });

        reset.setOnClickListener(view -> {
            Intent intent = new Intent(LogInStudentActivity.this, ResetActivity.class);
            intent.putExtra("email", Objects.requireNonNull(myEmail.getText()).toString());
            intent.putExtra("user", "student");
            startActivity(intent);
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

        if(!isValidEmail(email))
        {
            myEmail.setError("Wrong email");
            myEmail.requestFocus();
            return;
        }
        if (password.equals("")) {
            myPassword.setError("");
            myPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        progressBar.requestFocus();

        Toast.makeText(getApplicationContext(), "Logging you in...", Toast.LENGTH_SHORT).show();

        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            Log.i("LOGIN", "Was user logged in : " + task.isSuccessful());
            if (!task.isSuccessful()) {
                showErrorBox();
                progressBar.setVisibility(View.GONE);
                Log.i("FINDCODE", "Message : " + task.getException());
            }
            else {
                //AUTO_SAVE = 1;

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students_Profile").child(myAuth.getCurrentUser().getUid());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("key_student", 1);
                            editor.apply();

                            Toast.makeText(getApplicationContext(),"Logged in",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInStudentActivity.this, ShowStudentActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("key_student", 0);
                            editor.apply();
                            myAuth.signOut();
                            Toast.makeText(LogInStudentActivity.this, "User does not exists", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
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
    private void showErrorBox() {
        new AlertDialog.Builder(this)
                .setTitle("Ooooops!!")
                .setMessage("You entered the wrong email ID or password")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LogInStudentActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}