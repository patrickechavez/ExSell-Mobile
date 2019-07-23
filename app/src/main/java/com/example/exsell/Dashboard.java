package com.example.exsell;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.exsell.Models.UploadPicModel;
import com.example.exsell.fragment.Dashboard_tabPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    private FirebaseAuth mAuth;
    private DocumentReference docRef;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        docRef = firebaseFirestore.collection("users").document(user_id);

        TabLayout tabLayout = findViewById(R.id.dashboard_tabs);
        ViewPager Pager = findViewById(R.id.dashboard_viewpager);

        Dashboard_tabPagerAdapter Dashboard_tabPagerAdapter = new Dashboard_tabPagerAdapter(getSupportFragmentManager());
        Pager.setAdapter(Dashboard_tabPagerAdapter);
        tabLayout.setupWithViewPager(Pager);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        String user_id = mAuth.getCurrentUser().getUid();
        switch(item.getItemId()){

            case R.id.list_items:

                Intent listRemnants = new Intent(Dashboard.this, ListRemnants.class);
                startActivity(listRemnants);

                break;

            case R.id.account:
                Intent account = new Intent(Dashboard.this, Account.class);
                startActivity(account);

                break;

            case R.id.messages:
                Intent message = new Intent(Dashboard.this, Message.class);
                startActivity(message);

                break;

            case R.id.notification:
                break;

            case R.id.logout:

                mAuth.signOut();
                sendToLogin();
                break;

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void sendToLogin() {

        Intent login = new Intent(Dashboard.this, Login.class);
        startActivity(login);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            sendToLogin();
        }else{

            docRef.update("online", "true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            docRef.update("online", "true");
        }
    }

}
