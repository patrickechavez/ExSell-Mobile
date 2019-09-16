package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class Profile_FixedPrice_Adapter extends FirestoreRecyclerAdapter<FixedPriceModel, Profile_FixedPrice_Adapter.ProfileFixedPriceHolder> {



    public Profile_FixedPrice_Adapter(@NonNull FirestoreRecyclerOptions<FixedPriceModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProfileFixedPriceHolder profileFixedPriceHolder, int i, @NonNull FixedPriceModel fixedPriceModel) {


        profileFixedPriceHolder.textViewTitle.setText(fixedPriceModel.getTitle());
        profileFixedPriceHolder.textViewPrice.setText("₱ "+fixedPriceModel.getPrice());
        Picasso.get().load(fixedPriceModel.getImageUrl().get(0)).into(profileFixedPriceHolder.imageViewRemnant);

        profileFixedPriceHolder.buttonBump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    @NonNull
    @Override
    public ProfileFixedPriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_promote_custom_layout, parent, false);


        return new ProfileFixedPriceHolder(view);
    }

    class ProfileFixedPriceHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewPrice;
        ImageView imageViewRemnant;
        Button buttonBump;


        public ProfileFixedPriceHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.profile_title);
            textViewPrice = itemView.findViewById(R.id.profile_price);
            imageViewRemnant = itemView.findViewById(R.id.profile_imageview);
            buttonBump = itemView.findViewById(R.id.profile_bumpBtn);

        }
    }
}
