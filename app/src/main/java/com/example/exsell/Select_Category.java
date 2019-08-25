package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

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

        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("category").orderBy("categoryName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<SelectCategoryModel> options = new FirestoreRecyclerOptions.Builder<SelectCategoryModel>()
                .setQuery(query, SelectCategoryModel.class)
                .build();

        adapter = new SelectCategoryAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_selectCategory);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SelectCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SelectCategoryModel selectCategoryModel = documentSnapshot.toObject(SelectCategoryModel.class);
                String id = documentSnapshot.getId();
                String categoryName = documentSnapshot.get("categoryName").toString();

                //Toast.makeText(getContext(), ""+id, Toast.LENGTH_SHORT).show();
               // Intent intent = new Intent(Select_Category.this, )
            }
        });


    }
}
