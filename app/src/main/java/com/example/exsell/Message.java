package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.exsell.Adapter.UsersAdapter;
import com.example.exsell.Models.UsersModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Message extends AppCompatActivity {

    private FirebaseAuth mAuth;


    private FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    private UsersAdapter usersAdapter;
    private static final String TAG = "MESSAGE";




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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        setUpRecyclerView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("users");

        FirestoreRecyclerOptions<UsersModel> options = new FirestoreRecyclerOptions.Builder<UsersModel>()
                .setQuery(query, UsersModel.class)
                .build();

        usersAdapter = new UsersAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        RecyclerView recyclerView = findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(usersAdapter);

        usersAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            //UsersModel usersModel = documentSnapshot.toObject(UsersModel.class);
            String id = documentSnapshot.getId();


          //  Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Message.this, Chat.class);
            i.putExtra("receiver_id", id);
            startActivity(i);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        usersAdapter.startListening();


    }
    @Override
    protected void onStop() {
        super.onStop();

        usersAdapter.stopListening();

    }

    private void sendToLogin() {

        Intent login = new Intent(Message.this, Login.class);
        startActivity(login);
        finish();
    }
}
