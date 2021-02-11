package com.example.tuitionrecords.StudentActivity.MyTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    RelativeLayout checkInternet;
    LinearLayout layout;
    ImageView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teacher_profile);
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

                if (model.getMyUri().equals("default")) {
                    dp.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(getApplicationContext()).load(model.getMyUri()).into(dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        String student_key = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        batches.child(student_key).child(teacher_key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String batchNumber = snapshot.child("batch").getValue().toString();
                if (batchNumber.equals("Allot Batch No.")) {
                    batch.setText("Batch number not alloted");
                }
                else {
                    batch.setText("Batch No. " + Objects.requireNonNull(snapshot.child("batch").getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
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
}