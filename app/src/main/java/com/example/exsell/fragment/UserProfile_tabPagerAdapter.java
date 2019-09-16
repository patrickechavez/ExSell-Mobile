package com.example.exsell.fragment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class UserProfile_tabPagerAdapter extends FragmentStatePagerAdapter {

    int countTab;

    public UserProfile_tabPagerAdapter(@NonNull FragmentManager fm, int counTab) {
        super(fm);

        this.countTab = counTab;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:


                UserProfile_fixedprice_fragment userProfile_fixedprice_fragment = new UserProfile_fixedprice_fragment();
                return userProfile_fixedprice_fragment;

            case 1:
                UserProfile_auction_fragment userProfile_auction_fragment = new UserProfile_auction_fragment();
                return userProfile_auction_fragment;

            case 2:
                UserProfile_blogs_fragment userProfile_blogs_fragment = new UserProfile_blogs_fragment();
                return userProfile_blogs_fragment;


        }
        return null;
    }

    @Override
    public int getCount() { return countTab; }
}
