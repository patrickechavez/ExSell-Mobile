package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.exsell.Adapter.NotificationAdapter;
import com.example.exsell.Models.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        notificationAdapter.setOnItemClickListener((documentSnapshot, position) -> {

            NotificationModel notificationModel = documentSnapshot.toObject(NotificationModel.class);

           /* String remnant_id = notificationModel.getRemnants_id();
            Intent toAUction = new Intent(Notification.this, Auction.class);
            toAUction.putExtra("receiver_id", remnant_id);
            startActivity(toAUction);*/

           // Toast.makeText(this, ""+notificationModel.getNotificationType(), Toast.LENGTH_SHORT).show();

            if(notificationModel.getNotificationType().equals("buyer")){

               // Toast.makeText(this, "buyer", Toast.LENGTH_SHORT).show();
                String sender_id = notificationModel.getSender_id();
                Intent chat = new Intent(Notification.this, Chat.class);
                chat.putExtra("receiver_id", sender_id);
                startActivity(chat);

            }else if(notificationModel.getNotificationType().equals("upcomingBid")){

               // Toast.makeText(this, "upcomingBid", Toast.LENGTH_SHORT).show();

                String remnant_id = notificationModel.getRemnants_id();
                Intent auction = new Intent(Notification.this, Auction.class);
                auction.putExtra("auctionId", remnant_id);
                startActivity(auction);

            }else if(notificationModel.getNotificationType().equals("awardBid")) {


               // Toast.makeText(this, "awardBid", Toast.LENGTH_SHORT).show();

                String sender_id1 = notificationModel.getSender_id();
                Intent chat1 = new Intent(Notification.this, Chat.class);
                chat1.putExtra("receiver_id", sender_id1);
                startActivity(chat1);

            }else if(notificationModel.getNotificationType().equals("noticeOwner")){


                //Toast.makeText(this, "noticeOwner", Toast.LENGTH_SHORT).show();

                String sender_id2 = notificationModel.getSender_id();
                Intent chat2 = new Intent(Notification.this, Chat.class);
                chat2.putExtra("receiver_id", sender_id2);
                startActivity(chat2);

            }else if(notificationModel.getNotificationType().equals("noBidder")){


              //  Toast.makeText(this, "noBidder", Toast.LENGTH_SHORT).show();

                String remnant_id2 = notificationModel.getRemnants_id();
                Intent auction2 = new Intent(Notification.this, Auction.class);
                auction2.putExtra("auctionId", remnant_id2);
                startActivity(auction2);
            }
                /*String remnant_id = notificationModel.getRemnants_id();

                DocumentReference docRef1 = firebaseFirestore.collection("remnants").document(remnant_id);
                docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot documentSnapshot1 = task.getResult();

                            if(documentSnapshot1.getString("type").equals("Fixed Price")){

                                Intent fixedPrice = new Intent(Notification.this, FixedPrice.class);
                                fixedPrice.putExtra("remnantId", remnant_id);
                                startActivity(fixedPrice);

                            }else{

                                Intent auction = new Intent(Notification.this, Auction.class);
                                auction.putExtra("auctionId", remnant_id);
                                startActivity(auction);
                            }

                        }else{

                        }
                    }
                });
*/

            
        });
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
