package com.example.tuitionrecords.TeacherActivity.Requests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckStudentProfile extends AppCompatActivity {

    CircleImageView dp;
    TextView name, email, gender, location, standard, about;
    AppCompatButton accept, reject;
    ProgressBar progressBar;

    String currentUser, requestUser;
    DatabaseReference reference, acceptedStudent, requestRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_student_profile);

        dp = findViewById(R.id.dp_upload);

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        gender = findViewById(R.id.gender_get);
        location = findViewById(R.id.city_state_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);

        progressBar = findViewById(R.id.progress_request);
        progressBar.setVisibility(View.GONE);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        requestUser = getIntent().getStringExtra("userId");

        reference = FirebaseDatabase.getInstance().getReference().child("Students_Profile").child(requestUser);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentModel studentModel = snapshot.getValue(StudentModel.class);

                name.setText(studentModel.getName());
                email.setText(studentModel.getMyEmail());
                gender.setText(studentModel.getMyGender());
                location.setText(studentModel.getMyCity() + ", " + studentModel.getMyState());
                standard.setText(studentModel.getMyStandard());
                about.setText(studentModel.getMyDescription());

                Glide.with(getApplicationContext()).load(studentModel.getMyUri()).into(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        acceptedStudent = FirebaseDatabase.getInstance().getReference("Accepted_Students");
        requestRef = FirebaseDatabase.getInstance().getReference("Requests");

        accept.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            acceptedStudent.child(currentUser).child(requestUser).child("status").setValue("student").addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    acceptedStudent.child(requestUser).child(currentUser).child("status").setValue("teacher").addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            requestRef.child(currentUser).child(requestUser).removeValue();
                            requestRef.child(requestUser).child(currentUser).removeValue();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
            startActivity(new Intent(CheckStudentProfile.this, RequestActivity.class));
            finish();
        });

        reject.setOnClickListener(view -> {
            requestRef.child(currentUser).child(requestUser).removeValue();
            requestRef.child(requestUser).child(currentUser).removeValue();
            startActivity(new Intent(CheckStudentProfile.this, RequestActivity.class));
            finish();
        });
    }
}