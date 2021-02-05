package com.example.tuitionrecords.StudentActivity.Authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlusBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpStudentActivity extends AppCompatActivity {
    Button signUpStudentBtn;
    private FirebaseAuth mAuth;

    EditText mName, mEmail,mContact,mPwd,mCnfPwd, mStandard,mCity,mState,mDescription;
    ProgressBar progressBar;

    CircleImageView dp;
    Uri uri;
    RadioGroup mGender;

    StorageReference mStorage;
    DatabaseReference db=FirebaseDatabase.getInstance().getReference("Students_Profile");

    private static final int GALLERY=5;
    Bitmap bitmap;

    boolean flag = false;

    RelativeLayout checkInternet;
    ImageView close;
    ScrollView layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);
        checkInternet = findViewById(R.id.check_internet);
        close = findViewById(R.id.close);
        layout = findViewById(R.id.layout);

        checkInternet();

        mName=findViewById(R.id.name_text);
        mEmail=findViewById(R.id.email_text);
        mContact=findViewById(R.id.phone_text);
        mPwd=findViewById(R.id.signUpPassword);
        mCnfPwd=findViewById(R.id.confirmPassword);
        mStandard=findViewById(R.id.classes_text);
        mCity=findViewById(R.id.city_text);
        mState=findViewById(R.id.state_text);
        mDescription=findViewById(R.id.description_text);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        signUpStudentBtn=findViewById(R.id.signUpButton);
        dp=findViewById(R.id.dpUpload);

        mStorage=FirebaseStorage.getInstance().getReference();

        mGender = findViewById(R.id.gender);

        mDescription.setScroller(new Scroller(getApplicationContext()));
        mDescription.setVerticalScrollBarEnabled(true);

        dp.setOnClickListener(v -> {
           openImage();
        });


        signUpStudentBtn.setOnClickListener(v -> {

            progressBar.requestFocus();
            hideKeybaord(v);

            final  String myEmail=mEmail.getText().toString().trim();
            final String mobNum=mContact.getText().toString().trim();
            final String password= mPwd.getText().toString().trim();
            final String cnfPassword=mCnfPwd.getText().toString().trim();
            final String myStandard= mStandard.getText().toString().trim();
            final String myName= mName.getText().toString().trim();
            final String myCity= mCity.getText().toString().trim();
            final String myState=mState.getText().toString().trim();





            if(TextUtils.isEmpty(myEmail))
            {
                mEmail.setError("Enter email !");
                mEmail.requestFocus();
            }

            else if(TextUtils.isEmpty(myName))
            {
                mName.setError("Name cannot be empty");
                mName.requestFocus();
            }
            else if(!myEmail.contains("@gmail.com") ||!myEmail.contains("@yahoo.com")||!myEmail.contains("@hotmail.com") ||!myEmail.contains("@rediffmail.com") || !myEmail.contains("@outlook.com"))
            {
                mEmail.setError("Have you entered it correctly? ");
                mEmail.requestFocus();
            }
            else if(TextUtils.isEmpty(myCity))
            {
                mCity.setError("Name cannot be empty");
                mCity.requestFocus();
            }
            else if(TextUtils.isEmpty(myState))
            {
                mState.setError("Name cannot be empty");
                mState.requestFocus();
            }
            else if(TextUtils.isEmpty(myStandard))
            {
                mStandard.setError("Enter your class");
                mStandard.requestFocus();
            }
            else if(TextUtils.isEmpty(mobNum))
            {
                mContact.setError("Enter contact !");
                mContact.requestFocus();
            }
            else if(!checkContact(mobNum))
            {
                mContact.setError("Your contact is invalid");
                mContact.requestFocus();
            }

            else if(TextUtils.isEmpty(password) )
            {
                mPwd.setError("Enter password !");
                mPwd.requestFocus();
            }
            else if(password.length()<=6)
            {
                mPwd.setError("Password length too short");
                mPwd.requestFocus();
            }
            else if(TextUtils.isEmpty(cnfPassword) )
            {
                mCnfPwd.setError("Enter password again !");
                mCnfPwd.requestFocus();
            }
            else if(!password.equals(cnfPassword))
            {
                mCnfPwd.setError("Incorrect password! ");
                mCnfPwd.requestFocus();
            }

            else {
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(myEmail, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("TAG", "createUserWithEmail:success");
                                Toast.makeText(SignUpStudentActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                saveStudentProfileDetails();
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpStudentActivity.this, "Failure" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        });
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY);
    }

    //To put the chosen image in imgView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==GALLERY && resultCode==RESULT_OK)
        {
            assert data != null;
            uri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                dp.setImageBitmap(bitmap);
                flag = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Saving the data to firebase
    private void saveStudentProfileDetails()
    {
        String name=mName.getText().toString();
        String  email=mEmail.getText().toString();
        String contact=mContact.getText().toString() ;
        String gender=((RadioButton)findViewById(mGender.getCheckedRadioButtonId())).getText().toString();
        String standard=mStandard.getText().toString();
        String city=mCity.getText().toString();
        String state=mState.getText().toString();
        String description=mDescription.getText().toString();

        StorageReference uploader;

        if (!flag) {
            insertDetails(name, email, gender, contact, standard, city, state, description, "default", mAuth.getCurrentUser().getUid());


        }
        else {
            uploader = mStorage.child("Photos/" + uri.getLastPathSegment());

            uploader.putFile(uri).addOnSuccessListener(taskSnapshot -> uploader.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        String url = uri.toString();
                        String user = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                        insertDetails(name, email, gender, contact, standard, city, state, description, url, user);




                    }))
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show());
        }
     }

    private void insertDetails(String name, String email, String gender, String contact, String standard, String city, String state, String description, String url, String user) {
        StudentModel sm = new StudentModel(name, email, gender, contact, standard, city, state, description, url, user, "offline");
        Log.i("USER", user);

        db.child(user).setValue(sm).addOnCompleteListener(task -> {
            Toast.makeText(getApplicationContext(), "Record Saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUpStudentActivity.this, LogInStudentActivity.class));
            finish();
        });
    }

    private boolean checkContact(String contact)
    {
        Pattern p = Pattern.compile("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$");

        Matcher m = p.matcher(contact);
        return (m.find() && m.group().equals(contact));
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpStudentActivity.this, LogInStudentActivity.class));
        finish();
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
