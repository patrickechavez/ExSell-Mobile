package com.example.exsell;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.SelectCategoryAdapter;
import com.example.exsell.Models.SelectCategoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class List_remnants_category_fragment extends Fragment {

    private View view;
    private FirebaseFirestore firebaseFirestore;
    private SelectCategoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.list_remnants_category_fragment, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();



        
        setUpRecyclerView();

        return view;

    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("category").orderBy("categoryName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<SelectCategoryModel> options = new FirestoreRecyclerOptions.Builder<SelectCategoryModel>()
                .setQuery(query, SelectCategoryModel.class)
                .build();

        adapter = new SelectCategoryAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_selectCategory);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
