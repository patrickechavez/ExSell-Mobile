package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Message extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ViewPager mViewPager;
    private Message_SectionsPagerAdapter message_sectionsPagerAdapter;
    private TabLayout mTabLayout;
    private FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        Toolbar toolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Messages");


        docRef = firebaseFirestore.collection("users").document(user_id);
        mViewPager = findViewById(R.id.message_tabPager);
        message_sectionsPagerAdapter = new Message_SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(message_sectionsPagerAdapter);

        mTabLayout = findViewById(R.id.message_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null) {
            sendToLogin();
        }else{

            docRef.update("online", "true");
        }

    }
    @Override
    protected void onStop() {
        super.onStop();

        if(currentUser != null) {
            docRef.update("online", "false");
        }

    }

    private void sendToLogin() {

        Intent login = new Intent(Message.this, Login.class);
        startActivity(login);
        finish();
    }
}
