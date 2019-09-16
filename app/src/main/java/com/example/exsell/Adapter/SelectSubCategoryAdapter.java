package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.SelectSubCategoryModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SelectSubCategoryAdapter extends FirestoreRecyclerAdapter<SelectSubCategoryModel, SelectSubCategoryAdapter.SelectSubCategoryHolder> {



    public SelectSubCategoryAdapter(@NonNull FirestoreRecyclerOptions<SelectSubCategoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SelectSubCategoryHolder selectSubCategoryHolder, int i, @NonNull SelectSubCategoryModel selectSubCategoryModel) {

        selectSubCategoryHolder.textViewSubCategory.setText(selectSubCategoryModel.getSubCategoryName());

    }

    @NonNull
    @Override
    public SelectSubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectsubactegory_custom_layout,parent, false);

        return new SelectSubCategoryHolder(v);
    }

    class SelectSubCategoryHolder extends RecyclerView.ViewHolder{

        TextView textViewSubCategory;


        public SelectSubCategoryHolder(@NonNull View itemView) {
            super(itemView);

            textViewSubCategory = itemView.findViewById(R.id.selectSubCategory_textView);
        }
    }
}
