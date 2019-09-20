package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.exsell.Adapter.SelectCategoryAdapter;
import com.example.exsell.Adapter.SelectSubCategoryAdapter;
import com.example.exsell.Models.SelectCategoryModel;
import com.example.exsell.Models.SelectSubCategoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class Select_Subcategory extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String categoryId;
    private SelectSubCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_subcategory_activity);


        categoryId = getIntent().getStringExtra("categoryId");

        Toolbar toolbar = findViewById(R.id.selectSubCategory_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SELECT SUB CATEGORY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setuprecyclerview();

    }

    private void setuprecyclerview() {

        Query query = firebaseFirestore.collection("category").document(categoryId)
                .collection("sub-category");


        FirestoreRecyclerOptions<SelectSubCategoryModel> options = new FirestoreRecyclerOptions.Builder<SelectSubCategoryModel>()
                .setQuery(query, SelectSubCategoryModel.class)
                .build();

        adapter = new SelectSubCategoryAdapter(options);

        /*RecyclerView recyclerView = findViewById(R.id.selectSubCategory_recycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        RecyclerView recyclerView = findViewById(R.id.selectSubCategory_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SelectCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SelectSubCategoryModel selectSubCategoryModel = documentSnapshot.toObject(SelectSubCategoryModel.class);
                String id = documentSnapshot.getId();
                String subCategoryName = documentSnapshot.get("subCategoryName").toString();


                /*Intent selectSub = new Intent(Select_Category.this, Select_Subcategory.class);
                selectSub.putExtra("categoryId", id);
                startActivity(selectSub);*/

                Intent resultIntent = new Intent();
                resultIntent.putExtra("subCategoryId", id);
                resultIntent.putExtra("subCategoryName", subCategoryName);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
