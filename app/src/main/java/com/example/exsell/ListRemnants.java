package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class ListRemnants extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_remnants_activity);

        Toolbar toolbar = findViewById(R.id.list_remnants_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LIST REMNANTS");



        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.list_remnants_container, new list_remnants1_fragment());
        fragmentTransaction.commit();

    }
}
