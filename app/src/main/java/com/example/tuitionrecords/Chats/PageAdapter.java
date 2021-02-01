package com.example.tuitionrecords.Chats;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tuitionrecords.Chats.ChatFragment;
import com.example.tuitionrecords.Chats.UsersFragment;

public class PageAdapter extends FragmentPagerAdapter {
    String role;
    int tabCount;
    public PageAdapter(@NonNull FragmentManager fm, int behavior, String user) {
        super(fm, behavior);
        tabCount=behavior;
        role=user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new ChatFragment();
            case 1: return new UsersFragment(role);
            default: return null;

        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
