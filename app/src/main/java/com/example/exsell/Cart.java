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
        relativeLayoutCheckOut = findViewById(R.id.relativelayout_Checkout);

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


        relativeLayoutCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();

                            if(documentSnapshot.getDouble("wallet") < 25){

                                Toast.makeText(Cart.this, "You must have atleast 25 pesos in your wallet to proceed", Toast.LENGTH_SHORT).show();

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
                                                        String imageUrl = documentSnapshot.getString("imageUrl");
                                                        String ownerId = documentSnapshot.getString("owner_id");
                                                        String ownerName = documentSnapshot.getString("ownerName");
                                                        String title = documentSnapshot.getString("title");
                                                        Integer quantity = documentSnapshot.getLong("quantity").intValue();
                                                        Double subTotal = documentSnapshot.getDouble("subTotal");

                                                        Map<String, Object> orders = new HashMap<>();
                                                        orders.put("remnantId", remnantId);
                                                        orders.put("ownerId", ownerId);
                                                        orders.put("quantity", quantity);
                                                        orders.put("subTotal", subTotal);
                                                        orders.put("timeStamp", FieldValue.serverTimestamp());


                                                        //ADD TO ORDER USER ORDER
                                                        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                                                                .collection("orders").add(orders)
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentReference> task) {




                                                                        // SEND MESSAGE TO SELLER
                                                                        Map<String, Object> sendMessage = new HashMap<>();
                                                                        String message3 = "Hi, I want to buy  your ["+quantity+"] "+title;
                                                                        sendMessage.put("receiver", ownerId);
                                                                        sendMessage.put("sender", mAuth.getCurrentUser().getUid());
                                                                        sendMessage.put("message", message3);
                                                                        sendMessage.put("time",FieldValue.serverTimestamp());
                                                                        sendMessage.put("type", "text");

                                                                        firebaseFirestore.collection("chat")
                                                                                .add(sendMessage)
                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentReference documentReference) {

                                                                                        Log.d(TAG, "hahaho SEND MESSAGE SUCCESSFUL");
                                                                                    }
                                                                                });

                                                                        //


                                                                        //SEND NOTIFICATION TO SELLER
                                                                        String message = currentUserFirstName+" "+currentUserLastName+" want to buy your "+title;
                                                                        String message2 = "Quantity: "+quantity;

                                                                        Map<String, Object> notifications = new HashMap<>();
                                                                        notifications.put("sender_id", mAuth.getCurrentUser().getUid());
                                                                        notifications.put("receiver_id", ownerId);
                                                                        notifications.put("remnants_id", remnantId);
                                                                        notifications.put("message", message);
                                                                        notifications.put("message2", message2);
                                                                        notifications.put("imageUrl", "https://i.ibb.co/XyCn3Gb/buy-icon.png");
                                                                        notifications.put("notificationType", "buyer");
                                                                        notifications.put("timeStamp", FieldValue.serverTimestamp());


                                                                        firebaseFirestore.collection("notification").add(notifications)
                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentReference documentReference) {

                                                                                        Log.d(TAG, "hahaho SUD SA NA NOTIFICATION");

                                                                                        //UPDATE QUANTITY
                                                                                        DocumentReference documentReference1 = firebaseFirestore.collection("remnants").document(remnantId);
                                                                                        documentReference1.update("quantity", FieldValue.increment(-quantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                Log.d(TAG, "hahaho MINUS QUANTITY");


                                                                                                //DELETE REMNANT CART
                                                                                                firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                                                                                        .collection("remnants").document(remnantId)
                                                                                                        .delete()
                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                Log.d(TAG, "hahaho DELETE USER REMNANT CART");
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


                                                                                                //UPDATE THE 0 QUANTITY TO ISSOLD
                                                                                                DocumentReference docRef1 = firebaseFirestore.collection("remnants").document(remnantId);
                                                                                                docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                        if(task.isSuccessful()){

                                                                                                            DocumentSnapshot documentSnapshot1 = task.getResult();
                                                                                                            if(documentSnapshot1.getLong("quantity").intValue() == 0){


                                                                                                                DocumentReference docref2 = firebaseFirestore.collection("remnants").document(remnantId);
                                                                                                                docref2.update("isSoldOut", true)
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
                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });

                                                                        Intent i = new Intent(Cart.this, Message.class);
                                                                        startActivity(i);
                                                                        Toast.makeText(Cart.this, "Checkout Successful", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });


                                //-25 FOR CURRENT USER
                                DocumentReference docRefCurrentUser = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
                                docRefCurrentUser.update("wallet", FieldValue.increment(-25));


                                ArrayList<String> str = new ArrayList<String>();

                                //-25 WALLET FOR SELLER
                                firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                        .collection("remnants")
                                        .orderBy("owner_id")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){

                                            for(QueryDocumentSnapshot queryDocumentSnapshot :task.getResult()){

                                                if(str.equals("")) {

                                                    str.add(queryDocumentSnapshot.getString("owner_id"));

                                                }else if(!str.contains(queryDocumentSnapshot.getString("owner_id"))){

                                                    str.add(queryDocumentSnapshot.getString("owner_id"));

                                                }

                                                Log.d(TAG, "OWNER_ID: "+str);

                                            }

                                            for(String owner_ids: str){


                                                Log.d(TAG, "USER_ID"+ owner_ids);
                                                DocumentReference docRef2 = firebaseFirestore.collection("users").document(owner_ids);
                                                docRef2.update("wallet", FieldValue.increment(-25));





                                                //SEND 25 GENERATE REPORT
                                                Map<String, Object> gReport = new HashMap<>();
                                                gReport.put("senderUser_id", owner_ids);
                                                gReport.put("transactionFee", 25);
                                                gReport.put("type", "cartFee");

                                                firebaseFirestore.collection("generateReport")
                                                        .add(gReport)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {

                                                                Log.d(TAG,"hahaho cartFee from Seller ID Successful ID:" + str);
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                                //END OF -25 WALLET USER

                                //SEND 25 PESOS TO GENERAL REPORT FOR CURRENT USER
                                Map<String, Object> gReport = new HashMap<>();
                                gReport.put("senderUser_id", mAuth.getCurrentUser().getUid());
                                gReport.put("transactionFee", 25);
                                gReport.put("type", "cartFee");

                                firebaseFirestore.collection("generateReport")
                                        .add(gReport)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                Log.d(TAG,"hahaho cartFee from Buyer ID Successful");
                                            }
                                        });

                                // END OF SEND 25 PESOS TO GENERAL REPORT FOR CURRENT USER

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
                        textViewTotal.setText("TOTAL:  ₱ "+bigDecimal);
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


                        relativeLayoutCheckOut.setEnabled(true);
                        linearLayoutHiding.setVisibility(View.VISIBLE);
                        relativeLayoutCheckOut.setBackgroundColor(getResources().getColor(colorPrimaryDark));

                    }else {

                        linearLayoutHiding.setVisibility(View.GONE);
                        relativeLayoutCheckOut.setEnabled(false);
                        relativeLayoutCheckOut.setBackgroundColor(getResources().getColor(buttonDisabled));
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
