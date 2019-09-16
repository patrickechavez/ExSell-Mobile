package com.example.exsell.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Profile_tabPagerAdapter extends FragmentStatePagerAdapter {

    int countTab;

    public Profile_tabPagerAdapter(@NonNull FragmentManager fm, int countTab) {
        super(fm);

        this.countTab = countTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                Profile_fixedprice_fragment fixedprice_fragment = new Profile_fixedprice_fragment();
                return fixedprice_fragment;

            case 1:
                Profile_auction_fragment auction_fragment = new Profile_auction_fragment();
                return auction_fragment;

            case 2:
                Profile_blogs_fragment blogs_fragment = new Profile_blogs_fragment();
                return blogs_fragment;
        }
        return null;
    }

    @Override
    public int getCount() { return countTab; }
}
