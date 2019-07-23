package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.SelectCategoryModel;
import com.example.exsell.R;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SelectCategoryAdapter extends FirestoreRecyclerAdapter<SelectCategoryModel, SelectCategoryAdapter.SelectCategoryHolder> {


    public SelectCategoryAdapter(@NonNull FirestoreRecyclerOptions<SelectCategoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SelectCategoryHolder selectCategoryHolder, int i, @NonNull SelectCategoryModel selectCategoryModel) {
        selectCategoryHolder.textCategory.setText(selectCategoryModel.getCategoryName());

    }

    @NonNull
    @Override
    public SelectCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_layout, parent,false);

        return new SelectCategoryHolder(v);
    }

    class SelectCategoryHolder extends RecyclerView.ViewHolder{

        TextView textCategory;

        public SelectCategoryHolder(@NonNull View itemView) {
            super(itemView);

            textCategory = itemView.findViewById(R.id.select_category_textview);
        }
    }
}
