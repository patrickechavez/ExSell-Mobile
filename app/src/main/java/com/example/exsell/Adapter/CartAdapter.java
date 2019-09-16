package com.example.exsell.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.CartModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class CartAdapter extends FirestoreRecyclerAdapter<CartModel, CartAdapter.CartHolder> {

    private static final String TAG = "CART ADAPTER";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public CartAdapter(@NonNull FirestoreRecyclerOptions<CartModel> options) {
        super(options);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore  = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull CartHolder cartHolder,int i,  @NonNull CartModel cartModel) {

        cartHolder.textViewOwner.setText(cartModel.getOwnerName());
        cartHolder.textViewRemnantName.setText(cartModel.getTitle());
        cartHolder.textViewViewPrice.setText("₱ "+ cartModel.getPrice());
        Picasso.get().load(cartModel.getImageUrl()).into(cartHolder.imageViewRemnant);
        cartHolder.textViewQuantity.setText(String.valueOf(cartModel.getQuantity()));

        double price = cartModel.getPrice();
        BigDecimal bigDecimal = new BigDecimal(price);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        cartHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                        .collection("remnants").document(cartModel.getRemnantId())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                .update("total", FieldValue.increment(-cartModel.getSubTotal()));
                    }
                });
            }
        });

        //INCREMENT
        cartHolder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Integer increment = Integer.parseInt(cartHolder.textViewQuantity.getText().toString());
                    if(increment < cartModel.getMaxQuantity()) {

                        increment++;
                        cartHolder.textViewQuantity.setText(String.valueOf(increment));

                        cartHolder.textViewQuantity.setText(String.valueOf(increment));
                        firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                .update("total", FieldValue.increment(cartModel.getPrice()));


                        DocumentReference docRef = firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                .collection("remnants").document(cartModel.getRemnantId());

                        docRef.update(
                                "quantity", FieldValue.increment(1),
                    "subTotal", FieldValue.increment(cartModel.getPrice()));
                    }
            }
        });

        //DECREMENT
        cartHolder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer decrement = Integer.parseInt(cartHolder.textViewQuantity.getText().toString());
                if(decrement > 1) {

                    decrement--;
                    cartHolder.textViewQuantity.setText(String.valueOf(decrement));

                    cartHolder.textViewQuantity.setText(String.valueOf(decrement));
                    firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                            .update("total", FieldValue.increment(-cartModel.getPrice()));


                    DocumentReference docRef = firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                            .collection("remnants").document(cartModel.getRemnantId());

                    docRef.update(
                            "quantity", FieldValue.increment(-1),
                            "subTotal", FieldValue.increment(-cartModel.getPrice()));
                }

            }
        });

    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_custom_layout, parent, false);

        return new CartHolder(view);
    }



    class CartHolder extends RecyclerView.ViewHolder{

        TextView textViewOwner, textViewViewPrice, textViewRemnantName;
        ImageView imageViewRemnant, increment, decrement, imageViewDelete;
        TextView textViewQuantity;


        public CartHolder(@NonNull View itemView) {
            super(itemView);

            textViewOwner = itemView.findViewById(R.id.cart_textViewName);
            textViewRemnantName = itemView.findViewById(R.id.cart_textViewTitle);
            textViewViewPrice = itemView.findViewById(R.id.cart_price);
            imageViewRemnant = itemView.findViewById(R.id.cart_imageView);
            textViewQuantity = itemView.findViewById(R.id.cart_quantity);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);
            imageViewDelete = itemView.findViewById(R.id.cart_deleteButton);
        }
    }

}
