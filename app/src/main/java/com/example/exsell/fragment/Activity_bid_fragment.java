package com.example.exsell.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.BiddingHistory_adapter;
import com.example.exsell.Models.BiddingHistoryModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_bid_fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private View view;
    private BiddingHistory_adapter biddingHistory_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.activity_bid_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();

        return  view;
    }

    private void setUpRecyclerView() {


        Query query = firebaseFirestore.collection("bidders")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .orderBy("timeStamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<BiddingHistoryModel> options = new FirestoreRecyclerOptions.Builder<BiddingHistoryModel>()
                .setQuery(query, BiddingHistoryModel.class)
                .build();

        biddingHistory_adapter = new BiddingHistory_adapter(options);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());

        RecyclerView recyclerView = view.findViewById(R.id.biddingHistory_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(biddingHistory_adapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        biddingHistory_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        biddingHistory_adapter.stopListening();
    }
}
