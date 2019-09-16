package com.example.exsell.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.OrderHistory_adapter;
import com.example.exsell.Adapter.Sellinghistory_Adapter;
import com.example.exsell.Models.OrderHistoryModel;
import com.example.exsell.Models.SellingHistoryModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_selling_fragment extends Fragment {

    private View view;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Sellinghistory_Adapter sellinghistory_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.activity_selling_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setUpRecyclerView();

        return  view;
    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .orderBy("timeStamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<SellingHistoryModel> options = new FirestoreRecyclerOptions.Builder<SellingHistoryModel>()
                .setQuery(query, SellingHistoryModel.class)
                .build();

        sellinghistory_adapter = new Sellinghistory_Adapter(options);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());

        RecyclerView recyclerView = view.findViewById(R.id.orderseller_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(sellinghistory_adapter);


    }

    @Override
    public void onStart() {
        super.onStart();

        sellinghistory_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        sellinghistory_adapter.stopListening();
    }

}
