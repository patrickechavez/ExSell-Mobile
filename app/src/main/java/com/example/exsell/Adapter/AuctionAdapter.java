package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.AuctionModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class AuctionAdapter extends FirestoreRecyclerAdapter<AuctionModel, AuctionAdapter.AuctionHolder> {

    private FixedPriceAdapter.OnItemClickListener listener;

    public AuctionAdapter(@NonNull FirestoreRecyclerOptions<AuctionModel> options) {
        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull AuctionHolder auctionHolder, int i, @NonNull AuctionModel auctionModel) {

        Picasso.get().load(auctionModel.getAuctionImageUrl().get(0)).into(auctionHolder.imageURL);
        auctionHolder.textViewTitle.setText(auctionModel.getTitle());
        auctionHolder.textViewBid.setText("₱ "+auctionModel.getStartPrice());



    }

    @NonNull
    @Override
    public AuctionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fixedprice_custom_layout,parent,false);

        return new AuctionHolder(view);
    }

    class AuctionHolder extends RecyclerView.ViewHolder{

        ImageView imageURL;
        TextView textViewTitle, textViewBid;

        public AuctionHolder(@NonNull View itemView) {
            super(itemView);

            imageURL = itemView.findViewById(R.id.fp_imageview);
            textViewTitle = itemView.findViewById(R.id.fp_title);
            textViewBid = itemView.findViewById(R.id.fp_price);

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
