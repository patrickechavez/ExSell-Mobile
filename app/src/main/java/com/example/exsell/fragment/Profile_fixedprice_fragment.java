package com.example.exsell.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exsell.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_fixedprice_fragment extends Fragment {


    public Profile_fixedprice_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_fixedprice_fragment, container, false);
    }

}