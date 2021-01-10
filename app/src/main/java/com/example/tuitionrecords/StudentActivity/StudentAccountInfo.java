package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tuitionrecords.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAccountInfo extends AppCompatActivity {

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
        reference = FirebaseDatabase.getInstance().getReference("Students_Profile").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentModel model = snapshot.getValue(StudentModel.class);

                Glide.with(getApplicationContext()).load(model.getMyUri()).into(profile_picture);

                name.setText(model.getName());
                email.setText(model.getMyEmail());
                phone.setText(model.getMyContact());
                city.setText(model.getMyCity());
                state.setText(model.getMyState());
                subject.setText(model.getMyContact());
                standard.setText(model.getMyStandard());
                about.setText(model.getMyDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}