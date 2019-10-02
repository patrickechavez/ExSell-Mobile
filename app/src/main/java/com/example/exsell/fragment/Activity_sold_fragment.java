package com.example.exsell.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.SoldAdapter;
import com.example.exsell.Adapter.UnAvailAdapter;
import com.example.exsell.Auction;
import com.example.exsell.FixedPrice;
import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_sold_fragment extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private View view;
    private SoldAdapter soldAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.activity_sold_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();

        return  view;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants")
                .whereEqualTo("isExpired", true)
                .whereEqualTo("isSoldOut", true)
                .whereEqualTo("isBanned", false)
                .whereEqualTo("isDeleted", false)
                .whereEqualTo("isActive", true)
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .orderBy("timeStamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<FixedPriceModel> options = new FirestoreRecyclerOptions.Builder<FixedPriceModel>()
                .setQuery(query, FixedPriceModel.class)
                .build();

        soldAdapter = new SoldAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.sold_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(soldAdapter);


        soldAdapter.setOnItemClickListener(new SoldAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                FixedPriceModel fixedPriceModel = documentSnapshot.toObject(FixedPriceModel.class);

                String id = documentSnapshot.getId();

                if(fixedPriceModel.getType().equals("Fixed Price")){

                    Intent intent = new Intent(getActivity(), FixedPrice.class);
                    intent.putExtra("remnantId", id);
                    startActivity(intent);

                }else{

                    Intent intent1 = new Intent(getActivity(), Auction.class);
                    intent1.putExtra("auctionId", id);
                    startActivity(intent1);
                }

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        soldAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        soldAdapter.stopListening();
    }

}
