package com.example.tuitionrecords.Chats.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.Chats.Chat;
import com.example.tuitionrecords.Chats.Model.ChatShowModel;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.example.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    //commit check

    RecyclerView recyclerView;
    TextView username;
    EditText sendText;

    CircleImageView profileImage;
    ImageButton sendButton;
    ImageView call;

    DatabaseReference reference;
    String userId, who;

    List<ChatShowModel> messageChats;

    MessageAdapter adapter;
    FirebaseUser firebaseUser;
    String contact;

    private static final int REQUEST_PHONE_CALL=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        call=findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        sendButton = findViewById(R.id.btn_send);
        sendText = findViewById(R.id.text_send);

        userId = getIntent().getStringExtra("userId");
        who = getIntent().getStringExtra("user");

        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(MessageActivity.this, Chat.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user", who);
            startActivity(intent);
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (who.equals("student")) {
            reference = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(userId);
        }
        else {
            reference = FirebaseDatabase.getInstance().getReference("Students_Profile").child(userId);
        }

        sendButton.setOnClickListener(view -> {
            String message = sendText.getText().toString();
            if (message.equals("")) {
                Toast.makeText(this, "You cannot send an empty message", Toast.LENGTH_SHORT).show();
            }
            else {
                sendMessage(firebaseUser.getUid(), userId, message);
            }
            sendText.setText("");
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (who.equals("student")) {
                    TeacherModel model = snapshot.getValue(TeacherModel.class);

                    Log.i("TEACHER_KEY_ID", userId);

                    assert model != null;
                    username.setText(model.getName());
                    Glide.with(getApplicationContext()).load(model.getMyUri()).into(profileImage);
                    contact=model.getContact();
                    Log.i("TEACHER_CONTACT",contact);

                    displayMessage(firebaseUser.getUid(), userId, model.getMyUri());
                }
                else {
                    StudentModel model = snapshot.getValue(StudentModel.class);

                    Log.i("STUDENT_KEY_ID", userId);

                    assert model != null;
                    username.setText(model.getName());
                    Glide.with(getApplicationContext()).load(model.getMyUri()).into(profileImage);
                    contact=model.getMyContact();
                    Log.i("STUDENT_CONTACT",contact);

                    displayMessage(firebaseUser.getUid(), userId, model.getMyUri());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    private void makePhoneCall()
    {
        if(contact.length()>0)
        {
            if(ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MessageActivity.this
                        ,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

            }
            else
            {
                String dial="tel:"+contact;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_PHONE_CALL)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                makePhoneCall();
            }
            else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(userId);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(receiver)
                .child(firebaseUser.getUid());

        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef1.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void displayMessage(String myId, String userId, String imageUrl) {
        messageChats = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageChats.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatShowModel chats = dataSnapshot.getValue(ChatShowModel.class);

                    if (chats.getReceiver().equals(myId) && chats.getSender().equals(userId) ||
                         chats.getReceiver().equals(userId) && chats.getSender().equals(myId))
                    {
                        messageChats.add(chats);
                    }
                    adapter = new MessageAdapter(MessageActivity.this, messageChats, imageUrl);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
    }
}