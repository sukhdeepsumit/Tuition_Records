package com.example.tuitionrecords.Chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.Chats.Message.ChatFragment;
import com.example.tuitionrecords.Chats.Model.ChatShowModel;
import com.example.tuitionrecords.Chats.Users.UsersFragment;
import com.example.tuitionrecords.Notifications.Token;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

//commit check

public class Chat extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    String who;
    CircleImageView dp;
    TextView user_name;
    DatabaseReference reference;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dp=findViewById(R.id.dpUpload);
        user_name=findViewById(R.id.userName);

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
                if (photoUrl.equals("default")) {
                    dp.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(getApplicationContext()).load(photoUrl).into(dp);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatShowModel chats = dataSnapshot.getValue(ChatShowModel.class);

                    Log.i("ERROR_CHECK_WHICH", "" + chats.getReceiver());

                    if (chats.getReceiver().equals(currentUser) && !chats.isIsseen()) {
                        unread++;
                    }
                }

                if (unread == 0) {
                    viewPagerAdapter.addFragments(new ChatFragment(who), "Chats");
                }
                else {
                    viewPagerAdapter.addFragments(new ChatFragment(who), "Chats (" + unread + ")");
                }

                viewPagerAdapter.addFragments(new UsersFragment(who), "Users");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void status(String status) {
        if (who.equals("teacher")) {
            reference = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(currentUser);
        }
        else {
            reference = FirebaseDatabase.getInstance().getReference("Students_Profile").child(currentUser);
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token tk = new Token(token);
        reference.child(currentUser).setValue(tk);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
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

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }
    }
}