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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.fragment.Dashboard_tabPagerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageViewDashboard;
    private double wallet;
    private TextView textViewFirstName, textViewLastName;
    private static final String TAG = "DASHBOARD";
    private DrawerLayout drawerLayout;
    private String remnant_id, seller_id, remnantName, owner_id, endTimeString;
    private long endTime;
    private SimpleDateFormat df = new SimpleDateFormat("h:mm a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        getWalletValue();
        sendNotiftoBidWinner();


        DocumentReference user_id = firebaseFirestore.collection("notification").document();
        String user_ids = user_id.getId();

    }

    private void sendNotiftoBidWinner() {


        Thread t = new Thread(){

            @Override
            public void run() {
                try {

                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                firebaseFirestore.collection("remnants")
                                        .whereEqualTo("type", "Auction")
                                        .whereEqualTo("isSoldOut", false)
                                        .whereEqualTo("isDeleted", false)
                                        .whereEqualTo("isActive", true)
                                        .whereEqualTo("isExpired", false)
                                        .get().addOnCompleteListener(task -> {

                                            if(task.isSuccessful()){

                                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                                     remnant_id = queryDocumentSnapshot.getId();
                                                     seller_id = queryDocumentSnapshot.getString("userId");
                                                     remnantName = queryDocumentSnapshot.getString("title");
                                                     endTime = queryDocumentSnapshot.getLong("endTime").intValue();


                                                    Log.d(TAG, "haha current Time: "+System.currentTimeMillis() / 1000);
                                                    Log.d(TAG,"haha end Time:     "+endTime);

                                                    String stringEndTime = df.format(endTime * 1000);
                                                    String currentTime = df.format(System.currentTimeMillis());

                                                     if(stringEndTime.equals(currentTime)){



                                                         //CHECK IF AUCTION HAS BIDDER
                                                         firebaseFirestore.collection("remnants").document(remnant_id)
                                                                 .collection("bidders")
                                                                 .get()
                                                                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                         if(task.isSuccessful()){

                                                                             if(task.getResult().size() > 0){

                                                                                 Log.d(TAG,"haha naay sud end Time: "+queryDocumentSnapshot.getLong("endTime").intValue());

                                                                                 firebaseFirestore.collection("remnants").document(remnant_id)
                                                                                         .collection("bidders")
                                                                                         .orderBy("bidAmount", Query.Direction.DESCENDING).limit(1)
                                                                                         .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                     @Override
                                                                                     public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                                         if(task.isSuccessful()){

                                                                                             for(QueryDocumentSnapshot queryDocumentSnapshot1: task.getResult()){

                                                                                                 DocumentReference docRef10 = firebaseFirestore.collection("remnants").document(remnant_id);
                                                                                                 docRef10.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                     @Override
                                                                                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                         if(task.isSuccessful()){

                                                                                                             DocumentSnapshot document = task.getResult();

                                                                                                             if(document.getBoolean("isSoldOut") == false){


                                                                                                                 //SEND NOTIFICATION TO WINNER
                                                                                                                 String message = "Congratulations, You are the new owner of "+remnantName;
                                                                                                                 Map<String, Object> notifData = new HashMap<>();
                                                                                                                 notifData.put("receiver_id", queryDocumentSnapshot1.getString("userId"));
                                                                                                                 notifData.put("message",message);
                                                                                                                 notifData.put("imageUrl", "https://i.ibb.co/wYKRnpK/bidItem.png");
                                                                                                                 notifData.put("message2", "");
                                                                                                                 notifData.put("remnants_id", remnant_id);
                                                                                                                 notifData.put("notificationType", "bidWinner");
                                                                                                                 notifData.put("sender_id", seller_id);
                                                                                                                 notifData.put("timeStamp", FieldValue.serverTimestamp());

                                                                                                                 firebaseFirestore.collection("notification")
                                                                                                                         .add(notifData)
                                                                                                                         .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                             @Override
                                                                                                                             public void onSuccess(DocumentReference documentReference) {

                                                                                                                                 Log.d(TAG,"HAHA NOTIFICATION SENT");


                                                                                                                                 //UPDATE TO SOLD AUCTION AND HAS BIDDER
                                                                                                                                 DocumentReference docRef = firebaseFirestore.collection("remnants").document(queryDocumentSnapshot.getId());
                                                                                                                                 docRef.update("isSoldOut", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                     @Override
                                                                                                                                     public void onSuccess(Void aVoid) {


                                                                                                                                         Log.d(TAG,"HAHA SOLD AUCTION SUCCESS");

                                                                                                                                     }
                                                                                                                                 });


                                                                                                                             }
                                                                                                                         });
                                                                                                             }

                                                                                                         }
                                                                                                     }
                                                                                                 });
                                                                                             }
                                                                                         }
                                                                                     }
                                                                                 });

                                                                             }else{

                                                                                 Log.d(TAG,"haha way  sud end Time: "+queryDocumentSnapshot.getLong("endTime").intValue());

                                                                                 //DELETE AUCTION IF NO ONE BID
                                                                                 DocumentReference docRef = firebaseFirestore.collection("remnants").document(queryDocumentSnapshot.getId());
                                                                                 docRef.update("isExpired", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                     @Override
                                                                                     public void onSuccess(Void aVoid) {

                                                                                         Log.d(TAG, "HAHA EXPIRED REMNANT SUCCESSFUL");
                                                                                     }
                                                                                 });
                                                                             }
                                                                         }
                                                                     }
                                                                 });
                                                     }
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }catch (InterruptedException e){}
            }
        };
        t.start();


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

                    Toast.makeText(this, "gamay rang wallet "+wallet, Toast.LENGTH_SHORT).show();
                    showSnackBar();
                }else {

                    Toast.makeText(this, "lapas 25 ang wallet "+wallet, Toast.LENGTH_SHORT).show();
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

        final DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
                docRef.addSnapshotListener((documentSnapshot, e) -> {

                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {

                        wallet = documentSnapshot.getDouble("wallet");
                        Log.d(TAG, "Current data: " + documentSnapshot.getData());
                    } else {
                        Log.d(TAG, "Current data: null");
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
