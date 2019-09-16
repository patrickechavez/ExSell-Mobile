package com.example.exsell.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Activity_tabPagerAdapter extends FragmentStatePagerAdapter {

    int countTab;

    public Activity_tabPagerAdapter(@NonNull FragmentManager fm, int counTab) {
        super(fm);

        this.countTab = counTab;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:

               Activity_buying_fragment activity_buying_fragment = new Activity_buying_fragment();
                return activity_buying_fragment;

            case 1:
                Activity_selling_fragment activity_selling_fragment = new Activity_selling_fragment();
                return activity_selling_fragment;

            case 2:
                Activity_bid_fragment activity_bid_fragment = new Activity_bid_fragment();
                return activity_bid_fragment;
        }
        return null;
    }

    @Override
    public int getCount() { return countTab; }
}
