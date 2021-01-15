package com.example.tuitionrecords.TeacherActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    int tabCount;
    private String[] tabTitles = new String[]{"Not Paid", "Paid"};

   //   the tabs displayed in the layout will be populated from the ViewPager adapter's page titles.
   //If you would like to use your TabLayout with a ViewPager,
   // you should override getPageTitle() in your PagerAdapter (and remove the addTab() calls, they are redundant).

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new FeeNotPaidFragment();
            case 1: return new FeePaidFragment();
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
