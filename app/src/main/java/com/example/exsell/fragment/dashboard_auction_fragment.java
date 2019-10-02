package com.example.exsell.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exsell.Adapter.AuctionAdapter;
import com.example.exsell.Adapter.FixedPriceAdapter;
import com.example.exsell.Auction;
import com.example.exsell.FixedPrice;
import com.example.exsell.Models.AuctionModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class dashboard_auction_fragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private AuctionAdapter auctionAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dashboard_fragment_auction, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();
        return  view;
    }


    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants")
                .whereEqualTo("type", "Auction")
                .whereEqualTo("isDeleted", false)
                .whereEqualTo("isSoldOut", false)
                .whereEqualTo("isBanned", false)
                .whereEqualTo("isExpired", false)
                .orderBy("featuredDuration", Query.Direction.DESCENDING)
                .orderBy("timeStamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<AuctionModel> options = new FirestoreRecyclerOptions.Builder<AuctionModel>()
                .setQuery(query, AuctionModel.class)
                .build();

        auctionAdapter = new AuctionAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.auctionRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(auctionAdapter);

        auctionAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            // FixedPriceModel fixedPriceModel = documentSnapshot.toObject(FixedPriceModel.class);
            String id = documentSnapshot.getId();
            // Toast.makeText(getContext(), ""+id, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), Auction.class);
                i.putExtra("auctionId", id);
            startActivity(i);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auctionAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        auctionAdapter.stopListening();
    }
}
