package com.example.tuitionrecords.TeacherActivity.Authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpTeacherActivity extends AppCompatActivity {

    TextInputEditText name, email, password, confirmPassword, contact, city, state, content, standard, about;
    AppCompatButton signUp;
    CircleImageView profileImage;
    ProgressBar progressBar;
    RadioGroup gender;

    FirebaseAuth myAuth;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher_profile");
    StorageReference storageReference;

    private static final int GALLERY = 5;
    private Uri imageUri;

    Bitmap bitmap;

    RelativeLayout checkInternet;
    ImageView close;

    ScrollView layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_teacher);

        checkInternet = findViewById(R.id.check_internet);
        close = findViewById(R.id.close);

        layout = findViewById(R.id.layout);

        checkInternet();

        myAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        name = findViewById(R.id.name_text);
        email = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);
        confirmPassword = findViewById(R.id.confirm_password_text);
        gender = findViewById(R.id.gender);
        contact = findViewById(R.id.phone_text);
        city = findViewById(R.id.city_text);
        state = findViewById(R.id.state_text);
        content = findViewById(R.id.content_text);
        standard = findViewById(R.id.class_text);
        about = findViewById(R.id.description_text);

        about.setScroller(new Scroller(getApplicationContext()));
        about.setVerticalScrollBarEnabled(true);

        signUp = findViewById(R.id.signup);
        profileImage = findViewById(R.id.profile_picture);

        progressBar = findViewById(R.id.pg);

        progressBar.setVisibility(View.GONE);

        signUp.setOnClickListener(view -> {
            progressBar.requestFocus();
            hideKeyboard(view);
            checkDetails();
        });

        profileImage.setOnClickListener(view -> openImage());
    }

    private void checkDetails() {

        String myEmail = Objects.requireNonNull(email.getText()).toString();
        String myPassword = Objects.requireNonNull(password.getText()).toString();
        String myContact = Objects.requireNonNull(contact.getText()).toString();

        boolean cancel =false;
        View focusView = null;

        //check email
        if (TextUtils.isEmpty(myEmail)) {
            email.setError("Your Email is empty");
            focusView = email;
            cancel = true;
        }

        //check password
        if (TextUtils.isEmpty(myPassword)) {
            password.setError("Your Password is empty");
            focusView = password;
            cancel = true;
        }
        else if(!checkPassword(myPassword)) {
            password.setError("Your password does not match");
            focusView = confirmPassword;
            cancel  =true;
        }
        else if(myPassword.length() < 6) {
            password.setError("Your password is too short");
            focusView = password;
            cancel = true;
        }

        //check contact
        if (!checkContact(myContact)) {
            contact.setError("Your contact is invalid");
            focusView = contact;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            signInWithFirebase();
        }
    }

    private boolean checkPassword(String password) {
        String confPassword = Objects.requireNonNull(confirmPassword.getText()).toString();
        return password.equals(confPassword);
    }

    private boolean checkContact(String contact) {
        Pattern p = Pattern.compile("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$");

        Matcher m =p.matcher(contact);
        return m.find() && m.group().equals(contact);
    }

    private void signInWithFirebase() {

        String myEmail = Objects.requireNonNull(email.getText()).toString();
        String myPassword = Objects.requireNonNull(password.getText()).toString();

        progressBar.setVisibility(View.VISIBLE);

        myAuth.createUserWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("TAG", "createUserWithEmail:success");
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
                saveProfileDetails();
                Intent intent = new Intent(SignUpTeacherActivity.this, LogInTeacherActivity.class);
                finish();
                startActivity(intent);
            }
            else {
                showErrorBox("Registration Failed !! Try Again");
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profileImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveProfileDetails() {

        String nm = Objects.requireNonNull(name.getText()).toString();
        String em = Objects.requireNonNull(email.getText()).toString();
        String cn = Objects.requireNonNull(contact.getText()).toString();
        String gn = ((RadioButton)findViewById(gender.getCheckedRadioButtonId())).getText().toString();
        String ct = Objects.requireNonNull(city.getText()).toString();
        String st = Objects.requireNonNull(state.getText()).toString();
        String cnt = Objects.requireNonNull(content.getText()).toString();
        String sn = Objects.requireNonNull(standard.getText()).toString();
        String ab = Objects.requireNonNull(about.getText()).toString();

        StorageReference ref = storageReference.child("Photos/" + imageUri.getLastPathSegment());

        ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                        String url = uri.toString();
                        TeacherModel model = new TeacherModel(nm, em, cn,gn, ct, st, cnt, sn, ab, url);

                        String user = Objects.requireNonNull(myAuth.getCurrentUser()).getUid();
                        reference.child(user).setValue(model)
                                .addOnCompleteListener(task -> Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show());
                }));
    }

    private void showErrorBox(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Heyyyy")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpTeacherActivity.this, LogInTeacherActivity.class));
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