package com.example.tuitionrecords.TeacherActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tuitionrecords.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class FeeStatus extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem paid, notPaid;
    ViewPager viewPager;
    PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_status);

        tabLayout=findViewById(R.id.tabLayout);
        paid=findViewById(R.id.paid);
        notPaid=findViewById(R.id.notPaid);
        viewPager=findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);



        //jab tabs pr click krenge
       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //listens for page change... matlab jab hum swipe krenge

    }
}