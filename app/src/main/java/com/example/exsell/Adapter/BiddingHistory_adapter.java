package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.BiddingHistoryModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BiddingHistory_adapter extends FirestoreRecyclerAdapter<BiddingHistoryModel, BiddingHistory_adapter.BiddingHistoryHolder> {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private SimpleDateFormat df = new SimpleDateFormat(" MM/dd/yy h:mm a");

    public BiddingHistory_adapter(@NonNull FirestoreRecyclerOptions<BiddingHistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BiddingHistoryHolder biddingHistoryHolder, int i, @NonNull BiddingHistoryModel biddingHistoryModel) {

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        long millisecond = biddingHistoryModel.getTimeStamp().getTime();
        Date currenDate = new Date(millisecond);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String stringCurrentDate = sdf.format(currenDate).toString();


        firebaseFirestore.collection("remnants").document(biddingHistoryModel.getRemnantId())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        DocumentSnapshot documentSnapshot = task.getResult();
                        List<String> imageUrl = (List<String>) documentSnapshot.get("imageUrl");

                        Picasso.get().load(imageUrl.get(0)).into(biddingHistoryHolder.imageView);
                        biddingHistoryHolder.textViewTitle.setText(documentSnapshot.getString("title"));

                        if(documentSnapshot.getBoolean("isExpired")){
                            biddingHistoryHolder.textViewBidDate.setText("Expired");
                        }else {
                            biddingHistoryHolder.textViewBidDate.setText(df.format(documentSnapshot.getLong("endTime")));
                        }
                    }
                });

        biddingHistoryHolder.textViewDate.setText(stringCurrentDate);
        biddingHistoryHolder.textViewType.setVisibility(View.GONE);
        biddingHistoryHolder.textViewBidPrice.setText("₱ "+biddingHistoryModel.getBidAmount());

    }

    @NonNull
    @Override
    public BiddingHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_orderhistory_custom_layout, parent, false);

        return new BiddingHistoryHolder(v);

    }

    class BiddingHistoryHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewBidDate, textViewBidPrice, textViewType, textViewDate, textViewQty;
        ImageView imageView;

        public BiddingHistoryHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.order_imageView);
            textViewTitle = itemView.findViewById(R.id.order_textViewTitle);
            textViewBidPrice = itemView.findViewById(R.id.order_textViewSubTotal);
            textViewType = itemView.findViewById(R.id.order_textViewQuantity);
            textViewBidDate = itemView.findViewById(R.id.order_textViewOwnerName);
            textViewDate = itemView.findViewById(R.id.order_textViewDate);


        }
    }
}
