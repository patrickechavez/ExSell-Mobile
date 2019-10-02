package com.example.exsell.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.AuctionModel;
import com.example.exsell.R;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public class AuctionAdapter extends FirestoreRecyclerAdapter<AuctionModel, AuctionAdapter.AuctionHolder> {

    private static final String TAG = "AUCTION ADAPTER";
    private FixedPriceAdapter.OnItemClickListener listener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    public AuctionAdapter(@NonNull FirestoreRecyclerOptions<AuctionModel> options) {
        super(options);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);

        
    }

    @Override
    protected void onBindViewHolder(@NonNull AuctionHolder auctionHolder, int i, @NonNull AuctionModel auctionModel) {

        DocumentSnapshot snapshot = getSnapshots().getSnapshot(auctionHolder.getAdapterPosition());
        String id = snapshot.getId();


        firebaseFirestore.collection("bidders")
                .whereEqualTo("remnantId", id)
                .orderBy("bidAmount", Query.Direction.DESCENDING)
                .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().size() > 0){

                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                            BigDecimal bigDecimal1 = new BigDecimal(documentSnapshot.getDouble("bidAmount"));
                            bigDecimal1 = bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                            auctionHolder.textViewBid.setText("₱ "+bigDecimal1);
                        }
                    }else{


                        BigDecimal bigDecimal = new BigDecimal(auctionModel.getPrice());
                        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        auctionHolder.textViewBid.setText("₱ "+bigDecimal);
                    }
                }
            }
        });


        Picasso.get().load(auctionModel.getImageUrl().get(0)).into(auctionHolder.imageURL);
        auctionHolder.textViewTitle.setText(auctionModel.getTitle());
       // auctionHolder.textViewBid.setText("₱ "+auctionModel.getPrice());


       //

    }
    @NonNull
    @Override
    public AuctionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auction_custom_layout,parent,false);

        return new AuctionHolder(view);
    }

    class AuctionHolder extends RecyclerView.ViewHolder{

        ImageView imageURL;
        TextView textViewTitle, textViewBid;

        public AuctionHolder(@NonNull View itemView) {
            super(itemView);

            imageURL = itemView.findViewById(R.id.auction_customimageViews);
            textViewTitle = itemView.findViewById(R.id.auction_customTitle);
            textViewBid = itemView.findViewById(R.id.auction_customPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {

                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setOnItemClickListener(FixedPriceAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
