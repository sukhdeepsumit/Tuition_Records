package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class StudentAccountInfo extends AppCompatActivity {

    CircleImageView profile_picture;
    TextView name, email, gender, phone, city_state, standard, about;

    Uri imageUri;
    Bitmap bitmap;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ImageView nameEdit, emailEdit, genderEdit, phoneEdit, locationEdit, standardEdit, aboutEdit;

    private static final int GALLERY = 5;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account_info);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Slow Internet Connection....");
        progressDialog.setMessage("Uploading...");

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        phone = findViewById(R.id.phone_get);
        city_state = findViewById(R.id.city_state_get);
        gender = findViewById(R.id.gender_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);

        profile_picture = findViewById(R.id.dp_upload);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser!=null;
        String userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Students_Profile").child(userId);

        storageReference = FirebaseStorage.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentModel model = snapshot.getValue(StudentModel.class);
                assert model != null;

                if (model.getMyUri().equals("default")) {
                    profile_picture.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(getApplicationContext()).load(model.getMyUri()).into(profile_picture);
                }

                name.setText(model.getName());
                email.setText(model.getMyEmail());
                phone.setText(model.getMyContact());
                gender.setText(model.getMyGender());
                String location= model.getMyCity()+","+model.getMyState();
                city_state.setText(location);
                standard.setText(model.getMyStandard());
                about.setText(model.getMyDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        nameEdit = findViewById(R.id.name_edit);
       // emailEdit = findViewById(R.id.email_edit);
        genderEdit = findViewById(R.id.gender_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        locationEdit = findViewById(R.id.city_state_edit);
        standardEdit = findViewById(R.id.standard_edit);
        aboutEdit = findViewById(R.id.about_edit);

        profile_picture.setOnClickListener(view -> {
            openImage();
        });

        nameEdit.setOnClickListener(view -> updateProfileData("name"));
        emailEdit.setOnClickListener(view -> updateProfileData("myEmail"));
        genderEdit.setOnClickListener(view -> updateProfileData("myGender"));
        phoneEdit.setOnClickListener(view -> updateProfileData("myContact"));
        locationEdit.setOnClickListener(view -> updateProfileData("location"));
        standardEdit.setOnClickListener(view -> updateProfileData("myStandard"));
        aboutEdit.setOnClickListener(view -> updateProfileData("myDescription"));
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY && resultCode == RESULT_OK && data.getData() != null) {
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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog.show();
        ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();

                FirebaseDatabase.getInstance().getReference("Students_Profile").child(uid).child("myUri").setValue(url)
                        .addOnCompleteListener(task -> {
                            Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Profile Picture is not updated due an error", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        });
            });
        });

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateProfileData(String check)
    {
        final DialogPlus dialogPlus=DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.update_teacher_profile_content))
                .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                .create();

        View myView= dialogPlus.getHolderView();

        final TextInputEditText editText=myView.findViewById(R.id.text);


        final AppCompatButton update=myView.findViewById(R.id.update);

        switch (check)
        {
            case "name": editText.setText(name.getText().toString());
                break;


            case "myEmail" : editText.setText(email.getText().toString());
                break;

            case "myGender" : editText.setText(gender.getText().toString());
                break;

            case "myContact" : editText.setText(phone.getText().toString());
                break;

            case "location" : editText.setText(city_state.getText().toString());
                break;

            case "myStandard" : editText.setText(standard.getText().toString());
                break;

            case "myDescription" : editText.setText(about.getText().toString());
                break;
        }

        dialogPlus.show();

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        update.setOnClickListener(view -> {

            String result = Objects.requireNonNull(editText.getText()).toString();

            if (check.equals("location")) {

                String[] loc = result.split(",");
                String city = loc[0];
                String state = loc[1];

                FirebaseDatabase.getInstance().getReference("Students_Profile").child(uid).child("myCity").setValue(city);
                FirebaseDatabase.getInstance().getReference("Students_Profile").child(uid).child("myState").setValue(state);

//                map.put("city",city);
//                map.put("state",state);

            }
            else {
                FirebaseDatabase.getInstance().getReference("Students_Profile").child(uid).child(check).setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Record Updated",Toast.LENGTH_SHORT).show();
                        //map.put(check,result);
                    }
                });

            }
            dialogPlus.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StudentAccountInfo.this, ShowStudentActivity.class));
        finish();
    }
}