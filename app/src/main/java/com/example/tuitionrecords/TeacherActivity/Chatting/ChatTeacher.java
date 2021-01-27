package com.example.tuitionrecords.TeacherActivity.Chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ChatTeacher extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem chat, users, profile;
    ViewPager viewPager;
    PageAdapter pageAdapter;

//goodnight
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_teacher);
        tabLayout=findViewById(R.id.tabLayout);
        chat=findViewById(R.id.chatHere);
        users=findViewById(R.id.users);
        profile=findViewById(R.id.profileShow);
        viewPager=findViewById(R.id.viewPager);
        pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition()==0 ||tab.getPosition()==1 ||tab.getPosition()==2 )
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
        startActivity(new Intent(ChatTeacher.this, ShowTeacherActivity.class));
        finish();
    }
}