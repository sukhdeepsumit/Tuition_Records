package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckTeacherProfile extends AppCompatActivity {

    CircleImageView dp;
    TextView name, email, gender, location, subject, standard, about;
    AppCompatButton send;
    ProgressBar progressBar;

    DatabaseReference received, requestRef;

    String CURRENT_STATE;
    String sender, receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_teacher_profile);

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

        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiver = getIntent().getStringExtra("userId");

        received = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(receiver);
        requestRef = FirebaseDatabase.getInstance().getReference("Requests");

        received.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeacherModel teacherModel = snapshot.getValue(TeacherModel.class);

                name.setText(teacherModel.getName());
                email.setText(teacherModel.getEmail());
                gender.setText(teacherModel.getGender());
                location.setText(teacherModel.getCity() + ", " + teacherModel.getState());
                subject.setText(teacherModel.getContent());
                standard.setText(teacherModel.getStandard());
                about.setText(teacherModel.getAbout());

                Glide.with(getApplicationContext()).load(teacherModel.getMyUri()).into(dp);

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
    }

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
                    else {
                        send.setEnabled(true);
                        CURRENT_STATE = "Not Teacher";
                        send.setBackgroundColor(Color.parseColor("#1C8D73"));
                        send.setText("Send Request");
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
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SendRequest.class));
        finish();
    }
}