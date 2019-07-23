package com.example.exsell.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Chat;
import com.example.exsell.Models.UsersModel;
import com.example.exsell.R;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UsersAdapter extends FirestoreRecyclerAdapter<UsersModel, UsersAdapter.UserHolder> {



    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;
    public UsersAdapter(@NonNull FirestoreRecyclerOptions<UsersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserHolder userHolder, int i, @NonNull UsersModel usersModel) {

        userHolder.firstName.setText(usersModel.getFirstName());
        userHolder.lastName.setText(usersModel.getLastName());

        if(usersModel.getImageUrl().equals("default")) {
            userHolder.profile_image.setImageResource(R.drawable.user);
        }else{
            Picasso.get().load(usersModel.getImageUrl()).into(userHolder.profile_image);
        }
        if(usersModel.getOnline().equals("true")){
            userHolder.image_online.setVisibility(View.VISIBLE);
        }else{
            userHolder.image_online.setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_users_item, parent, false);
        return new UserHolder(view);
    }

    class UserHolder extends  RecyclerView.ViewHolder{

        public TextView firstName;
        public TextView lastName;
        public ImageView profile_image;
        public ImageView image_online;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.chat_fname);
            lastName = itemView.findViewById(R.id.chat_lname);
            profile_image = itemView.findViewById(R.id.chat_image);
            image_online = itemView.findViewById(R.id.image_online);

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
