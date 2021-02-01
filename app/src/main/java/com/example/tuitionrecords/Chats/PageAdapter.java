package com.example.tuitionrecords.Chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tuitionrecords.Chats.Message.ChatFragment;
import com.example.tuitionrecords.Chats.Users.UsersFragment;

public class PageAdapter extends FragmentPagerAdapter {

    String role;
    int tabCount;

    public PageAdapter(@NonNull FragmentManager fragment, int behavior, String user) {
        super(fragment, behavior);

        tabCount = behavior;
        role = user;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0: return new ChatFragment(role);

            case 1: return new UsersFragment(role);

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0: return "Chats";

            case 1: return "Users";

            default: return null;
        }
    }
}
