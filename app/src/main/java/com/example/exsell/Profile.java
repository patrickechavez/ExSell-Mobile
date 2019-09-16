package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exsell.fragment.Profile_tabPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textViewFirstName, textViewLastName, textViewGetCoins;
    private CircleImageView circleImageViewProfile;
    private ImageView imageViewSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = findViewById(R.id.profile_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        circleImageViewProfile = findViewById(R.id.profile_circleImageView);
        textViewFirstName = findViewById(R.id.profile_textViewFirstName);
        textViewLastName = findViewById(R.id.profile_textViewLastName);
        textViewGetCoins = findViewById(R.id.profile_textViewGetCoins);

        //FETCH THE CURRENT USER
        fetchCurrentUser();
        imageViewSettings = findViewById(R.id.settings);

        imageViewSettings.setOnClickListener(this);
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


    private void fetchCurrentUser() {

        DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    textViewFirstName.setText(documentSnapshot.getString("firstName"));
                    textViewLastName.setText(documentSnapshot.getString("lastName"));
                    Picasso.get().load(documentSnapshot.getString("imageUrl")).into(circleImageViewProfile);

                }
            }
        });

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.profile_textViewGetCoins:

                Intent getCoins = new Intent(Profile.this, Wallet.class);
                startActivity(getCoins);
                break;

            case R.id.settings:

                Intent toAccount = new Intent(Profile.this, Account.class);
                startActivity(toAccount);

                break;
        }




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
