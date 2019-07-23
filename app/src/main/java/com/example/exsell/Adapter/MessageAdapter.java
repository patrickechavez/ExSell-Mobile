package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.MessageModel;
import com.example.exsell.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{


    private List<MessageModel> messageModels;
    private FirebaseAuth mAuth;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    public MessageAdapter(List<MessageModel> messageModels){

        this.messageModels = messageModels;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }else{

           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final MessageModel message = messageModels.get(position);

        holder.show_message.setText(message.getMessage());

        FirebaseFirestore.getInstance().collection("users").document(message.getSender()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){

                        String imageUrl = documentSnapshot.getString("imageUrl");
                        // String image = documentSnapshot.getString("imageUrl");
                        Picasso.get().load(imageUrl).placeholder(R.drawable.user).into(holder.profile_image);

                    }
                }
            }
        });


        }




    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView  profile_image;



        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            show_message = itemView.findViewById(R.id.show_message);


        }
    }

    @Override
    public int getItemViewType(int position) {

        mAuth = FirebaseAuth.getInstance();
        String sender_id = mAuth.getCurrentUser().getUid();

        if(messageModels.get(position).getSender().equals(sender_id)){

            return MSG_TYPE_RIGHT;
        }else{

            return MSG_TYPE_LEFT;
        }
    }
}


