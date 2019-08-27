package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        textViewId = findViewById(R.id.txtId);
        textViewAmount = findViewById(R.id.txtAmount);
        textViewStatus = findViewById(R.id.txtStatus);

        /*DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();

                    currentAmount = Float.parseFloat(document.getString("wallet"));
                }
            }
        });

        float totalAmount = Float.parseFloat(paymentAmount) + currentAmount;
        String stringTotalAmount = Float.toString(totalAmount);

        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                .update("wallet", stringTotalAmount);*/

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

            textViewId.setText(jsonDetails.getString("id"));
            textViewStatus.setText(jsonDetails.getString("state"));
            textViewAmount.setText("₱ "+paymentAmount);


            DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();

                        float totalAmount = Float.valueOf(document.getString("wallet")) + Float.valueOf(paymentAmount);
                        String stringTotalAmount = String.valueOf(totalAmount);

                        Log.d(TAG, "haha walletAmount: "+ Float.valueOf(document.getString("wallet")));
                        Log.d(TAG, "haha paymentAmount: "+ Float.valueOf(paymentAmount));
                        Log.d(TAG, "haha totalAmount: " +stringTotalAmount);




                        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                                .update("wallet", stringTotalAmount);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
