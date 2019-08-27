package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;


public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFname, editTextLname, editTextEmail, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        editTextFname = findViewById(R.id.register_fname);
        editTextLname = findViewById(R.id.register_lname);
        editTextEmail = findViewById(R.id.register_email);
        editTextPassword = findViewById(R.id.register_password);
        buttonRegister = findViewById(R.id.register_registerbtn);
        buttonLogin = findViewById(R.id.register_loginbtn);


        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.register_registerbtn:

                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String fname = editTextFname.getText().toString().trim();
                String lname = editTextLname.getText().toString().trim();

                register_user(email, password, fname, lname);

                break;

            case R.id.register_loginbtn:

                Intent login = new Intent(Register.this, Login.class);
                startActivity(login);
                break;
        }
    }

    private void register_user(String email, String password, final String fname, final String lname) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {




                if(task.isSuccessful()){

                    String user_id = mAuth.getCurrentUser().getUid();
                    //mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("firstName", fname);
                    userMap.put("lastName", lname);
                    userMap.put("imageUrl", "default");
                    userMap.put("wallet", "0.00");
                    userMap.put("type", "user");

                    firebaseFirestore.collection("users").document(user_id).set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });




                    sendToDashboard();

                }else{

                    String error = task.getException().getMessage();
                    Toast.makeText(Register.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void sendToDashboard() {

        Intent dashboard = new Intent(Register.this, Dashboard.class);
        startActivity(dashboard);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            sendToDashboard();
        }
    }

}
