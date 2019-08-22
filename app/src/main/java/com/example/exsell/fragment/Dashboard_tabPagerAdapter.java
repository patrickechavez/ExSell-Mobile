package com.example.exsell.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Dashboard_tabPagerAdapter extends FragmentStatePagerAdapter {


    int countTab;

    public Dashboard_tabPagerAdapter(@NonNull FragmentManager fm, int countTab) {
        super(fm);

        this.countTab = countTab;
    }




    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                dashboard_hotdeals_fragment hotdeal = new dashboard_hotdeals_fragment();
                return hotdeal;

            case 1:
                dashboard_fixedprice_fragment shop = new dashboard_fixedprice_fragment();
                return shop;

            case 2:
                dashboard_auction_fragment auction = new dashboard_auction_fragment();
                return auction;

            case 3:
                dashboard_stories_fragment stories = new dashboard_stories_fragment();
                return stories;

        }

        return null;
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
