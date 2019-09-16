package com.example.exsell.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exsell.R;
import com.example.exsell.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


public class UserProfile_auction_fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private View view;
    private String seller_id;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        seller_id = sharedPref.getString("seller_id", null);


        // Toast.makeText(getContext(), ""+seller_id, Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.userprofile_auction_fragment, container, false);


        Toast.makeText(getContext(), ""+seller_id, Toast.LENGTH_SHORT).show();


        // PARA CLEAR SA SESSION

       //  sharedPref.edit().clear().apply();

//        if(getArguments() == null){
//
//            Toast.makeText(getContext(), "hahah", Toast.LENGTH_SHORT).show();
//        }else{
//            seller_id = getArguments().getString("seller_id");
//            Toast.makeText(getContext(), "hohoh", Toast.LENGTH_SHORT).show();
//        }
       // setUpRecyclerView();


        return view;
    }


    /* private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants")
                .whereEqualTo("type", "auction")
                .whereEqualTo()
                .orderBy("timeStamp", Query.Direction.DESCENDING);
    }*/



}
