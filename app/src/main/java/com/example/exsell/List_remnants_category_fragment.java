package com.example.exsell;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exsell.Adapter.SelectCategoryAdapter;
import com.example.exsell.Models.SelectCategoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class List_remnants_category_fragment extends Fragment {

    private View view;
    private FirebaseFirestore firebaseFirestore;
    private SelectCategoryAdapter adapter;
    private String price, quantity, title, description, backStory, bounceBack;
    private List<Uri> listOfPic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.list_remnants_category_fragment, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        listOfPic = getArguments().getParcelableArrayList("listOfPic");
        Log.d("GF","IMAGEURL: "+listOfPic);
        title = getArguments().getString("title");
        description = getArguments().getString("description");
        backStory = getArguments().getString("backStory");
        bounceBack = getArguments().getString("bounceBack");
        price = getArguments().getString("price");
        quantity = getArguments().getString("quantity");

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

        adapter.setOnItemClickListener(new SelectCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SelectCategoryModel selectCategoryModel = documentSnapshot.toObject(SelectCategoryModel.class);
                String id = documentSnapshot.getId();
                String categoryName = documentSnapshot.get("categoryName").toString();


                Bundle bundle = new Bundle();
                /*bundle.putParcelableArrayList("listOfPic", (ArrayList<? extends Parcelable>) listOfPic);
                bundle.putString("title", title);
                bundle.putString("description", description);
                bundle.putString("backStory", backStory);
                bundle.putString("bounceBack", bounceBack);

                bundle.putString("id", id);*/
                bundle.putString("categoryName", categoryName);
                /*bundle.putString("price", price);
                bundle.putString("quantity",quantity);
                bundle.putString("meetup",meetup);*/

               /* Fragment lm_fragment2  = new List_remnants2_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                lm_fragment2.setArguments(bundle);
                transaction.replace(R.id.list_remnants_container, lm_fragment2);
                transaction.addToBackStack(null);
                transaction.commit();*/

                FragmentManager manager = getActivity().getSupportFragmentManager();
                List_remnants2_fragment lm_fragment2 = new List_remnants2_fragment();
                lm_fragment2.setArguments(bundle);
                manager.popBackStack();



            }
        });
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
