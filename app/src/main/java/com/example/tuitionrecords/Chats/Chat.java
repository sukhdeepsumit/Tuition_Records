package com.example.tuitionrecords.Chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem chat, users;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    String who;
    CircleImageView dp;
    TextView user_name;
    DatabaseReference reference;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dp=findViewById(R.id.dpUpload);
        user_name=findViewById(R.id.userName);
        currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        who=getIntent().getStringExtra("user");
        if(who.equals("teacher")) {
            reference = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(currentUser);
        }
        else
        {
            reference = FirebaseDatabase.getInstance().getReference("Students_Profile").child(currentUser);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue().toString();
                String photoUrl=snapshot.child("myUri").getValue().toString();
                user_name.setText(name);
                Glide.with(getApplicationContext()).load(photoUrl).into(dp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabLayout=findViewById(R.id.tabLayout);
        chat=findViewById(R.id.chatHere);
        users=findViewById(R.id.users);

        viewPager=findViewById(R.id.viewPager);
        pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),who);
        viewPager.setAdapter(pageAdapter);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition()==0 ||tab.getPosition()==1)
                {
                    pageAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




    }

    @Override
    public void onBackPressed() {
        if(who.equals("teacher")) {
            startActivity(new Intent(Chat.this, ShowTeacherActivity.class));
        }
        else
        {
            startActivity(new Intent(Chat.this, ShowStudentActivity.class));
        }
        finish();
    }
}