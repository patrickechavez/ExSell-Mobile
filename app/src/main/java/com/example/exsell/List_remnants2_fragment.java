package com.example.exsell;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class List_remnants2_fragment extends Fragment {

    private static final String TAG = "remnants2";
    private RecyclerView recyclerView;


    private LinearLayoutManager linearLayoutManager;
    private String user_id;

    //RADIO BUTTON
    private RadioGroup radioGroupFormat;;
    private LinearLayout linearLayoutprice, linearLayoutauction;
    private String duration;
    private EditText editTextSelectCategory, editTextMeetup;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Button listitBtn;


    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.list_remnants2_fragment, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        linearLayoutprice = view.findViewById(R.id.linearlayout_price);
        linearLayoutauction = view.findViewById(R.id.linearlayout_auction);

        listitBtn = view.findViewById(R.id.listit_btn);
        listitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Message.class);
                startActivity(i);
            }
        });

        editTextSelectCategory = view.findViewById(R.id.lm_select_category);
        editTextSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment lm_fragmentSelectCategory  = new List_remnants_category_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.list_remnants_container, lm_fragmentSelectCategory);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });



        radioGroupFormat = view.findViewById(R.id.radigroup_format);
        radioGroupFormat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = view.findViewById(checkedId);

                if(rb.getText().toString().trim().equals("Fixed Price")){

                    linearLayoutauction.setVisibility(View.GONE);
                    linearLayoutprice.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutauction.setVisibility(View.VISIBLE);
                    linearLayoutprice.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }






}
