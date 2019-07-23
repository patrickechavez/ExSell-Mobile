package com.example.exsell;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exsell.Adapter.UsersAdapter;
import com.example.exsell.Models.UsersModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.List;


public class message_users_fragment extends Fragment{


    private FirebaseAuth mAuth;
    private View view;
    private UsersAdapter usersAdapter;
    private RecyclerView recyclerView;
    private List<UsersModel> usersModelList;
    private static final String TAG = "MESSAGE_USER_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.message_users_fragment, container, false);

         mAuth = FirebaseAuth.getInstance();



        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("users");

       /* FirebaseRecyclerOptions<UsersModel> options = new FirebaseRecyclerOptions.Builder<UsersModel>()
                .setQuery(query, UsersModel.class)
                .build();*/

       FirestoreRecyclerOptions<UsersModel> options = new FirestoreRecyclerOptions.Builder<UsersModel>()
               .setQuery(query, UsersModel.class)
               .build();

       // usersAdapter = new UsersAdapter(options);
        usersAdapter = new UsersAdapter(options);

        recyclerView = view.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(usersAdapter);

        usersAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                UsersModel usersModel = documentSnapshot.toObject(UsersModel.class);
                String id = documentSnapshot.getId();

                Intent i = new Intent(getContext(), Chat.class);
                i.putExtra("receiver_id", id);
                startActivity(i);
            }
        });
        /*usersAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot dataSnapshot, int position) {
                UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                String id = dataSnapshot.getKey();
               // String name = usersModel.getFirstName();

                Intent i = new Intent(getContext(), Chat.class);
                i.putExtra("receiver_id", id);
                startActivity(i)
                ;
               // Toast.makeText(getActivity(), "ID: " + name, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();

        usersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        usersAdapter.stopListening();
    }




}
