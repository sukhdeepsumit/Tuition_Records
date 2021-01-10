package com.example.tuitionrecords;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

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
}