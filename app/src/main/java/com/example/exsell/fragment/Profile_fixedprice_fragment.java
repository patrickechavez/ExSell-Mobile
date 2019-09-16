package com.example.exsell.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.Profile_FixedPrice_Adapter;
import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Profile_fixedprice_fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Profile_FixedPrice_Adapter profile_fixedPrice_adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.profile_fixedprice_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants")
                .whereEqualTo("type", "fixedPrice")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .whereEqualTo("isBanned", false)
                .whereEqualTo("isDeleted", false)
                .whereEqualTo("isSoldOut", false);



        FirestoreRecyclerOptions<FixedPriceModel> options = new FirestoreRecyclerOptions.Builder<FixedPriceModel>()
            .setQuery(query, FixedPriceModel.class)
                .build();


        profile_fixedPrice_adapter = new Profile_FixedPrice_Adapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.profile_fixedPriceRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(profile_fixedPrice_adapter);



    }

    @Override
    public void onStart() {
        super.onStart();
        profile_fixedPrice_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        profile_fixedPrice_adapter.stopListening();
    }

}
