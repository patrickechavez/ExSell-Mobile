package com.example.exsell.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.BlogAdapter;
import com.example.exsell.Adapter.FixedPriceAdapter;
import com.example.exsell.Blog;
import com.example.exsell.Blogview;
import com.example.exsell.FixedPrice;
import com.example.exsell.Models.BlogModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class dashboard_stories_fragment extends Fragment {


    private View view;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private BlogAdapter blogAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.dashboard_fragment_stories, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FloatingActionButton fab = view.findViewById(R.id.blog_fab);

        fab.setOnClickListener(view -> {
           Intent i = new Intent(getContext(), Blog.class);
           startActivity(i);
        });

        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("blogs");

        FirestoreRecyclerOptions<BlogModel> options = new FirestoreRecyclerOptions.Builder<BlogModel>()
                .setQuery(query, BlogModel.class)
                .build();

        blogAdapter = new BlogAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.blog_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(blogAdapter);


        blogAdapter.setOnItemClickListener(new BlogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String id = documentSnapshot.getId();
                Intent i = new Intent(getContext(), Blogview.class);
                i.putExtra("blogId", id);
                startActivity(i);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        blogAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        blogAdapter.stopListening();
    }


}
