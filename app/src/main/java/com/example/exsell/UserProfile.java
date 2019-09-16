package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exsell.fragment.Profile_tabPagerAdapter;
import com.example.exsell.fragment.UserProfile_tabPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageView profileImageView;
    private TextView profileFirstName, profileLastName;
    private String seller_id;
    private TextView textViewReportUser;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);

        Toolbar toolbar = findViewById(R.id.userProfile_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        seller_id = getIntent().getStringExtra("seller_id");


        profileFirstName = findViewById(R.id.userProfile_textViewFirstName);
        profileLastName = findViewById(R.id.userProfile_textViewLastName);
        profileImageView = findViewById(R.id.userProfile_circleImageView);
        textViewReportUser = findViewById(R.id.user_reportUserTextView);



        textViewReportUser.setOnClickListener(this);

        fetchDataUser();

        tabLayout = findViewById(R.id.userProfile_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("REMNANTS"));
        tabLayout.addTab(tabLayout.newTab().setText("AUCTION"));
        tabLayout.addTab(tabLayout.newTab().setText("BLOG"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.userProfile_ViewPager);
        UserProfile_tabPagerAdapter userProfile_tabPagerAdapter = new UserProfile_tabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(userProfile_tabPagerAdapter);
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


        // MAO NI ANG SET SA SESSION

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.edit().putString("seller_id", seller_id).apply();



    }

    private void fetchDataUser() {

        DocumentReference docRef = firebaseFirestore.collection("users").document(seller_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot document  = task.getResult();

                    Picasso.get().load(document.getString("imageUrl")).placeholder(R.drawable.user).error(R.drawable.user)
                    .into(profileImageView);
                    profileFirstName.setText(document.getString("firstName"));
                    profileLastName.setText(document.getString("lastName"));


                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.user_reportUserTextView:

                Intent toReport = new Intent(UserProfile.this, ReportUsers.class);
                toReport.putExtra("seller_id",seller_id);
                startActivity(toReport);
                break;

            case R.id.user_messageUserTextView:

                Intent message = new Intent(UserProfile.this, Chat.class);
                message.putExtra("receiver_id", seller_id);
                startActivity(message);
                break;



        }
    }
}
