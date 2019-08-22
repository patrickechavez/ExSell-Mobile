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
import com.google.firebase.firestore.DocumentSnapshot;

public class SelectCategoryAdapter extends FirestoreRecyclerAdapter<SelectCategoryModel, SelectCategoryAdapter.SelectCategoryHolder> {
    private OnItemClickListener listener;


    public SelectCategoryAdapter(@NonNull FirestoreRecyclerOptions<SelectCategoryModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull SelectCategoryHolder selectCategoryHolder, int i, @NonNull SelectCategoryModel selectCategoryModel) {

        selectCategoryHolder.textViewCategoryName.setText(selectCategoryModel.getCategoryName());

    }

    @NonNull
    @Override
    public SelectCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_remnants_category_layout_fragment, parent, false);

        return new SelectCategoryHolder(v);
    }

    class SelectCategoryHolder extends RecyclerView.ViewHolder{

        TextView textViewCategoryName;

        public SelectCategoryHolder(@NonNull View itemView) {
            super(itemView);

            textViewCategoryName = itemView.findViewById(R.id.selectCategory_textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
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
