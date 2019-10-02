package com.example.exsell;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.fragment.Dashboard_tabPagerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageViewDashboard;
    private double wallet;
    private static final String TAG = "DASHBOARD";
    private DrawerLayout drawerLayout;
    private String remnant_id, seller_id, remnantName, owner_id, endTimeString;
    private long endTime;
    private SimpleDateFormat df = new SimpleDateFormat("h:mm a");
    private Boolean isSuccess = false;
    private CircleImageView circleImageView;
    private TextView textViewFirstName, textViewLastName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //BADGE
        BadgeDrawable badgeDrawable = BadgeDrawable.create(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();









        drawerLayout = findViewById(R.id.drawer_layout);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Featured"));
        tabLayout.addTab(tabLayout.newTab().setText("Remnants"));
        tabLayout.addTab(tabLayout.newTab().setText("Auction"));
        tabLayout.addTab(tabLayout.newTab().setText("Blogs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.ViewPager);
        Dashboard_tabPagerAdapter pagerAdapter = new Dashboard_tabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

        getWalletValue();




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
         circleImageView = headerView.findViewById(R.id.navigation_imageView);
         textViewFirstName = headerView.findViewById(R.id.navigation_firstName);
         textViewLastName = headerView.findViewById(R.id.navigation_lastName);


         fetchUserInfo();


    }

    private void fetchUserInfo() {

        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();

                    Picasso.get().load(documentSnapshot.getString("imageUrl")).into(circleImageView);
                    textViewFirstName.setText(documentSnapshot.getString("firstName"));
                    textViewLastName.setText(documentSnapshot.getString("lastName"));

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){


            case R.id.search_icon:


                break;
            case R.id.shopping_cart:

                Intent toCart = new Intent(Dashboard.this, Cart.class);
                startActivity(toCart);

                break;
        }
        return super.onOptionsItemSelected(item);
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
        int id = item.getItemId();

        switch (id){


            case R.id.nav_listRemnant:

                if(wallet < 25){

                    showSnackBar();

                }else {

                    Intent lm = new Intent(Dashboard.this   , ListRemnants.class);
                    startActivity(lm);
                }
                break;

            case R.id.nav_chat:

                Intent message = new Intent(Dashboard.this, Message.class);
                startActivity(message);
                break;

            case R.id.nav_notification:

                Intent notification = new Intent(Dashboard.this, Notification.class);
                startActivity(notification);

                break;

            case R.id.nav_activity:

                Intent toActivity = new Intent(Dashboard.this, Activity.class);
                startActivity(toActivity);
                break;

            case R.id.nav_profile:

                Intent profile = new Intent(Dashboard.this, Profile.class);
                startActivity(profile);
                break;

            case R.id.nav_logout:

                mAuth.signOut();
                finish();
                Intent logout = new Intent(Dashboard.this, Login.class);
                startActivity(logout);
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getWalletValue() {

       DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {

               if(task.isSuccessful()){
                   DocumentSnapshot documentSnapshot = task.getResult();

                   if(documentSnapshot.getDouble("wallet") > 24){
                       wallet = documentSnapshot.getDouble("wallet");
                   }
               }
           }
       });
    }

    private void showSnackBar() {

        Snackbar snackbar = Snackbar.make(drawerLayout, "Must have atleast 25 coins in your wallet", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser  firebaseUser =  mAuth.getCurrentUser();

        if(firebaseUser == null){

            Intent i = new Intent(Dashboard.this, Login.class);
            startActivity(i);
        }
    }
}
