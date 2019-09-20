package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.SelectCategoryModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectCategoryAdapter extends FirestoreRecyclerAdapter<SelectCategoryModel, SelectCategoryAdapter.SelectCategoryHolder> {
    private OnItemClickListener listener;
    private static final String TAG = "SelectCategoryAdapter";

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    public SelectCategoryAdapter(@NonNull FirestoreRecyclerOptions<SelectCategoryModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull SelectCategoryHolder selectCategoryHolder, int i, @NonNull SelectCategoryModel selectCategoryModel) {

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentSnapshot snapshot = getSnapshots().getSnapshot(selectCategoryHolder.getAdapterPosition());
        String id = snapshot.getId();

        Picasso.get().load(selectCategoryModel.getCategoryImageUrl()).into(selectCategoryHolder.circleImageViewCategoryImage);
        selectCategoryHolder.textViewCategoryName.setText(selectCategoryModel.getCategoryName());

        firebaseFirestore.collection("category").document(id).collection("sub-category")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.getResult().size() > 0){

                    selectCategoryHolder.imageViewDropDown.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @NonNull
    @Override
    public SelectCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_custom_layout, parent, false);

        return new SelectCategoryHolder(v);
    }

    class SelectCategoryHolder extends RecyclerView.ViewHolder{

        TextView textViewCategoryName;
        CircleImageView circleImageViewCategoryImage;
        ImageView imageViewDropDown;

        public SelectCategoryHolder(@NonNull View itemView) {
            super(itemView);

            circleImageViewCategoryImage = itemView.findViewById(R.id.selectCategory_imageView);
            imageViewDropDown = itemView.findViewById(R.id.selectCategory_dropDown);
            textViewCategoryName = itemView.findViewById(R.id.selectCategory_textView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);

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
