package com.example.exsell.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.ListRemnants;
import com.example.exsell.Models.UploadPicModel;
import com.example.exsell.R;

import java.util.List;

public class UploadPicAdapter extends RecyclerView.Adapter<UploadPicAdapter.UploadPicHolder>{

    private List<UploadPicModel> uploadPicModels;
    private Context context;

    public UploadPicAdapter(Context context, List<UploadPicModel> uploadPicModels){

        this.uploadPicModels = uploadPicModels;
        this.context = context;
    }


    @NonNull
    @Override
    public UploadPicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_image_layout, parent, false);

        return new UploadPicAdapter.UploadPicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadPicHolder holder, int position) {

            UploadPicModel uploadPicModel = uploadPicModels.get(position);
            holder.imageView.setImageURI(uploadPicModel.getUpload_image());
    }

    @Override
    public int getItemCount() {
        return uploadPicModels.size();
    }

    public class UploadPicHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public UploadPicHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.upload_imageview);
        }
    }


}
