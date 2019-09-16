package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class FixedPriceAdapter extends FirestoreRecyclerAdapter<FixedPriceModel, FixedPriceAdapter.FixedPriceHolder>{

    private OnItemClickListener listener;



    public FixedPriceAdapter(@NonNull FirestoreRecyclerOptions<FixedPriceModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FixedPriceHolder fixedPriceHolder, int i, @NonNull FixedPriceModel fixedPriceModel) {

        Picasso.get().load(fixedPriceModel.getImageUrl().get(0)).into(fixedPriceHolder.imageURL);
        fixedPriceHolder.remnantTitle.setText(fixedPriceModel.getTitle());
        fixedPriceHolder.remnantPrice.setText("₱ "+ fixedPriceModel.getPrice());
    }

    @NonNull
    @Override
    public FixedPriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fixedprice_custom_layout,parent,false);

        return new FixedPriceHolder(view);
    }








    class FixedPriceHolder extends RecyclerView.ViewHolder{

        ImageView imageURL;
        TextView remnantTitle, remnantPrice;

        public FixedPriceHolder(@NonNull View itemView) {
            super(itemView);

            imageURL = itemView.findViewById(R.id.fp_imageview);
            remnantTitle = itemView.findViewById(R.id.fp_title);
            remnantPrice = itemView.findViewById(R.id.fp_price);



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

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
