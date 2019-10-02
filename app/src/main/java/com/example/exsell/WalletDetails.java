package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class WalletDetails extends AppCompatActivity {

    private static final String TAG = "WALLET DETAILS";
    private TextView textViewId, textViewAmount, textViewStatus;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private float currentAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_details_activity);

        Toolbar toolbar = findViewById(R.id.walletDetails_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        textViewId = findViewById(R.id.txtId);
        textViewAmount = findViewById(R.id.txtAmount);
        textViewStatus = findViewById(R.id.txtStatus);


        Intent intent = getIntent();

        try {

            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonDetails.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));


        }catch (JSONException e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) {

        try {

            double doublePaymentAmount = Double.parseDouble(paymentAmount);

            textViewId.setText(jsonDetails.getString("id"));
            textViewStatus.setText(jsonDetails.getString("state"));
            textViewAmount.setText("₱ " +doublePaymentAmount);


            DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();

                        double totalAmount = document.getDouble("wallet") + doublePaymentAmount;


                        Log.d(TAG, "haha walletAmount: "+ document.getDouble("wallet"));
                        Log.d(TAG, "haha paymentAmount: "+ document.getDouble(paymentAmount));
                        Log.d(TAG, "haha totalAmount: " + totalAmount);



                        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                                .update("wallet", totalAmount);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
