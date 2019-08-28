package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.exsell.fragment.Profile_tabPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textViewFirstName, textViewLastName, textViewGetCoins;
    private CircleImageView circleImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = findViewById(R.id.profile_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PROFILE");

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        circleImageViewProfile = findViewById(R.id.profile_circleImageView);
        textViewFirstName = findViewById(R.id.profile_textViewFirstName);
        textViewLastName = findViewById(R.id.profile_textViewLastName);
        textViewGetCoins = findViewById(R.id.profile_textViewGetCoins);


        textViewGetCoins.setOnClickListener(this);

        tabLayout = findViewById(R.id.profile_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("REMNANTS"));
        tabLayout.addTab(tabLayout.newTab().setText("AUCTION"));
        tabLayout.addTab(tabLayout.newTab().setText("BLOG"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.profile_ViewPager);
        Profile_tabPagerAdapter profile_tabPagerAdapter = new Profile_tabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(profile_tabPagerAdapter);
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
    public void onClick(View v) {


        Intent getCoins = new Intent(Profile.this, Wallet.class);
        startActivity(getCoins);
    }
}
