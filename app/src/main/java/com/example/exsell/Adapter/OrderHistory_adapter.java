package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.OrderHistoryModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderHistory_adapter extends FirestoreRecyclerAdapter<OrderHistoryModel, OrderHistory_adapter.OrderHistoryHolder> {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    public OrderHistory_adapter(@NonNull FirestoreRecyclerOptions<OrderHistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderHistoryHolder orderHistoryHolder, int i, @NonNull OrderHistoryModel orderHistoryModel) {

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();




        //FETCH ORDERS
        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("orders")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().size() > 0){

                        long millisecond = orderHistoryModel.getTimeStamp().getTime();
                        Date currenDate = new Date(millisecond);
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                        String stringCurrentDate = sdf.format(currenDate).toString();

                        String remnantId = orderHistoryModel.getRemnantId();
                        String ownerId = orderHistoryModel.getOwnerId();

                        //GET REMNANTS
                        DocumentReference docRef = firebaseFirestore.collection("remnants").document(remnantId);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();

                                    orderHistoryHolder.textViewTitle.setText(document.getString("title"));
                                    List<String> imageUrl = (List<String>) document.get("imageUrl");
                                    Picasso.get().load(imageUrl.get(0)).into(orderHistoryHolder.imageViewOrder);
                                    //orderHistoryHolder.textViewOwnerName.(document.getS);
                                }

                            }
                        });

                        //GET USER INFO
                        DocumentReference docRef1 = firebaseFirestore.collection("users").document(ownerId);
                        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();

                                    String fname = documentSnapshot.getString("firstName");
                                    String lname = documentSnapshot.getString("lastName");
                                    orderHistoryHolder.textViewOwnerName.setText(fname + " " + lname);
                                }
                            }
                        });


                        orderHistoryHolder.textViewQuantity.setText(orderHistoryModel.getQuantity()+" pcs");
                        orderHistoryHolder.textViewSubTotal.setText("₱ "+orderHistoryModel.getSubTotal());
                        orderHistoryHolder.textViewDate.setText(stringCurrentDate);
                    }
                }
            }
        });

        //END OF FETCH ORDERS


    }

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_orderhistory_custom_layout, parent,false);
        return new OrderHistoryHolder(v);
    }



    class OrderHistoryHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewOwnerName, textViewSubTotal, textViewQuantity, textViewDate;
        ImageView imageViewOrder;

        public OrderHistoryHolder(@NonNull View itemView) {
            super(itemView);


            textViewTitle = itemView.findViewById(R.id.order_textViewTitle);
            textViewOwnerName = itemView.findViewById(R.id.order_textViewOwnerName);
            textViewSubTotal = itemView.findViewById(R.id.order_textViewSubTotal);
            textViewQuantity = itemView.findViewById(R.id.order_textViewQuantity);
            imageViewOrder = itemView.findViewById(R.id.order_imageView);
            textViewDate = itemView.findViewById(R.id.order_textViewDate);


            }
        }
}



