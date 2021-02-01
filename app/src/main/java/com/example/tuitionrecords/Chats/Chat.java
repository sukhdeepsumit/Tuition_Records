    package com.example.tuitionrecords.Chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Chat extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem chat, users;

    ViewPager viewPager;
    PageAdapter pageAdapter;

    String who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tabLayout = findViewById(R.id.tabLayout);
        chat = findViewById(R.id.chatHere);
        users = findViewById(R.id.users);

        who = getIntent().getStringExtra("user");

        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), who);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
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