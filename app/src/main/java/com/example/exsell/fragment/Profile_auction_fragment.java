package com.example.exsell.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.Profile_Auction_Adapter;
import com.example.exsell.Adapter.Profile_FixedPrice_Adapter;
import com.example.exsell.Models.AuctionModel;
import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Profile_auction_fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private View view;
    private Profile_Auction_Adapter profile_auction_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.profile_auction_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();

        return  view;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants")
                .whereEqualTo("type", "auction")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .orderBy("timeStamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AuctionModel> options = new FirestoreRecyclerOptions.Builder<AuctionModel>()
                .setQuery(query, AuctionModel.class)
                .build();


        profile_auction_adapter = new Profile_Auction_Adapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.profile_auctionRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(profile_auction_adapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        profile_auction_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        profile_auction_adapter.stopListening();
    }

}
