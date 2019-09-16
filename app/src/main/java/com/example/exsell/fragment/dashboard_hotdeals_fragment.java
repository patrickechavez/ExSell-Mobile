package com.example.exsell.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.FixedPriceAdapter;
import com.example.exsell.FixedPrice;
import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;



public class dashboard_hotdeals_fragment extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private View view;
    private FixedPriceAdapter fixedPriceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view =  inflater.inflate(R.layout.dashboard_fragment_hotdeals, container, false);

       mAuth = FirebaseAuth.getInstance();
       firebaseFirestore = FirebaseFirestore.getInstance();

       setUpRecyclerView();

       return view;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants").whereEqualTo("isFeatured", true)
                .orderBy("featuredDay", Query.Direction.ASCENDING)
                .orderBy("featuredDuration", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<FixedPriceModel> options = new FirestoreRecyclerOptions.Builder<FixedPriceModel>()
                .setQuery(query, FixedPriceModel.class)
                .build();

        fixedPriceAdapter= new FixedPriceAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.featured_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(fixedPriceAdapter);


        fixedPriceAdapter.setOnItemClickListener(new FixedPriceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                // FixedPriceModel fixedPriceModel = documentSnapshot.toObject(FixedPriceModel.class);
                String id = documentSnapshot.getId();
                // Toast.makeText(getContext(), ""+id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), FixedPrice.class);
                i.putExtra("remnantId", id);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        fixedPriceAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fixedPriceAdapter.stopListening();
    }


}
