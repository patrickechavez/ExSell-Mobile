package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.BlogModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class BlogAdapter extends FirestoreRecyclerAdapter<BlogModel, BlogAdapter.BlogHolder>{


    private OnItemClickListener listener;


    public BlogAdapter(@NonNull FirestoreRecyclerOptions<BlogModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BlogHolder blogHolder, int i, @NonNull BlogModel blogModel) {

        blogHolder.textViewTitle.setText(blogModel.getTitle());
        Picasso.get().load(blogModel.getImageUrl().get(0)).into(blogHolder.imageView);

    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_custom_layout, parent,false);

        return new BlogHolder(v);
    }

    class BlogHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        ImageView imageView;

        public BlogHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.blog_customImageView);
            textViewTitle = itemView.findViewById(R.id.blog_customTextViewTitle);

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
