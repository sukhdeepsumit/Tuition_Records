package com.example.tuitionrecords.TeacherActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAccountInfo extends AppCompatActivity {

    CircleImageView profile_picture;
    TextView name, email, gender, phone, city_state, subject, standard, about;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ImageView nameEdit, emailEdit, genderEdit, phoneEdit, locationEdit, subjectEdit, standardEdit, aboutEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_account_info);

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        gender = findViewById(R.id.gender_get);
        phone = findViewById(R.id.phone_get);
        city_state = findViewById(R.id.city_state_get);
        subject = findViewById(R.id.subject_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);

        profile_picture = findViewById(R.id.dp_upload);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeacherModel model = snapshot.getValue(TeacherModel.class);

                assert model != null;
                Glide.with(getApplicationContext()).load(model.getMyUri()).into(profile_picture);

                name.setText(model.getName());
                email.setText(model.getEmail());
                gender.setText(model.getGender());
                phone.setText(model.getContact());
                String location = model.getCity() + ", " + model.getState();
                city_state.setText(location);
                subject.setText(model.getContent());
                standard.setText(model.getStandard());
                about.setText(model.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        genderEdit = findViewById(R.id.gender_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        locationEdit = findViewById(R.id.city_state_edit);
        subjectEdit = findViewById(R.id.subject_edit);
        standardEdit = findViewById(R.id.standard_edit);
        aboutEdit = findViewById(R.id.about_edit);

        nameEdit.setOnClickListener(view -> updateProfileData("name"));
        emailEdit.setOnClickListener(view -> updateProfileData("email"));
        genderEdit.setOnClickListener(view -> updateProfileData("gender"));
        phoneEdit.setOnClickListener(view -> updateProfileData("phone"));
        locationEdit.setOnClickListener(view -> updateProfileData("location"));
        subjectEdit.setOnClickListener(view -> updateProfileData("content"));
        standardEdit.setOnClickListener(view -> updateProfileData("standard"));
        aboutEdit.setOnClickListener(view -> updateProfileData("about"));

    }

    private void updateProfileData(String check) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.update_teacher_profile_content))
                .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                .create();

        View myView = dialogPlus.getHolderView();
        final TextInputEditText editText = myView.findViewById(R.id.text);
        final AppCompatButton update = myView.findViewById(R.id.update);

        switch (check) {
            case "name" : editText.setText(name.getText().toString());
                break;

            case "email" : editText.setText(email.getText().toString());
                break;

            case "gender" : editText.setText(gender.getText().toString());
                break;

            case "phone" : editText.setText(phone.getText().toString());
                break;

            case "location" : editText.setText(city_state.getText().toString());
                break;

            case "content" : editText.setText(subject.getText().toString());
                break;

            case "standard" : editText.setText(standard.getText().toString());
                break;

            case "about" : editText.setText(about.getText().toString());
                break;
        }

        dialogPlus.show();

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String result = Objects.requireNonNull(editText.getText()).toString();

        update.setOnClickListener(view -> {
            if (check.equals("location")) {

                //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Teacher_profile");

                String[] loc = result.split(",");
                String city = loc[0];
                String state = loc[1];

                FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child("city").setValue(city);
                FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child("state").setValue(state);
            }
            else {
                FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child(check).setValue(result)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(aVoid -> {
                            Toast.makeText(this, "Could not updated", Toast.LENGTH_SHORT).show();
                        });
            }
            dialogPlus.dismiss();
            //Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TeacherAccountInfo.this, ShowTeacherActivity.class));
        finish();
    }
}