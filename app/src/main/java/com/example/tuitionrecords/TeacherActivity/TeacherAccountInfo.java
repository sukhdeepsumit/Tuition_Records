package com.example.tuitionrecords.TeacherActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAccountInfo extends AppCompatActivity {

    CircleImageView profile_picture;
    TextView name, email, phone, city, state, subject, standard, about;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_account_info);

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        phone = findViewById(R.id.phone_get);
        city = findViewById(R.id.city_get);
        state = findViewById(R.id.state_get);
        subject = findViewById(R.id.subject_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);

        profile_picture = findViewById(R.id.dp_upload);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeacherModel model = snapshot.getValue(TeacherModel.class);

                Glide.with(getApplicationContext()).load(model.getMyUri()).into(profile_picture);

                name.setText(model.getName());
                email.setText(model.getEmail());
                phone.setText(model.getContact());
                city.setText(model.getCity());
                state.setText(model.getState());
                subject.setText(model.getContent());
                standard.setText(model.getStandard());
                about.setText(model.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}