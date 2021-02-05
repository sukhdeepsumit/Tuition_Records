package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;

public class Contact_us extends AppCompatActivity {

    RelativeLayout email_contact, feedback;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        email_contact = findViewById(R.id.contact_email);
        feedback = findViewById(R.id.feedback_contact);

        String subject = "Subject";
        String body = "Body";
        String email = "app.devs.feedback@gmail.com";

        email_contact.setOnClickListener(view -> sendEmail(Contact_us.this, email, subject, body));

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= Uri.parse("https://tuitionrecordsfeedback.blogspot.com/");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));

            }
        });
    }

    private void sendEmail(Context context, String email, String subject, String body) {
        StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(email));
        builder.append("?subject=").append(Uri.encode(Uri.encode(subject)));
        builder.append("&body=").append(Uri.encode(Uri.encode(body)));

        String uri = builder.toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        String checkId=getIntent().getStringExtra("userId");
        if(checkId.equals("Teacher"))
        {
            startActivity(new Intent(Contact_us.this,ShowTeacherActivity.class));

        }
        else
        {
            startActivity(new Intent(Contact_us.this, ShowStudentActivity.class));

        }
        finish();
    }
}