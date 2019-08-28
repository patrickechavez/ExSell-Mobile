package com.example.exsell.Adapter;

import android.text.format.DateFormat;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationAdapter extends FirestoreRecyclerAdapter<NotificationModel, NotificationAdapter.NotificationHolder> {


    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int i, @NonNull NotificationModel notificationModel) {

        long millisecond = notificationModel.getTimeStamp().getTime();
        Date currenDate = new Date(millisecond);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String stringCurrentDate = sdf.format(currenDate).toString();

        notificationHolder.textViewMessage.setText(notificationModel.getMessage());
        notificationHolder.textViewDate.setText(stringCurrentDate);
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);

        return new NotificationHolder(v);
    }

    class NotificationHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        TextView textViewDate;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);



            textViewMessage  = itemView.findViewById(R.id.notification_layout_textViewMessage);
            textViewDate = itemView.findViewById(R.id.notification_textViewDate);
        }
    }


   /* public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options) {
        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int i, @NonNull NotificationModel notificationModel) {


        notificationHolder.textViewMessage.setText(notificationModel.getMessage());

        long millisecond = notificationModel.getTimeStamp().getTime();
        Date currenDate = new Date(millisecond);
       SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String stringCurrentDate = sdf.format(currenDate).toString();
        //String currenDate = new SimpleDateFormat("MMM dd",new Date(millisecond)).toString();
        notificationHolder.textViewDate.setText(stringCurrentDate);
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);

        return new NotificationHolder(view);
    }

    class NotificationHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewMessage;
        TextView textViewDate;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.notification_layout_imageView);
            textViewMessage = itemView.findViewById(R.id.notification_layout_textViewMessage);
            textViewDate = itemView.findViewById(R.id.notification_textViewDate);
        }
    }*/
}
