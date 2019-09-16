package com.example.exsell;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LOGIN";
    private EditText login_email, login_password;
    private Button login_loginbtn, login_forgotypbtn, login_registerbtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_loginbtn = findViewById(R.id.login_loginbtn);
        login_registerbtn = findViewById(R.id.login_registerbtn);

        login_loginbtn.setOnClickListener(this);
        login_registerbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.login_loginbtn:

                final String email = login_email.getText().toString().trim();
                final String password = login_password.getText().toString().trim();

                if(email.isEmpty()){

                    login_email.setError("Email must not be empty");

                }else if(password.isEmpty()){

                    login_password.setError("Password must not be empty");
                }else {



                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                firebaseFirestore.collection("users")
                                        .whereEqualTo(FieldPath.documentId(), mAuth.getCurrentUser().getUid())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isSuccessful()){

                                            for(QueryDocumentSnapshot document: task.getResult()){

                                                if(document.getBoolean("isBanned")){


                                                    mAuth.signOut();
                                                    Intent toLogin = new Intent(Login.this, Login.class);
                                                    startActivity(toLogin);

                                                    Toast.makeText(Login.this, "Your account has been banned because of numerous reports from different users regarding your behaviour and recent activity. You may no longer use nor access your ExSell.", Toast.LENGTH_SHORT).show();
                                                }else{

                                                    sendToDashboard();
                                                }
                                            }
                                        }
                                    }
                                });



                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(Login.this, "" + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;

            case R.id.login_registerbtn:
                Intent register = new Intent(Login.this, Register.class);
                startActivity(register);
                break;

            /*case R.id.login_forgotypbtn:
                break;*/
        }

    }
    private void sendToDashboard() {

        Intent dashboard = new Intent(Login.this, Dashboard.class);
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
