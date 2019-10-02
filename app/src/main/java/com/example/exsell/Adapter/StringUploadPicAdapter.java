package com.example.exsell.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.UploadPicModel;
import com.example.exsell.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StringUploadPicAdapter extends RecyclerView.Adapter<StringUploadPicAdapter.StringUploadPicHolder> {


    private List<String> stringImageUrl;
    private Context context;

    public StringUploadPicAdapter(Context context, List<String> stringImageUrl){

        this.stringImageUrl = stringImageUrl;
        this.context = context;
    }

    @NonNull
    @Override
    public StringUploadPicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_image_layout, parent, false);

        return new StringUploadPicAdapter.StringUploadPicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringUploadPicHolder holder, int position) {


        /*holder.imageView.setImageURI(uploadPicModel.getImageUrl());*/
        Picasso.get().load(stringImageUrl.get(position)).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return stringImageUrl.size();
    }


    public class  StringUploadPicHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public StringUploadPicHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.upload_imageview);
        }
    }

}
