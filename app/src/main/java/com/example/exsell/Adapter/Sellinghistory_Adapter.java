package com.example.exsell.Adapter;

import android.annotation.SuppressLint;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.SellingHistoryModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Sellinghistory_Adapter extends FirestoreRecyclerAdapter<SellingHistoryModel, Sellinghistory_Adapter.SellingHistoryHolder> {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private SimpleDateFormat df = new SimpleDateFormat(" MM/dd/yy h:mm a");


    public Sellinghistory_Adapter(@NonNull FirestoreRecyclerOptions<SellingHistoryModel> options) {
        super(options);


    }


    @Override
    protected void onBindViewHolder(@NonNull SellingHistoryHolder sellingHistoryHolder, int i, @NonNull SellingHistoryModel sellingHistoryModel) {

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        long millisecond = sellingHistoryModel.getTimeStamp().getTime();
        Date currenDate = new Date(millisecond);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String stringCurrentDate = sdf.format(currenDate).toString();

        List<String> imageUrl = sellingHistoryModel.getImageUrl();

        Picasso.get().load(sellingHistoryModel.getImageUrl().get(0)).into(sellingHistoryHolder.imageView);
        sellingHistoryHolder.textViewTitle.setText(sellingHistoryModel.getTitle());
        sellingHistoryHolder.textViewDate.setText(stringCurrentDate);


        if(sellingHistoryModel.getType().equals("Fixed Price")){

            if(sellingHistoryModel.getQuantity() == 1) {
                sellingHistoryHolder.textViewQuantity.setText(sellingHistoryModel.getQuantity() + " pc");
            }else{
                sellingHistoryHolder.textViewQuantity.setText(sellingHistoryModel.getQuantity() + " pcs");
            }
            sellingHistoryHolder.textViewPrice.setText("₱ "+sellingHistoryModel.getPrice());
            sellingHistoryHolder.textViewType.setText(sellingHistoryModel.getType());

        }else{

            sellingHistoryHolder.textViewType.setText(sellingHistoryModel.getType());

            DocumentSnapshot snapshot = getSnapshots().getSnapshot(sellingHistoryHolder.getAdapterPosition());
            String id = snapshot.getId();

            firebaseFirestore.collection("remnants").document(id).collection("bidders")
                    .orderBy("bidAmount", Query.Direction.DESCENDING).limit(1)
                    .get().addOnCompleteListener(task -> {

                        if(task.getResult().size() > 0){

                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                sellingHistoryHolder.textViewPrice.setText("₱ "+queryDocumentSnapshot.getDouble("bidAmount"));
                            }
                        }else{
                            sellingHistoryHolder.textViewPrice.setText("₱ "+sellingHistoryModel.getPrice());
                        }


                    });

            firebaseFirestore.collection("remnants").document(id)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){
                        DocumentSnapshot document  = task.getResult();
                        if(document.exists()){

                            if(document.getBoolean("isExpired")){
                                sellingHistoryHolder.textViewQuantity.setText("Expired");
                            }else{
                                sellingHistoryHolder.textViewQuantity.setText(df.format(document.getLong("endTime")));
                            }
                        }
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public SellingHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sellinghistory_custom_layout, parent, false);

        return new SellingHistoryHolder(v);
    }

    class SellingHistoryHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewQuantity, textViewPrice, textViewDate, textViewType;
        ImageView imageView;

        public SellingHistoryHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.seller_imageView);
            textViewTitle = itemView.findViewById(R.id.seller_textViewTitle);
            textViewQuantity = itemView.findViewById(R.id.seller_textViewQuantity);
            textViewPrice = itemView.findViewById(R.id.seller_textViewPrice);
            textViewType = itemView.findViewById(R.id.seller_textViewType);
            textViewDate = itemView.findViewById(R.id.seller_textViewDate);
        }
    }


}
