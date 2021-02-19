package com.app_devs.tuitionrecords.TeacherActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.app_devs.tuitionrecords.R;
import com.app_devs.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAccountInfo extends AppCompatActivity {

    CircleImageView profile_picture;
    TextView name, email, gender, phone, city_state, subject, standard, about;

    private static final int GALLERY = 5;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ImageView nameEdit, emailEdit, genderEdit, phoneEdit, locationEdit, subjectEdit, standardEdit, aboutEdit;
    private Uri imageUri;
    Bitmap bitmap;

    String userId;
    StorageReference storageReference;

    RelativeLayout checkInternet;
    LinearLayout layout;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_account_info);

        checkInternet = findViewById(R.id.check_internet);
        layout = findViewById(R.id.main_layout);
        close = findViewById(R.id.close);
        checkInternet();

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Slow Internet Connection !!!");
        progressDialog.setMessage("Uploading...");

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        gender = findViewById(R.id.gender_get);
        phone = findViewById(R.id.phone_get);
        city_state = findViewById(R.id.city_state_get);
        subject = findViewById(R.id.subject_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);

        profile_picture = findViewById(R.id.dp_upload);

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeacherModel model = snapshot.getValue(TeacherModel.class);

                assert model != null;

                if (model.getMyUri().equals("default")) {
                    profile_picture.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(getApplicationContext()).load(model.getMyUri()).into(profile_picture);
                }

                name.setText(model.getName());
                email.setText(model.getEmail());
                gender.setText(model.getGender());
                phone.setText(model.getContact());
                String location = model.getCity() + "," + model.getState();
                city_state.setText(location);
                String sbj = model.getContent();
                sbj = sbj.substring(0,1).toUpperCase() + sbj.substring(1);
                subject.setText(sbj);
                standard.setText(model.getStandard());
                about.setText(model.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        nameEdit = findViewById(R.id.name_edit);
      //  emailEdit = findViewById(R.id.email_edit);
        genderEdit = findViewById(R.id.gender_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        locationEdit = findViewById(R.id.city_state_edit);
        subjectEdit = findViewById(R.id.subject_edit);
        standardEdit = findViewById(R.id.standard_edit);
        aboutEdit = findViewById(R.id.about_edit);

        profile_picture.setOnClickListener(view -> {
            openImage();
        });

        nameEdit.setOnClickListener(view -> updateProfileData("name"));
       // emailEdit.setOnClickListener(view -> updateProfileData("email"));
        genderEdit.setOnClickListener(view -> updateProfileData("gender"));
        phoneEdit.setOnClickListener(view -> updateProfileData("contact"));
        locationEdit.setOnClickListener(view -> updateProfileData("location"));
        subjectEdit.setOnClickListener(view -> updateProfileData("content"));
        standardEdit.setOnClickListener(view -> updateProfileData("standard"));
        aboutEdit.setOnClickListener(view -> updateProfileData("about"));
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

            case "contact" : editText.setText(phone.getText().toString());
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

        update.setOnClickListener(view -> {
            String result = Objects.requireNonNull(editText.getText()).toString();

            if (check.equals("location")) {

                String[] loc = result.split(",");
                String city = loc[0].trim();
                String state = loc[1].trim();

                FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child("city").setValue(city);
               FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child("state").setValue(state).addOnCompleteListener(task -> {
                   Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
               });
            }
            else if(check.equals("content")) {
                result = result.substring(0,1).toUpperCase() + result.substring(1);
                FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child(check).setValue(result).addOnCompleteListener(task -> {
                    Toast.makeText(getApplicationContext(),"Record Updated",Toast.LENGTH_SHORT).show();
                });
            }
            else {
                FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child(check).setValue(result).addOnCompleteListener(task -> {
                    Toast.makeText(getApplicationContext(),"Record Updated",Toast.LENGTH_SHORT).show();
                });
            }
         dialogPlus.dismiss();
        });
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile_picture.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        StorageReference ref = storageReference.child("Photos/" + imageUri.getLastPathSegment());
        progressDialog.show();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();

                FirebaseDatabase.getInstance().getReference("Teacher_profile").child(uid).child("myUri").setValue(url).addOnCompleteListener(task -> {
                    Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }).addOnFailureListener(e -> Toast.makeText(this, "Could not be updated", Toast.LENGTH_SHORT).show());
            });
        });
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TeacherAccountInfo.this, ShowTeacherActivity.class));
        finish();
    }
}