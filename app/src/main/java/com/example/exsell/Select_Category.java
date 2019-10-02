package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.exsell.Adapter.SelectCategoryAdapter;
import com.example.exsell.Models.SelectCategoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Select_Category extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private SelectCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category_activity);

        Toolbar toolbar = findViewById(R.id.selectCategory_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SELECT CATEGORY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("category").orderBy("categoryName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<SelectCategoryModel> options = new FirestoreRecyclerOptions.Builder<SelectCategoryModel>()
                .setQuery(query, SelectCategoryModel.class)
                .build();

        adapter = new SelectCategoryAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.selectCategory_recycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SelectCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SelectCategoryModel selectCategoryModel = documentSnapshot.toObject(SelectCategoryModel.class);
                String id = documentSnapshot.getId();
                String subCategoryName = documentSnapshot.get("categoryName").toString();


              //  Toast.makeText(Select_Category.this, ""+id, Toast.LENGTH_SHORT).show();
                /*Intent selectSub = new Intent(Select_Category.this, Select_Subcategory.class);
                selectSub.putExtra("categoryId", id);
                selectSub.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(selectSub);*/

                Intent resultIntent = new Intent();
                resultIntent.putExtra("subCategoryId", id);
                resultIntent.putExtra("subCategoryName", subCategoryName);

                setResult(RESULT_OK, resultIntent);
                finish();
               // List_remnants2_fragment.
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
