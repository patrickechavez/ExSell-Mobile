package com.example.exsell.Adapter;

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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Sellinghistory_Adapter extends FirestoreRecyclerAdapter<SellingHistoryModel, Sellinghistory_Adapter.SellingHistoryHolder> {


    public Sellinghistory_Adapter(@NonNull FirestoreRecyclerOptions<SellingHistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SellingHistoryHolder sellingHistoryHolder, int i, @NonNull SellingHistoryModel sellingHistoryModel) {

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
            //sellingHistoryHolder.t

        }

    }

    @NonNull
    @Override
    public SellingHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_sellinghistory_custom_layout, parent, false);

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
