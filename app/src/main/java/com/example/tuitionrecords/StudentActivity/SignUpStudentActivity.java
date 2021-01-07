package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpStudentActivity extends AppCompatActivity {
    Button signUpStudentBtn;
    private FirebaseAuth mAuth;
    EditText mName, mEmail,mContact,mPwd,mCnfPwd, mStandard,mCity,mState,mDescription;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);
        mName=findViewById(R.id.name_text);
        mEmail=findViewById(R.id.email_text);
        mContact=findViewById(R.id.phone_text);
        mPwd=findViewById(R.id.signUpPassword);
        mCnfPwd=findViewById(R.id.confirmPassword);
        mStandard=findViewById(R.id.standard);
        mCity=findViewById(R.id.city_text);
        mState=findViewById(R.id.classes_text);
        mDescription=findViewById(R.id.description_text);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        signUpStudentBtn=findViewById(R.id.signUpButton);
        signUpStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeybaord(v);

                final  String myEmail=mEmail.getText().toString().trim();
                final String mobNum=mContact.getText().toString().trim();

                final String password= mPwd.getText().toString().trim();
                final String cnfPassword=mCnfPwd.getText().toString().trim();

                if(TextUtils.isEmpty(myEmail))
                {
                    mEmail.setError("Enter email !");
                    mEmail.requestFocus();
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
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.i("TAG", "createUserWithEmail:success");
                                        Toast.makeText(SignUpStudentActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(SignUpStudentActivity.this, LogInStudentActivity.class));
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpStudentActivity.this, "Failure" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            }
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


    }
