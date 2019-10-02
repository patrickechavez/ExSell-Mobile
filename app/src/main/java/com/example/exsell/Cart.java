package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Adapter.CartAdapter;
import com.example.exsell.Models.CartModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;

public class Cart extends AppCompatActivity {

    private static final String TAG = "CART";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private CartAdapter cartAdapter;
    private TextView textViewTotal;
    private ExtendedFloatingActionButton buttonProceedCheckOut ;
    private LinearLayout linearLayoutHiding;
    private String currentUserFirstName, currentUserLastName;
    private Double wallet;
    private RelativeLayout relativeLayoutCheckOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        textViewTotal = findViewById(R.id.cart_total);
        linearLayoutHiding = findViewById(R.id.cart_hiding);
       // relativeLayoutCheckOut = findViewById(R.id.relativelayout_Checkout);
        buttonProceedCheckOut = findViewById(R.id.cart_proceedCheckOut);

        Toolbar toolbar = findViewById(R.id.cart_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CART");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        DocumentSnapshot documentSnapshot = task.getResult();

                        currentUserFirstName = documentSnapshot.getString("firstName");
                        currentUserLastName = documentSnapshot.getString("lastName");
                    }
                }
            });


            setUpRecyclerView();
            hideTheCart();
            //hideTheCartDeleteFromCart();
            getTotal();



        buttonProceedCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();

                            if(documentSnapshot.getDouble("wallet") < 25){

                                Toast.makeText(Cart.this, "You must have at least 25 pesos in your wallet to proceed", Toast.LENGTH_SHORT).show();

                            }else{

                                //ADD TO USER ORDER
                                firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                        .collection("remnants")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                if(task.isSuccessful()){

                                                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                                                        String remnantId = documentSnapshot.getId();
                                                        String buyerId = documentSnapshot.getString("buyerId");
                                                        Integer quantity = documentSnapshot.getLong("quantity").intValue();
                                                        Double subTotal = documentSnapshot.getDouble("subTotal");


                                                        Map<String, Object> orders = new HashMap<>();
                                                        orders.put("remnantId", remnantId);
                                                        orders.put("quantity", quantity);
                                                        orders.put("subTotal", subTotal);
                                                        orders.put("timeStamp", FieldValue.serverTimestamp());

                                                        //ADD TO ORDER USER ORDER
                                                        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                                                                .collection("orders").add(orders)
                                                                .addOnCompleteListener(task1 -> {


                                                                    firebaseFirestore.collection("remnants").document(remnantId)
                                                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                            if(task.isSuccessful()){

                                                                                DocumentSnapshot documentSnapshot1 = task.getResult();

                                                                                //SEND MESSAGE TO SELLER
                                                                                Map<String, Object> sendMessage = new HashMap<>();
                                                                                String message = "Hi, I want to buy your ["+quantity+"] "+documentSnapshot1.getString("title");
                                                                                sendMessage.put("receiver", documentSnapshot1.getString("userId"));
                                                                                sendMessage.put("sender", mAuth.getCurrentUser().getUid());
                                                                                sendMessage.put("message", message);
                                                                                sendMessage.put("time", FieldValue.serverTimestamp());
                                                                                sendMessage.put("type", "text");


                                                                                firebaseFirestore.collection("chat").add(sendMessage)
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                                Log.d(TAG, "HAHA SEND MESSAGE SUCCESSFULLY");
                                                                                            }
                                                                                        });
                                                                                //END SEND MESSAGE TO SELLER



                                                                                //SEND NOTIFICATION TO SELLER
                                                                                String message1 = currentUserFirstName+" "+currentUserLastName+" wants to buy your "+documentSnapshot1.getString("title");
                                                                                String message2 = "Quantity: "+quantity;

                                                                                Map<String, Object> notifications = new HashMap<>();
                                                                                notifications.put("sender_id", mAuth.getCurrentUser().getUid());
                                                                                notifications.put("receiver_id", documentSnapshot1.getString("userId"));
                                                                                notifications.put("remnants_id", remnantId);
                                                                                notifications.put("message", message1);
                                                                                notifications.put("message2", message2);
                                                                                notifications.put("imageUrl", "https://i.ibb.co/XyCn3Gb/buy-icon.png");
                                                                                notifications.put("notificationType", "buyer");
                                                                                notifications.put("timeStamp", FieldValue.serverTimestamp());


                                                                                firebaseFirestore.collection("notification").add(notifications)
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                                Log.d(TAG, "HAHA SUD SA NOTIFICATION");
                                                                                            }
                                                                                        });
                                                                                //END SEND NOTIFICATION TO SELLER

                                                                                //-25 FOR EACH SELLER

                                                                                firebaseFirestore.collection("users").document(documentSnapshot1.getString("userId"))
                                                                                        .update("wallet", FieldValue.increment(-25)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        Log.d(TAG, "haha -25 for each seller");
                                                                                    }
                                                                                });

                                                                                //END -25 FOR EACH SELLER

                                                                                //SEND +25 FROM SELLER TO GENERATE REPORT
                                                                                Map<String, Object> gReport = new HashMap<>();
                                                                                gReport.put("senderUser_id", documentSnapshot1.getString("userId"));
                                                                                gReport.put("transactionFee", 25);
                                                                                gReport.put("type", "Cart Fee");
                                                                                gReport.put("timeStamp", FieldValue.serverTimestamp());

                                                                                firebaseFirestore.collection("generateReport")
                                                                                        .add(gReport).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentReference documentReference) {

                                                                                        Log.d(TAG, "HAHA SUD SA GENERATE REPORT");
                                                                                    }
                                                                                });


                                                                                //MINUS QUANTITY
                                                                                firebaseFirestore.collection("remnants").document(remnantId)
                                                                                        .update("quantity", FieldValue.increment(-quantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        Log.d(TAG, "HAHA MINUS QUANTITY");
                                                                                    }
                                                                                });
                                                                                //END MINUS QUANTITY


                                                                                //DELETE REMNANT CART
                                                                                firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                                                                        .collection("remnants").document(remnantId)
                                                                                        .delete()
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                Log.d(TAG, "HAHA DELETE USER REMNANT CART");
                                                                                            }
                                                                                        });


                                                                                //DELETE USER ID CART
                                                                                firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                                                                        .delete()
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                Log.d(TAG, "hahaho DELETE USER ID CART");
                                                                                            }
                                                                                        });


                                                                                //UPDATE IF NO 0 QUANTITY
                                                                                DocumentReference docRef1 = firebaseFirestore.collection("remnants").document(remnantId);
                                                                                docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task1) {

                                                                                        if(task1.isSuccessful()){

                                                                                            DocumentSnapshot documentSnapshot1 = task1.getResult();
                                                                                            if(documentSnapshot1.getLong("quantity").intValue() == 0){


                                                                                                DocumentReference docref2 = firebaseFirestore.collection("remnants").document(remnantId);
                                                                                                docref2.update("isExpired", true)
                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                Log.d(TAG, "hahaho 0 QUANTITY UPDATE");
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                });
                                                                                //END UPDATE IF NO 0 QUANTITY

                                                                            }
                                                                        }
                                                                    });


                                                                    Intent i = new Intent(Cart.this, Message.class);
                                                                    startActivity(i);
                                                                    Toast.makeText(Cart.this, "Checkout Successful", Toast.LENGTH_SHORT).show();
                                                                });
                                                    }
                                                }
                                            }
                                        });

                                //-25 FOR CURRENT USER
                                firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                                        .update("wallet", FieldValue.increment(-25));

                                //SEND 25 PESOS TO GENERAL REPORT FOR CURRENT USER
                                Map<String, Object> gReport5 = new HashMap<>();
                                gReport5.put("senderUser_id", mAuth.getCurrentUser().getUid());
                                gReport5.put("transactionFee", 25);
                                gReport5.put("type", "Cart Fee");

                                firebaseFirestore.collection("generateReport")
                                        .add(gReport5)
                                        .addOnSuccessListener(documentReference -> Log.d(TAG,"hahaho cartFee from Buyer ID Successful"));

                            }
                        }
                    }
                });

            }
        });

    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                .collection("remnants");


        FirestoreRecyclerOptions<CartModel> options  = new FirestoreRecyclerOptions.Builder<CartModel>()
                .setQuery(query, CartModel.class)
                .build();


        cartAdapter = new CartAdapter(options);


        RecyclerView recyclerView = findViewById(R.id.cart_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

    }

    private void getTotal() {

        DocumentReference docRefTotal = firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid());
            docRefTotal.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {

                        BigDecimal bigDecimal = new BigDecimal(documentSnapshot.getDouble("total"));
                        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);



                        Log.d(TAG, "Current data: " + documentSnapshot.getData());
                        textViewTotal.setText("₱ "+bigDecimal);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
    }
    private void hideTheCart() {

        DocumentReference docRefCart = firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid());
            docRefCart.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot.getDouble("total") > 0){


                        buttonProceedCheckOut.setEnabled(true);
                        linearLayoutHiding.setVisibility(View.VISIBLE);
                        buttonProceedCheckOut.setBackgroundColor(getResources().getColor(colorPrimaryDark));

                    }else {

                        linearLayoutHiding.setVisibility(View.GONE);
                        buttonProceedCheckOut.setEnabled(false);
                        buttonProceedCheckOut.setBackgroundColor(getResources().getColor(buttonDisabled));
                    }
                }
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cartAdapter.stopListening();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
