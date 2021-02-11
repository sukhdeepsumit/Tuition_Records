package com.example.tuitionrecords.StudentActivity.Request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.Notifications.APIService;
import com.example.tuitionrecords.Notifications.Client;
import com.example.tuitionrecords.Notifications.Data;
import com.example.tuitionrecords.Notifications.MyResponse;
import com.example.tuitionrecords.Notifications.Sender;
import com.example.tuitionrecords.Notifications.Token;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.MyTeacher.MyTeachers;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.example.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//commit check
public class CheckTeacherProfile extends AppCompatActivity {

    CircleImageView dp;
    TextView name, email, gender, location, subject, standard, about;
    AppCompatButton send;
    ProgressBar progressBar;

    DatabaseReference received, requestRef, accepted, requestNotify, sent;

    String CURRENT_STATE;
    String sender, receiver;
    String username;
    RelativeLayout checkInternet;
    LinearLayout layout;
    ImageView close;
    //APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_teacher_profile);
        checkInternet = findViewById(R.id.check_internet);
        layout = findViewById(R.id.main_layout);
        close = findViewById(R.id.close);
        checkInternet();

        dp = findViewById(R.id.dp_upload);

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        gender = findViewById(R.id.gender_get);
        location = findViewById(R.id.city_state_get);
        subject = findViewById(R.id.subject_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);

        send = findViewById(R.id.send_request);
        progressBar = findViewById(R.id.progress_request);
        progressBar.setVisibility(View.GONE);

        CURRENT_STATE = "Not Teacher";

        //apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiver = getIntent().getStringExtra("userId");

        sent = FirebaseDatabase.getInstance().getReference("Students_Profile").child(sender);
        received = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(receiver);
        requestRef = FirebaseDatabase.getInstance().getReference("Requests");
        accepted = FirebaseDatabase.getInstance().getReference("Accepted_Students");
        requestNotify = FirebaseDatabase.getInstance().getReference("Tokens");

        sent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentModel model = snapshot.getValue(StudentModel.class);
                username = model.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        received.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeacherModel teacherModel = snapshot.getValue(TeacherModel.class);

                name.setText(teacherModel.getName());
                email.setText(teacherModel.getEmail());
                gender.setText(teacherModel.getGender());
                location.setText(teacherModel.getCity() + ", " + teacherModel.getState());
                String sbj = teacherModel.getContent();
                sbj = sbj.substring(0,1).toUpperCase() + sbj.substring(1);
                subject.setText(sbj);
                standard.setText(teacherModel.getStandard());
                about.setText(teacherModel.getAbout());

                if (teacherModel.getMyUri().equals("default")) {
                    dp.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(getApplicationContext()).load(teacherModel.getMyUri()).into(dp);
                }

                changeButtonState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        //changeButtonState();

        send.setOnClickListener(view -> {
            if (CURRENT_STATE.equals("Not Teacher")) {
                sendTeacherRequest();
            }

            if (CURRENT_STATE.equals("Request Sent")) {
                cancelTeacherRequest();
            }
            send.setEnabled(true);
        });

        //updateToken();
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

    /*private void updateToken() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(sender).setValue(token);
    }*/

/*    public void sendNotifications(String receiver, String username, String message) {
        Query query = requestNotify.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Token token = snapshot.getValue(Token.class);
                Data data = new Data(sender, R.drawable.chat, username + ": " + message, "New Request", receiver);

                Sender sender = new Sender(data, token.getToken());

                apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if (response.code() == 200) {
                            if (response.body().success != 1) {
                                Toast.makeText(CheckTeacherProfile.this, "Request Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {   }
        });
    }*/

    private void changeButtonState() {
        requestRef.child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(receiver)) {
                    String request_type = Objects.requireNonNull(snapshot.child(receiver).child("request_type").getValue()).toString();

                    if (request_type.equals("sent")) {
                        send.setEnabled(true);
                        CURRENT_STATE = "Request Sent";
                        send.setBackgroundColor(Color.parseColor("#758283"));
                        send.setText("Cancel Request");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        accepted.child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(receiver)) {
                    String status = snapshot.child(receiver).child("status").getValue().toString();

                    if (status.equals("teacher")) {
                        CURRENT_STATE = "Accepted";
                        send.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void cancelTeacherRequest() {
        progressBar.setVisibility(View.VISIBLE);
        requestRef.child(sender).child(receiver).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                requestRef.child(receiver).child(sender).removeValue().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        send.setEnabled(true);
                        CURRENT_STATE = "Not Teacher";
                        send.setBackgroundColor(Color.parseColor("#1C8D73"));
                        send.setText("Send Request");
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(this, Objects.requireNonNull(task1.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTeacherRequest() {
        progressBar.setVisibility(View.VISIBLE);
        requestRef.child(sender).child(receiver).child("request_type").setValue("sent").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                requestRef.child(receiver).child(sender).child("request_type").setValue("received").addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        send.setEnabled(true);
                        CURRENT_STATE = "Request Sent";
                        send.setBackgroundColor(Color.parseColor("#758283"));
                        send.setText("Cancel Request");
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        /*sent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentModel studentModel = snapshot.getValue(StudentModel.class);
                sendNotifications(receiver, studentModel.getName(), studentModel.getName() + " sent you a student request");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });*/
    }

    @Override
    public void onBackPressed() {
        String checkId=getIntent().getStringExtra("check");
        if(checkId.equals("Student"))
        {
            startActivity(new Intent(getApplicationContext(), MyTeachers.class));
        }
        else {
            startActivity(new Intent(getApplicationContext(), SendRequest.class));
            finish();
        }
    }
}