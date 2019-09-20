package com.example.exsell;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class MyService extends Service {


    private static final String TAG = "SERVICE";
    String remnant_id, seller_id, remnantName;
    long endTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        fetchData();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private void fetchData() {
        FirebaseFirestore.getInstance().collection("remnants")
                .whereEqualTo("type", "Auction")
                .whereEqualTo("isSoldOut", false)
                .whereEqualTo("isDeleted", false)
                .whereEqualTo("isActive", true)
                .whereEqualTo("isExpired", false)
                .get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){

                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                    remnant_id = queryDocumentSnapshot.getId();
                    seller_id = queryDocumentSnapshot.getString("userId");
                    remnantName = queryDocumentSnapshot.getString("title");
                    endTime = queryDocumentSnapshot.getLong("endTime").intValue();


                    Log.d(TAG, "haha current Time: "+System.currentTimeMillis() / 1000);
                    Log.d(TAG,"haha end Time:     "+endTime);

                                                   /* String stringEndTime = df.format(endTime * 1000);
                                                    String currentTime = df.format(System.currentTimeMillis());*/

                    if(endTime == System.currentTimeMillis() / 1000){

                        //CHECK IF AUCTION HAS BIDDER
                        FirebaseFirestore.getInstance().collection("remnants").document(remnant_id)
                                .collection("bidders")
                                .get()
                                .addOnCompleteListener(task1 -> {

                                    if(task1.isSuccessful()){

                                        if(task1.getResult().size() > 0){

                                            Log.d(TAG,"haha naay sud end Time: "+queryDocumentSnapshot.getLong("endTime").intValue());

                                            FirebaseFirestore.getInstance().collection("remnants").document(remnant_id)
                                                    .collection("bidders")
                                                    .orderBy("bidAmount", Query.Direction.DESCENDING).limit(1)
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task1) {

                                                    if(task1.isSuccessful()){

                                                        for(QueryDocumentSnapshot queryDocumentSnapshot1: task1.getResult()){

                                                            DocumentReference docRef10 = FirebaseFirestore.getInstance().collection("remnants").document(remnant_id);
                                                            docRef10.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {

                                                                    if(task1.isSuccessful()){

                                                                        DocumentSnapshot document = task1.getResult();

                                                                        if(document.getBoolean("isSoldOut") == false){


                                                                            //SEND NOTIFICATION TO WINNER
                                                                            String message = "Congratulations, You are the new owner of "+remnantName;
                                                                            Map<String, Object> notifData = new HashMap<>();
                                                                            notifData.put("receiver_id", queryDocumentSnapshot1.getString("userId"));
                                                                            notifData.put("message",message);
                                                                            notifData.put("imageUrl", "https://i.ibb.co/wYKRnpK/bidItem.png");
                                                                            notifData.put("message2", "");
                                                                            notifData.put("remnants_id", remnant_id);
                                                                            notifData.put("notificationType", "bidWinner");
                                                                            notifData.put("sender_id", seller_id);
                                                                            notifData.put("timeStamp", FieldValue.serverTimestamp());

                                                                            FirebaseFirestore.getInstance().collection("notification")
                                                                                    .add(notifData)
                                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentReference documentReference) {

                                                                                            Log.d(TAG,"HAHA NOTIFICATION SENT");


                                                                                            //UPDATE TO SOLD AUCTION AND HAS BIDDER
                                                                                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("remnants").document(queryDocumentSnapshot.getId());
                                                                                            docRef.update("isSoldOut", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {


                                                                                                    Log.d(TAG,"HAHA SOLD AUCTION SUCCESS");
                                                                                                }
                                                                                            });

                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });

                                        }else{

                                            Log.d(TAG,"haha way  sud end Time: "+queryDocumentSnapshot.getLong("endTime").intValue());

                                            //DELETE AUCTION IF NO ONE BID
                                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("remnants").document(queryDocumentSnapshot.getId());
                                            docRef.update("isExpired", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Log.d(TAG, "HAHA EXPIRED REMNANT SUCCESSFUL");
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }


}
