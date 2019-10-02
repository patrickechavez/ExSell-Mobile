package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class UnAvailAdapter extends FirestoreRecyclerAdapter<FixedPriceModel, UnAvailAdapter.UnAvailHolder> {


    private  UnAvailAdapter.OnItemClickListener listener;

    public UnAvailAdapter(@NonNull FirestoreRecyclerOptions<FixedPriceModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UnAvailHolder unAvailHolder, int i, @NonNull FixedPriceModel fixedPriceModel) {

        unAvailHolder.textViewTitle.setText(fixedPriceModel.getTitle());
        unAvailHolder.textViewType.setText(fixedPriceModel.getType());
        Picasso.get().load(fixedPriceModel.getImageUrl().get(0)).into(unAvailHolder.imageView);

    }

    @NonNull
    @Override
    public UnAvailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unavailable_remnants_custom_layout,parent,false);

        return new UnAvailHolder(v);
    }

    class  UnAvailHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewType;


        public UnAvailHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.unavail_imageView);
            textViewTitle = itemView.findViewById(R.id.unavail_textViewTitle);
            textViewType = itemView.findViewById(R.id.unavail_type);

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

    public void setOnItemClickListener(UnAvailAdapter.OnItemClickListener listener){
        this.listener = listener;
    }


}
