package com.example.socialnapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ActivityFragment activityFragment = new ActivityFragment();
                return activityFragment;

            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;

                default:
                    return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
        case 0:
            return "Selfie Master";

        case 1:
            return "CHAT";
    }
        return super.getPageTitle(position);


    }
}
