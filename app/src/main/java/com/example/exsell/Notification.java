package com.example.exsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.exsell.Adapter.NotificationAdapter;
import com.example.exsell.Models.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Notification extends AppCompatActivity {

    private static final String TAG = "NOTIFICATION";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private NotificationAdapter notificationAdapter;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        Toolbar toolbar = findViewById(R.id.notification_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NOTIFICATION");

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

         user_id = mAuth.getCurrentUser().getUid();

        setUpRecycleView();
    }

    private void setUpRecycleView() {

        Query query = firebaseFirestore.collection("notification")
                .whereEqualTo("receiver_id", mAuth.getCurrentUser().getUid())
                .orderBy("timeStamp", Query.Direction.DESCENDING);



        FirestoreRecyclerOptions<NotificationModel> options = new FirestoreRecyclerOptions.Builder<NotificationModel>()
                .setQuery(query, NotificationModel.class)
                .build();

        notificationAdapter = new NotificationAdapter(options);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        RecyclerView recyclerView = findViewById(R.id.notifications_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(notificationAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        notificationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }
}
