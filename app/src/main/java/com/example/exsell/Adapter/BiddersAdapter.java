package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.BiddersModel;
import com.example.exsell.Models.NotificationModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BiddersAdapter extends FirestoreRecyclerAdapter<BiddersModel, BiddersAdapter.BiddersHolder> {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public BiddersAdapter(@NonNull FirestoreRecyclerOptions<BiddersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BiddersHolder biddersHolder, int i, @NonNull BiddersModel biddersModel) {

        long millisecond = biddersModel.getTimeStamp().getTime();
        Date currenDate = new Date(millisecond);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String stringCurrentDate = sdf.format(currenDate).toString();

        String user_id = biddersModel.getUserId();

        DocumentReference docRef = firebaseFirestore.collection("users").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                String name = documentSnapshot.getString("firstName");

                Picasso.get().load(documentSnapshot.getString("imageUrl")).into(biddersHolder.imageView);
                biddersHolder.textView.setText(name+" bid ₱ "+biddersModel.getBidAmount());

            }
        });

        biddersHolder.textViewDate.setText(stringCurrentDate);
    }

    @NonNull
    @Override
    public BiddersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bidders_layout,parent,false);

        return new BiddersHolder(v);
    }

    class BiddersHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        TextView textViewDate;


        public BiddersHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bidders_imageView);
            textView = itemView.findViewById(R.id.bidders_textView);
            textViewDate = itemView.findViewById(R.id.bidders_date);
        }
    }
}
