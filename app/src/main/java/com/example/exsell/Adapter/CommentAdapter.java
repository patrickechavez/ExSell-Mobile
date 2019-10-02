package com.example.exsell.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.CommentModel;
import com.example.exsell.Models.MessageModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends FirestoreRecyclerAdapter<CommentModel, CommentAdapter.CommentHolder> {

    private static final String TAG = "COMMENT ADAPTER";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Context mContext;
    private String commentId;
    private String blogId;

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<CommentModel> options, Context mContext) {
        super(options);

        this.mContext =  mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentHolder commentHolder, int i, @NonNull CommentModel commentModel) {

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*DocumentSnapshot documentSnapshot1 = getSnapshots().getSnapshot(commentHolder.getAdapterPosition());
        commentId = documentSnapshot1.getId();*/

        blogId = commentModel.getBlogId();

        firebaseFirestore.collection("users").document(commentModel.getUserId())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                DocumentSnapshot documentSnapshot = task.getResult();

                if(!documentSnapshot.getId().equals(mAuth.getCurrentUser().getUid())){

                    commentHolder.imageViewOption.setVisibility(View.GONE);
                }

                Picasso.get().load(documentSnapshot.getString("imageUrl")).into(commentHolder.circleImageView);
                commentHolder.textViewFirstName.setText(documentSnapshot.getString("firstName"));
                commentHolder.textViewLastName.setText(documentSnapshot.getString("lastName"));
            }
        });

        commentHolder.textViewComment.setText(commentModel.getComment());
        commentHolder.imageViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMenu(v,commentHolder.getAdapterPosition());
            }
        });

    }

    private void showMenu(View view, int position) {

        PopupMenu popup = new PopupMenu(mContext, view);
        popup.getMenuInflater().inflate(R.menu.comment_toolbar_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.comment_delete:


                        deleteComment(position);

                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_custom_layout, parent, false);

        return new CommentHolder(v);
    }

    public void deleteComment(int position){


        getSnapshots().getSnapshot(position).getReference().delete();
       // Toast.makeText(mContext, "id" +getSnapshots().getSnapshot(position).getReference().getId(), Toast.LENGTH_SHORT).show();
    }



   /* @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){

            case R.id.comment_delete:


                *//*firebaseFirestore.collection("blogs").document(blogId).collection("comment")
                        .document(commentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(mContext, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(mContext, "Error Deleting Comment", Toast.LENGTH_SHORT).show();

                            }
                        });*//*

                Toast.makeText(mContext, "DELETE "+commentId, Toast.LENGTH_SHORT).show();

                return true;

            case R.id.comment_update:


                Toast.makeText(mContext, "UPDATE", Toast.LENGTH_SHORT).show();
                return  true;

                default:
                    return false;
        }

    }*/


    class CommentHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        ImageView imageViewOption;
        TextView textViewFirstName, textViewLastName, textViewComment, textViewDate;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.comment_imageView);
            imageViewOption = itemView.findViewById(R.id.comment_option);
            textViewFirstName = itemView.findViewById(R.id.comment_textViewFirstName);
            textViewLastName = itemView.findViewById(R.id.comment_textLastName);
            textViewComment = itemView.findViewById(R.id.comment_textViewComment);
            textViewDate = itemView.findViewById(R.id.comment_textViewDate);
        }
    }


}
