package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Models.AuctionModel;
import com.example.exsell.Models.UsersModel;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static com.example.exsell.R.color.accent_material_dark;
import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;

public class Auction extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView textViewTitle, textViewDescription, textViewBackStory, textViewBounceBack, textViewBidPrice, textViewStartPrice, textViewMeetup, textViewOwner, textViewMoreDetails, textViewEndtime, auction_availTimeTextView, auction_availTime;
    private CircleImageView userCircleImageView;
    private List<String> stringImageUrl;
   // private AuctionModel auctionModel;
    private UsersModel usersModel;
    private ExpandableRelativeLayout expandableRelativeLayout;
    private String auctionId, owner_id, user_id;
    private CarouselView auctionCarouselView;
    private EditText editTextbidAmount;
    private Button buttonPlaceBid, buttonBidders;
    private static final String TAG = "AUCTION";
    private double wallet, bidPricePartial, endTime1;
    private String firstName;
    private RelativeLayout auction_relativeAvaiLTime;
    private SimpleDateFormat df = new SimpleDateFormat(" MM/dd/yy h:mm a");
    private String title;
    private Double startPrice = 0.00;
    private boolean isSuccess = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity);

        Toolbar toolbar = findViewById(R.id.auction_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        auctionId = getIntent().getStringExtra("auctionId");
        user_id = mAuth.getCurrentUser().getUid();

        textViewTitle = findViewById(R.id.auction_title);
        textViewDescription = findViewById(R.id.auction_description);
        textViewBackStory = findViewById(R.id.auction_backStoryTextView);
        textViewBounceBack = findViewById(R.id.auction_bounceBackTextView);
        textViewBidPrice = findViewById(R.id.auction_bidPrice);
        textViewStartPrice = findViewById(R.id.auction_startPrice);
        textViewMeetup = findViewById(R.id.auction_meetup);
        textViewOwner = findViewById(R.id.auction_ownerTextView);
        textViewEndtime = findViewById(R.id.auction_endTime);
        textViewMoreDetails = findViewById(R.id.auction_moreDetailsTextView);
        auctionCarouselView = findViewById(R.id.auctionCarouselViews);
        userCircleImageView = findViewById(R.id.auction_circleImageView);
        buttonBidders = findViewById(R.id.auction_biddersBtn);

        editTextbidAmount = findViewById(R.id.auction_bidAmount);
        buttonPlaceBid = findViewById(R.id.auction_placeBtn);
        editTextbidAmount.addTextChangedListener(bidTextWatcher);

        auction_relativeAvaiLTime = findViewById(R.id.auction_relativeAvailTime);
        auction_availTime = findViewById(R.id.auction_availTime);
        auction_availTimeTextView = findViewById(R.id.auction_availTimeTextView);

        buttonPlaceBid.setOnClickListener(this);
        buttonBidders.setOnClickListener(this);
        textViewOwner.setOnClickListener(this);


        //GET THE VALUE OF WALLET FROM THE USER

       // checkifDeleted();
        fetchUserWallet();
        checkBidderInOption();


        DocumentReference docRef1 = firebaseFirestore.collection("remnants").document(auctionId);
        docRef1.addSnapshotListener((documentSnapshot, e) -> {

            if(documentSnapshot != null && documentSnapshot.exists()){

                title = documentSnapshot.getString("title");
                textViewTitle.setText(documentSnapshot.getString("title"));
                textViewDescription.setText(documentSnapshot.getString("description"));
                textViewBackStory.setText(documentSnapshot.getString("backStory"));
                textViewBounceBack.setText(documentSnapshot.getString("bounceBack"));
                startPrice = documentSnapshot.getDouble("price");
                textViewStartPrice.setText("₱ "+ documentSnapshot.getDouble("price"));
                textViewMeetup.setText(documentSnapshot.getString("meetup"));
                stringImageUrl = (List<String>) documentSnapshot.get("imageUrl");
                owner_id = documentSnapshot.getString("userId");

                if(documentSnapshot.getLong("endTime") != 0) {

                    long endTime = documentSnapshot.getLong("endTime");
                    textViewEndtime.setText(df.format(endTime));
                }else{

                    textViewEndtime.setText("Expired");
                }


                if(documentSnapshot.getLong("idleDuration") != 0){

                    long idleDuration = documentSnapshot.getLong("idleDuration");
                    auction_availTime.setText(df.format(idleDuration));
                }else{

                    auction_relativeAvaiLTime.setVisibility(View.GONE);
                }

                if (mAuth.getCurrentUser().getUid().equals(owner_id)) {

                    editTextbidAmount.setVisibility(View.GONE);
                    buttonPlaceBid.setVisibility(View.GONE);
                    buttonBidders.setVisibility(View.VISIBLE);

                    // START if (documentSnapshot.getString("startTime").equals("Schedule Start Time"))
                    if (documentSnapshot.getString("startTime").equals("Schedule Start Time")) {
                        editTextbidAmount.setVisibility(View.GONE);
                        buttonPlaceBid.setVisibility(View.GONE);

                        firebaseFirestore.collection("remnants")
                                .whereEqualTo(FieldPath.documentId(), auctionId)
                                .whereEqualTo("isExpired", false)
                                .whereEqualTo("isSoldOut", false)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){

                                    Thread thread = new Thread(){

                                        @Override
                                        public void run() {

                                            while (!isInterrupted()){

                                                try {
                                                    Thread.sleep(1000);

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                                                Log.d(TAG, "haha endTime:      "+queryDocumentSnapshot.getLong("endTime") / 1000);
                                                                Log.d(TAG, "haha currenTime:   "+System.currentTimeMillis() / 1000);

                                                                if(queryDocumentSnapshot.getLong("idleDuration") != 0) {

                                                                    if ((queryDocumentSnapshot.getLong("idleDuration") / 1000) <= (System.currentTimeMillis() / 1000)) {

                                                                        auction_relativeAvaiLTime.setVisibility(View.GONE);
                                                                        DocumentReference documentReference = firebaseFirestore.collection("remnants").document(auctionId);
                                                                        documentReference.update(
                                                                                 "idleDuration", 0);

                                                                    }
                                                                }

                                                                if(queryDocumentSnapshot.getLong("endTime") != 0) {

                                                                    if ((queryDocumentSnapshot.getLong("endTime") / 1000) == (System.currentTimeMillis() / 1000)) {

                                                                        // textViewEndtime.setText("Expired");
                                                                        editTextbidAmount.setVisibility(View.GONE);
                                                                        buttonPlaceBid.setVisibility(View.GONE);

                                                                        DocumentReference documentReference = firebaseFirestore.collection("remnants").document(auctionId);
                                                                        documentReference.update("endTime", 0
                                                                                    ,"isExpired", true);

                                                                        moreThan2();


                                                                    }
                                                                }


                                                            }
                                                        }
                                                    });
                                                } catch (InterruptedException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    };
                                    thread.start();
                                }
                            }
                        });
                    }else{

                        firebaseFirestore.collection("remnants")
                                .whereEqualTo(FieldPath.documentId(), auctionId)
                                .whereEqualTo("isExpired", false)
                                .whereEqualTo("isSoldOut", false)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    Thread thread = new Thread(){

                                        @Override
                                        public void run() {

                                            while (!isInterrupted()){

                                                try {
                                                    Thread.sleep(1000);

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {


                                                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                                                Log.d(TAG, "haha endTIme:      "+queryDocumentSnapshot.getLong("endTime") / 1000);
                                                                Log.d(TAG, "haha currenTime:   "+System.currentTimeMillis() / 1000);


                                                                if(documentSnapshot.getLong("endTime") != 0) {

                                                                    if ((queryDocumentSnapshot.getLong("endTime") / 1000) == (System.currentTimeMillis() / 1000)) {

                                                                        textViewEndtime.setText("Expired");
                                                                        editTextbidAmount.setVisibility(View.GONE);
                                                                        buttonPlaceBid.setVisibility(View.GONE);

                                                                        DocumentReference documentReference = firebaseFirestore.collection("remnants").document(auctionId);
                                                                        documentReference.update("endTime", 0
                                                                                , "isExpired", true);

                                                                        moreThan2();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });

                                                } catch (InterruptedException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    };
                                    thread.start();
                                }

                            }
                        });

                    }

                }else {

                   //NOT OWNER
                  //  Toast.makeText(this, "Not Owner", Toast.LENGTH_SHORT).show();
                    if (documentSnapshot.getString("startTime").equals("Schedule Start Time")) {
                        editTextbidAmount.setVisibility(View.GONE);
                        buttonPlaceBid.setVisibility(View.GONE);

                        firebaseFirestore.collection("remnants")
                                .whereEqualTo(FieldPath.documentId(), auctionId)
                                .whereEqualTo("isExpired", false)
                                .whereEqualTo("isSoldOut", false)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){

                                    Thread thread = new Thread(){

                                        @Override
                                        public void run() {

                                            while (!isInterrupted()){

                                                try {
                                                    Thread.sleep(1000);

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                                                Log.d(TAG, "haha endTime:      "+queryDocumentSnapshot.getLong("endTime") / 1000);
                                                                Log.d(TAG, "haha currenTime:   "+System.currentTimeMillis() / 1000);

                                                                if((queryDocumentSnapshot.getLong("idleDuration") / 1000) <= (System.currentTimeMillis() / 1000)){

                                                                    editTextbidAmount.setVisibility(View.VISIBLE);
                                                                    buttonPlaceBid.setVisibility(View.VISIBLE);
                                                                    auction_relativeAvaiLTime.setVisibility(View.GONE);
                                                                }

                                                                if(queryDocumentSnapshot.getLong("endTime") != 0) {

                                                                    if ((queryDocumentSnapshot.getLong("endTime") / 1000) == (System.currentTimeMillis() / 1000)) {

                                                                        textViewEndtime.setText("Expired");
                                                                        editTextbidAmount.setVisibility(View.GONE);
                                                                        buttonPlaceBid.setVisibility(View.GONE);

                                                                        DocumentReference documentReference = firebaseFirestore.collection("remnants").document(auctionId);
                                                                        documentReference.update("endTime", 0
                                                                                , "idleDuration", 0
                                                                                , "isExpired", true);

                                                                        moreThan2();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                } catch (InterruptedException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    };
                                    thread.start();
                                }
                            }
                        });
                    }else{

                        firebaseFirestore.collection("remnants")
                                .whereEqualTo(FieldPath.documentId(), auctionId)
                                .whereEqualTo("isExpired", false)
                                .whereEqualTo("isSoldOut", false)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    Thread thread = new Thread(){

                                        @Override
                                        public void run() {

                                            while (!isInterrupted()){

                                                try {
                                                    Thread.sleep(1000);

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                                                Log.d(TAG, "haha endTIme:      "+queryDocumentSnapshot.getLong("endTime") / 1000);
                                                                Log.d(TAG, "haha currenTime:   "+System.currentTimeMillis() / 1000);

                                                                if(documentSnapshot.getLong("endTime") != 0) {

                                                                    if ((queryDocumentSnapshot.getLong("endTime") / 1000) == (System.currentTimeMillis() / 1000)) {

                                                                        textViewEndtime.setText("Expired");
                                                                        editTextbidAmount.setVisibility(View.GONE);
                                                                        buttonPlaceBid.setVisibility(View.GONE);

                                                                        DocumentReference documentReference = firebaseFirestore.collection("remnants").document(auctionId);
                                                                        documentReference.update("endTime", 0
                                                                                , "isExpired", true);

                                                                        moreThan2();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });

                                                } catch (InterruptedException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    };
                                    thread.start();
                                }

                            }
                        });

                    }

                }

            }

            //FETCH OWNER PIC AND OWNER FIRSTNAME
            DocumentReference docRef6 = firebaseFirestore.collection("users").document(owner_id); //ERROR
            docRef6.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                    // usersModel = documentSnapshot.toObject(UsersModel.class);
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {

                        textViewOwner.setText(documentSnapshot.getString("firstName"));
                        List<String> imageUrl = Collections.singletonList(documentSnapshot.getString("imageUrl"));
                        Picasso.get().load(imageUrl.get(0)).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(userCircleImageView);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
            //END FETCH OWNER PIC AND OWNER FIRSTNAME

            auctionCarouselView.setImageListener(imageListener);
            auctionCarouselView.setPageCount(stringImageUrl.size());

            //GET THE LATEST BID
            firebaseFirestore.collection("bidders")
                    .whereEqualTo("remnantId", auctionId)
                    .orderBy("bidAmount", Query.Direction.DESCENDING)
                    .limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if(!queryDocumentSnapshots.isEmpty()){

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {


                            BigDecimal bigDecimal = new BigDecimal(doc.getDouble("bidAmount"));
                            bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);


                            textViewBidPrice.setText("₱ "+bigDecimal);
                            bidPricePartial = doc.getDouble("bidAmount");
                        }

                    }else{

                        BigDecimal bigDecimal1 = new BigDecimal(startPrice);
                        bigDecimal1 = bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_EVEN);

                        textViewBidPrice.setText("₱ "+bigDecimal1);
                        bidPricePartial = startPrice;
                    }

                }
            });
            //END GET THE LATEST BID
        }); //END
    }

    //BACK
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.edit_toolbar_menu, menu);


        DocumentReference docRef8 = firebaseFirestore.collection("remnants").document(auctionId);
        docRef8.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if(mAuth.getCurrentUser().getUid().equals(documentSnapshot.getString("userId"))){

                    if(!documentSnapshot.getBoolean("isSoldOut")) {



                        inflater.inflate(R.menu.edit_toolbar_menu, menu);

                        // Log.d(TAG, "haha ID "+documentSnapshot.getString("userId"));
                        menu.findItem(R.id.promote_remnant).setEnabled(false);
                        //FIRST
                        firebaseFirestore.collection("bidders")
                                .whereEqualTo("remnantId", auctionId)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    ArrayList<String> userId1 = new ArrayList<String>();

                                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                        if (userId1.isEmpty()) {

                                            userId1.add(queryDocumentSnapshot.getString("userId"));

                                        } else if (!userId1.contains(queryDocumentSnapshot.getString("userId"))) {

                                            userId1.add(queryDocumentSnapshot.getString("userId"));
                                        }
                                    }

                                    if(userId1.size() > 2 && !userId1.isEmpty()){
                                        isSuccess = true;
                                        Log.d(TAG, "haha "+isSuccess);
                                        menu.findItem(R.id.soldOut_remnant).setEnabled(true);
                                    }else{

                                        checkIfExpired();
                                        isSuccess = false;
                                        Log.d(TAG, "haha "+isSuccess);
                                        menu.findItem(R.id.soldOut_remnant).setEnabled(false);
                                    }
                                }
                            }
                        });
                        //LAST


                    }

                }else{

                    inflater.inflate(R.menu.report_toolbar_menu, menu);
                }
            }
        });



        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.promote_remnant:

              //  Toast.makeText(this, "Promote Remnant", Toast.LENGTH_SHORT).show();
                /*Intent promote = new Intent(Auction.this, Featured.class);
                promote.putExtra("remnantId", auctionId);
                startActivity(promote);*/
                break;

            case R.id.edit_remnant:

                Intent toUpdate = new Intent(Auction.this, Update_Remnants.class);
                toUpdate.putExtra("remnantId", auctionId);
                startActivity(toUpdate);


                break;

            case R.id.delete_remnant:

                ArrayList<String> documentId = new ArrayList<String>();

                firebaseFirestore.collection("remnants").document(auctionId)
                        .collection("bidders").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                        if(documentId.isEmpty()){

                                            deleteRemnant();
                                            //documentId.add(documentSnapshot.getId());

                                        }else if(!documentId.contains(documentSnapshot.getId())){

                                            documentId.add(documentSnapshot.getId());

                                            if(documentId.size() > 2){

                                                Toast.makeText(Auction.this, "The bidding is ongoing", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    }
                                }
                            }
                        });

                break;

            case R.id.reportRemnant:

                Intent i = new Intent(Auction.this, ReportRemnants.class);
                i.putExtra("seller_id", owner_id);
                i.putExtra("remnant_id", auctionId);
                startActivity(i);

              //  Toast.makeText(this, "report Remnant" + auctionId, Toast.LENGTH_SHORT).show();

                break;

            case R.id.soldOut_remnant:


                firebaseFirestore.collection("remnants").document(auctionId)
                        .update("isSoldOut", true);

                Intent toAct = new Intent(Auction.this, Activity.class);
                startActivity(toAct);

                Toast.makeText(this, "Moved to Sold Remnants", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void showMoreDetails(View view) {

        expandableRelativeLayout = findViewById(R.id.auction_expandableLinearLayout);
        expandableRelativeLayout.toggle();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            for (String s : stringImageUrl)
                Picasso.get().load(stringImageUrl.get(position)).into(imageView);

        }
    };
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.auction_placeBtn:

                if(!editTextbidAmount.getText().toString().isEmpty()) {

                    double bidAmount = Double.parseDouble(editTextbidAmount.getText().toString());

                    firebaseFirestore.collection("bidders")
                            .whereEqualTo("remnantId", auctionId)
                            .orderBy("bidAmount", Query.Direction.DESCENDING)
                            .limit(1)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (!task.getResult().isEmpty()) {

                                for (QueryDocumentSnapshot docs : task.getResult()) {

                                    if (docs.getString("userId").equals(mAuth.getCurrentUser().getUid())) {

                                        Toast.makeText(Auction.this, "You are the previous bidder. Please wait for other bidders to bid.", Toast.LENGTH_SHORT).show();

                                    } else {

                                        //GE
                                        if (wallet < bidAmount) {

                                            editTextbidAmount.setError("Insufficient Balance");
                                        } else if (wallet > bidAmount && wallet < bidAmount + 25) {
                                            editTextbidAmount.setError("You must have an extra balance atleast 25 pesos for the transaction fee");

                                        } else{

                                            Toast.makeText(Auction.this, "Bidding Successful", Toast.LENGTH_SHORT).show();

                                            //SEND TO AUCTION REMNANTS BIDDER
                                            Map<String, Object> bidAmountMap1 = new HashMap<>();
                                            bidAmountMap1.put("bidAmount", bidAmount);
                                            bidAmountMap1.put("userId", mAuth.getCurrentUser().getUid());
                                            bidAmountMap1.put("timeStamp", FieldValue.serverTimestamp());
                                            bidAmountMap1.put("remnantId", auctionId);


                                            firebaseFirestore.collection("bidders")
                                                    .add(bidAmountMap1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    Log.d(TAG, "haha sud successful");
                                                }
                                            });

                                            //SEND NOTIFICATION

                                            String message = firstName + " bid ₱" + bidAmount + " to your " + title + " remnant";
                                            Map<String, Object> bidNotification1 = new HashMap<>();
                                            bidNotification1.put("message", message);
                                            bidNotification1.put("sender_id", mAuth.getCurrentUser().getUid());
                                            bidNotification1.put("receiver_id", owner_id);
                                            bidNotification1.put("notificationType", "bidder");
                                            bidNotification1.put("timeStamp", FieldValue.serverTimestamp());

                                            firebaseFirestore.collection("notification")
                                                    .add(bidNotification1).addOnSuccessListener(documentReference -> {
                                            });
                                            //END OF SEND NOTIFICATION

                                        }//END OF GE
                                    }
                                }

                            } else {
                                //GE
                                if (wallet < bidAmount) {

                                    editTextbidAmount.setError("Insufficient Balance");
                                } else if (wallet > bidAmount && wallet < bidAmount + 25) {
                                    editTextbidAmount.setError("You must have an extra balance atleast 25 pesos for the transaction fee");

                                } else {

                                    Toast.makeText(Auction.this, "Bidding Successful", Toast.LENGTH_SHORT).show();

                                    //SEND TO AUCTION REMNANTS BIDDER
                                    Map<String, Object> bidAmountMap2 = new HashMap<>();
                                    bidAmountMap2.put("bidAmount", bidAmount);
                                    bidAmountMap2.put("userId", mAuth.getCurrentUser().getUid());
                                    bidAmountMap2.put("timeStamp", FieldValue.serverTimestamp());
                                    bidAmountMap2.put("remnantId", auctionId);


                                    firebaseFirestore.collection("bidders")
                                            .add(bidAmountMap2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            Log.d(TAG, "haha sud successful");
                                        }
                                    });


                                    //SEND NOTIFICATION
                                    String message = firstName + " bid ₱" + bidAmount + " to your " + title + " remnant";
                                    Map<String, Object> bidNotification2 = new HashMap<>();
                                    bidNotification2.put("message", message);
                                    bidNotification2.put("sender_id", mAuth.getCurrentUser().getUid());
                                    bidNotification2.put("receiver_id", owner_id);
                                    bidNotification2.put("notificationType", "bidder");
                                    bidNotification2.put("timeStamp", FieldValue.serverTimestamp());

                                    firebaseFirestore.collection("notification")
                                            .add(bidNotification2).addOnSuccessListener(documentReference -> {
                                    });
                                    //END OF SEND NOTIFICATION

                                }//END OF GE
                            }
                        }
                    });
                }else{

                    editTextbidAmount.setError("The field must not contain 0");
                }


                break;

            case R.id.auction_biddersBtn:

                Intent i = new Intent(Auction.this, Bidders.class);
                i.putExtra("auctionId", auctionId);
                startActivity(i);

                break;

            case R.id.auction_ownerTextView:

                if(!mAuth.getCurrentUser().getUid().equals(owner_id)) {

                    Intent ownerView = new Intent(Auction.this, UserProfile.class);
                    ownerView.putExtra("seller_id", owner_id);
                    startActivity(ownerView);
                }
                break;
        }
    }

    private TextWatcher bidTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //Toast.makeText(Auction.this, ""+Double.parseDouble(editTextbidAmount.getText().toString()), Toast.LENGTH_SHORT).show();

            try {
                Double bidAmount = Double.parseDouble(editTextbidAmount.getText().toString());
                Double bidPricePartial5 = bidPricePartial * 0.05 + bidPricePartial;
                Double bidPricePartial25 = bidPricePartial * 0.25 + bidPricePartial;

                BigDecimal bigBidAmounts = new BigDecimal(bidAmount);
                bigBidAmounts = bigBidAmounts.setScale(2, BigDecimal.ROUND_HALF_EVEN);

                BigDecimal  BigbidPricePartial5 = new BigDecimal(bidPricePartial5);
                BigbidPricePartial5 = BigbidPricePartial5.setScale(2, BigDecimal.ROUND_HALF_EVEN);

                BigDecimal  BigbidPricePartial25 = new BigDecimal(bidPricePartial25);
                BigbidPricePartial25 = BigbidPricePartial25.setScale(2, BigDecimal.ROUND_HALF_EVEN);

                if (bidAmount > bidPricePartial && !editTextbidAmount.getText().toString().isEmpty() && bidAmount >= bidPricePartial5 && bidAmount <= bidPricePartial25) {

                    buttonPlaceBid.setEnabled(true);
                    buttonPlaceBid.setBackgroundColor(getResources().getColor(colorPrimaryDark));
                } else {
                    buttonPlaceBid.setEnabled(false);
                    buttonPlaceBid.setBackgroundColor(getResources().getColor(buttonDisabled));
                    editTextbidAmount.setError("You must input " + BigbidPricePartial5 + " above and " + BigbidPricePartial25 + " below");
                }
            } catch (NumberFormatException e) {

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    private void fetchUserWallet() {
            final DocumentReference docref10 = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
            docref10.addSnapshotListener((documentSnapshot, e) -> {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    wallet = documentSnapshot.getDouble("wallet");
                    firstName = documentSnapshot.getString("firstName");
                } else {
                    Log.d(TAG, "Current data: null");
                }
            });
    }

    private void deleteRemnant(){

        firebaseFirestore.collection("remnants").document(auctionId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Auction.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void moreThan2(){

        firebaseFirestore.collection("bidders")
                .whereEqualTo("remnantId", auctionId)
                .orderBy("bidAmount", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    ArrayList<String> userId = new ArrayList<String>();

                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                        if(userId.isEmpty()){

                            userId.add(queryDocumentSnapshot.getString("userId"));

                        }else if(!userId.contains(queryDocumentSnapshot.getString("userId"))){

                            userId.add(queryDocumentSnapshot.getString("userId"));
                        }
                    }

                    if(userId.size() > 2 && !userId.isEmpty()){



                        /*firebaseFirestore.collection("remnants").document(auctionId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()){

                                    DocumentSnapshot documentSnapshot = task.getResult();*/


                                      //  if(documentSnapshot.getBoolean("isExpired")) {

                                            firebaseFirestore.collection("bidders")
                                                    .whereEqualTo("remnantId", auctionId)
                                                    .orderBy("bidAmount", Query.Direction.DESCENDING)
                                                    .limit(1).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {


                                                                //CONGRATULATIONS
                                                                String message = "Congratulations you're the new owner of this " + title;
                                                                Map<String, Object> notifData = new HashMap<>();
                                                                notifData.put("imageUrl", "https://i.ibb.co/SQdczqP/award.png");
                                                                notifData.put("notificationType", "awardBid");
                                                                notifData.put("receiver_id", queryDocumentSnapshot.getString("userId"));
                                                                notifData.put("sender_id", owner_id);
                                                                notifData.put("message", message);
                                                                notifData.put("timeStamp", FieldValue.serverTimestamp());

                                                                firebaseFirestore.collection("notification").add(notifData)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                Log.d(TAG, "haha sending notificatio to winner");
                                                                            }
                                                                        });

                                                                //SEND NOTIFICATION IF HAS A WINNER
                                                                String message1 = "Your auction for " + title + " is already finished, Check who's won the bid!";

                                                                Map<String, Object> notifData1 = new HashMap<>();
                                                                notifData1.put("imageUrl", "https://i.ibb.co/PFKHfQG/expired-Remnant.png");
                                                                notifData1.put("notificationType", "noticeOwner");
                                                                notifData1.put("sender_id", queryDocumentSnapshot.getString("userId"));
                                                                notifData1.put("receiver_id", owner_id);
                                                                notifData1.put("message", message1);
                                                                notifData1.put("timeStamp", FieldValue.serverTimestamp());

                                                                firebaseFirestore.collection("notification").add(notifData1)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                Log.d(TAG, "haha sending notification to owner");
                                                                            }
                                                                        });

                                                                //-25 FOR OWNER
                                                                DocumentReference documentReference = firebaseFirestore.collection("users").document(owner_id);
                                                                documentReference.update("wallet", FieldValue.increment(-25));


                                                                //-25 FOR WINNER
                                                                DocumentReference documentReference1 = firebaseFirestore.collection("users").document(queryDocumentSnapshot.getString("userId"));
                                                                documentReference1.update("wallet", FieldValue.increment(-25));

                                                                //ADD MONEY TO GENERATE REPORT FROM OWNER
                                                                Map<String, Object> gReport = new HashMap<>();
                                                                gReport.put("senderUser_id", owner_id);
                                                                gReport.put("transactionFee", 25);
                                                                gReport.put("type", "auctionFee");
                                                                gReport.put("timeStamp", FieldValue.serverTimestamp());

                                                                firebaseFirestore.collection("generateReport").add(gReport)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                Log.d(TAG, "haha send gReport from owner");
                                                                            }
                                                                        });

                                                                //ADD MONEY TO GENERATE REPORT FROM SELLER

                                                                Map<String, Object> gReport1 = new HashMap<>();
                                                                gReport1.put("senderUser_id", queryDocumentSnapshot.getString("userId"));
                                                                gReport1.put("transactionFee", 25);
                                                                gReport1.put("type", "auctionFee");
                                                                gReport.put("timeStamp", FieldValue.serverTimestamp());

                                                                firebaseFirestore.collection("generateReport").add(gReport1)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                Log.d(TAG, "haha send gReport from seller");
                                                                            }
                                                                        });


                                                                //MESSAGE TO WINNER FROM OWNER
                                                                Map<String, Object> toMessage = new HashMap<>();
                                                                String messageToSeller = "Congratulations, you've won this " + title + " with the winning bid price ₱" + queryDocumentSnapshot.getDouble("bidAmount") + ". \n Where and When should we meet?";
                                                                toMessage.put("message", messageToSeller);
                                                                toMessage.put("receiver", queryDocumentSnapshot.getString("userId"));
                                                                toMessage.put("sender", owner_id);
                                                                toMessage.put("type", "text");
                                                                toMessage.put("time", FieldValue.serverTimestamp());

                                                                firebaseFirestore.collection("chat").add(toMessage)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {


                                                                                Log.d(TAG, "haha send message successful");
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                       // }

                             /*   } //end of task.isSUccessful
                            }
                        });
*/



                    firebaseFirestore.collection("remnants").document(auctionId)
                            .update("endTime", 0);

                    }else{

                        //NO WINNER
                        //String message1 = "Your " +title+ "is already finished";
                        String message3 = "There weren't enough bidders for your auction of "+title;
                        Map<String, Object> notifData3 = new HashMap<>();
                        notifData3.put("imageUrl", "https://i.ibb.co/PFKHfQG/expired-Remnant.png");
                        notifData3.put("notificationType", "noBidder");
                        notifData3.put("remnants_id", auctionId);
                        notifData3.put("receiver_id", owner_id);
                        notifData3.put("message", message3);
                        notifData3.put("timeStamp", FieldValue.serverTimestamp());

                        firebaseFirestore.collection("notification").add(notifData3)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Log.d(TAG, "haha no bidder");
                                    }
                                });

                        firebaseFirestore.collection("remnants").document(auctionId)
                                .update("endTime", 0);
                    }

                }
            }
        });
    }

    private void checkIfExpired(){

        firebaseFirestore.collection("remnants").document(auctionId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.getBoolean("isExpired")){

                        new MaterialAlertDialogBuilder(Auction.this)
                                .setTitle("Not Enough Bidders")
                                .setMessage("There weren’t enough bidders for your auction, "+title+" What would you want to do?")
                                .setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        firebaseFirestore.collection("remnants").document(auctionId)
                                                .update("isDeleted", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toast.makeText(Auction.this, "Removed Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Intent toDashboard = new Intent(Auction.this, Dashboard.class);
                                        startActivity(toDashboard);




                                    }
                                }).setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                Intent toUpdate = new Intent(Auction.this, Update_Remnants.class);
                                toUpdate.putExtra("remnantId", auctionId);
                                startActivity(toUpdate);



                            }
                        }).show();

                    }
                }
            }
        });


    }

    private void checkifDeleted(){

            firebaseFirestore.collection("remnants").document(auctionId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.getBoolean("isDeleted")) {


                    }
                }
            });
        }

    private void checkBidderInOption(){

        firebaseFirestore.collection("bidders")
                .whereEqualTo("remnantId", auctionId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    ArrayList<String> userId1 = new ArrayList<String>();

                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                        if (userId1.isEmpty()) {

                            userId1.add(queryDocumentSnapshot.getString("userId"));

                        } else if (!userId1.contains(queryDocumentSnapshot.getString("userId"))) {

                            userId1.add(queryDocumentSnapshot.getString("userId"));
                        }


                    }

                        if(userId1.size() > 2 && !userId1.isEmpty()){

                            isSuccess = true;

                            Log.d(TAG, "haha "+isSuccess);

                        }else{

                            checkIfExpired();
                            isSuccess = false;
                            Log.d(TAG, "haha "+isSuccess);


                        }

                }
            }
        });

    }



}