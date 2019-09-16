package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.exsell.Adapter.BiddersAdapter;
import com.example.exsell.Adapter.NotificationAdapter;
import com.example.exsell.Models.BiddersModel;
import com.example.exsell.Models.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Bidders extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private BiddersAdapter biddersAdapter;
    private String auctionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bidders_activity);

        Toolbar toolbar = findViewById(R.id.bidders_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BIDDERS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        auctionId = getIntent().getStringExtra("auctionId");
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        setUpRecyclerView();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants")
                .document(auctionId).collection("bidders")
                .orderBy("bidAmount", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<BiddersModel> options = new FirestoreRecyclerOptions.Builder<BiddersModel>()
                .setQuery(query, BiddersModel.class)
                .build();

        biddersAdapter = new BiddersAdapter(options);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        RecyclerView recyclerView = findViewById(R.id.bidders_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(biddersAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        biddersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        biddersAdapter.stopListening();
    }
}
