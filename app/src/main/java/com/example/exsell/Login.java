package com.example.exsell;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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


public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText login_email, login_password;
    private Button login_loginbtn, login_forgotypbtn, login_registerbtn;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_loginbtn = findViewById(R.id.login_loginbtn);
        login_registerbtn = findViewById(R.id.login_registerbtn);
        login_forgotypbtn = findViewById(R.id.login_forgotypbtn);

        login_loginbtn.setOnClickListener(this);
        login_registerbtn.setOnClickListener(this);
        login_forgotypbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.login_loginbtn:

                final String email = login_email.getText().toString().trim();
                final String password = login_password.getText().toString().trim();


               mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if(task.isSuccessful()){

                           sendToDashboard();
                       }else{

                           String error = task.getException().getMessage();
                           Toast.makeText(Login.this, ""+error, Toast.LENGTH_SHORT).show();
                       }
                   }
               });

                break;

            case R.id.login_registerbtn:
                Intent register = new Intent(Login.this, Register.class);
                startActivity(register);
                break;

            case R.id.login_forgotypbtn:
                break;
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
