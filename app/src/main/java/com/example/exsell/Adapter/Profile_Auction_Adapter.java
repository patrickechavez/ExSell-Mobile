package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.AuctionModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class Profile_Auction_Adapter extends FirestoreRecyclerAdapter<AuctionModel, Profile_Auction_Adapter.ProfileAuctionHolder> {



    public Profile_Auction_Adapter(@NonNull FirestoreRecyclerOptions<AuctionModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProfileAuctionHolder profileAuctionHolder, int i, @NonNull AuctionModel auctionModel) {

        profileAuctionHolder.textViewTitle.setText(auctionModel.getTitle());
        profileAuctionHolder.textViewStartPrice.setText("₱ "+auctionModel.getPrice());
        Picasso.get().load(auctionModel.getImageUrl().get(0)).into(profileAuctionHolder.imageViewAuctionPic);
    }

    @NonNull
    @Override
    public ProfileAuctionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_promote_custom_layout, parent, false);

        return new ProfileAuctionHolder(view);
    }

    class ProfileAuctionHolder extends RecyclerView.ViewHolder{


        ImageView imageViewAuctionPic;
        TextView textViewTitle, textViewStartPrice;
        Button buttonBump;


        public ProfileAuctionHolder(@NonNull View itemView) {
            super(itemView);

            imageViewAuctionPic = itemView.findViewById(R.id.profile_imageview);
            textViewTitle = itemView.findViewById(R.id.profile_title);
            textViewStartPrice = itemView.findViewById(R.id.profile_price);
            buttonBump = itemView.findViewById(R.id.profile_bumpBtn);
        }
    }
}
