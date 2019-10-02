package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.exsell.fragment.Activity_tabPagerAdapter;
import com.example.exsell.fragment.Dashboard_tabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Activities");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tabLayout = findViewById(R.id.activity_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Buying"));
        tabLayout.addTab(tabLayout.newTab().setText("Selling"));
        tabLayout.addTab(tabLayout.newTab().setText("Bids"));
        tabLayout.addTab(tabLayout.newTab().setText("SOLD"));
        tabLayout.addTab(tabLayout.newTab().setText("UNAVAILABLE"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.activity_ViewPager);
        Activity_tabPagerAdapter pagerAdapter = new Activity_tabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
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

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
