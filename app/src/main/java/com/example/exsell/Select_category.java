package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.exsell.Adapter.SelectCategoryAdapter;
import com.example.exsell.Models.SelectCategoryModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.List;

public class Select_category extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private SelectCategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category_activity);

        Toolbar toolbar = findViewById(R.id.select_category_appbarlayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Category");
        mAuth = FirebaseAuth.getInstance();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        Query query = FirebaseFirestore.getInstance().collection("category");
        FirestoreRecyclerOptions<SelectCategoryModel> options = new FirestoreRecyclerOptions.Builder<SelectCategoryModel>()
                .setQuery(query, SelectCategoryModel.class)
                .build();

        adapter = new SelectCategoryAdapter(options);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recycler_divider));

        recyclerView = findViewById(R.id.select_category_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
