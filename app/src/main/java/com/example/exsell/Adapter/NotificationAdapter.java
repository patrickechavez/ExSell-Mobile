package com.example.exsell.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exsell.Models.NotificationModel;
import com.example.exsell.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationAdapter extends FirestoreRecyclerAdapter<NotificationModel, NotificationAdapter.NotificationHolder> {
    private OnItemClickListener listener;



    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int i, @NonNull NotificationModel notificationModel) {

        long millisecond = notificationModel.getTimeStamp().getTime();
        Date currenDate = new Date(millisecond);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String stringCurrentDate = sdf.format(currenDate).toString();


        Picasso.get().load(notificationModel.getImageUrl()).into(notificationHolder.imageView);
        notificationHolder.textViewMessage.setText(notificationModel.getMessage());
        notificationHolder.textViewMessage2.setText(notificationModel.getMessage2());
        notificationHolder.textViewDate.setText(stringCurrentDate);

    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_custom_layout,parent,false);

        return new NotificationHolder(v);
    }

    class NotificationHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage, textViewMessage2;
        TextView textViewDate;
        ImageView imageView;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.notification_imageView);
            textViewMessage  = itemView.findViewById(R.id.notification_firstLine);
            textViewMessage2 = itemView.findViewById(R.id.notification_secondLine);
            textViewDate = itemView.findViewById(R.id.notification_Date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
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
