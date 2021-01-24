package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTeacherProfile extends AppCompatActivity {

    CircleImageView dp;
    TextView name, email, gender, location, subject, standard, about, batch;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher_profile");
    DatabaseReference batches = FirebaseDatabase.getInstance().getReference("Allot_Batches");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teacher_profile);

        dp = findViewById(R.id.dp_upload);

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        gender = findViewById(R.id.gender_get);
        location = findViewById(R.id.city_state_get);
        subject = findViewById(R.id.subject_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);
        batch = findViewById(R.id.batch);

        String teacher_key = getIntent().getStringExtra("userId");

        reference.child(teacher_key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeacherModel model = snapshot.getValue(TeacherModel.class);

                assert model != null;
                name.setText(model.getName());
                email.setText(model.getEmail());
                gender.setText(model.getGender());
                location.setText(model.getCity() + ", " + model.getState());
                subject.setText(model.getContent());
                standard.setText(model.getStandard());
                about.setText(model.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        String student_key = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        batches.child(student_key).child(teacher_key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                batch.setText("Batch No. " + Objects.requireNonNull(snapshot.child("batch").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}