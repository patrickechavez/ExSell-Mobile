package com.example.exsell;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.Adapter.UsersAdapter;
import com.example.exsell.Models.MessageModel;
import com.example.exsell.Models.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class message_chats_fragment extends Fragment {


    private View view;
    private UsersAdapter usersAdapter;
    private List<UsersModel> usersModelList;
    private FirebaseAuth mAuth;
    private List<String> userList;
    private FirebaseUser fuser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.message_chats_fragment, container, false);



        return view;
    }




}
