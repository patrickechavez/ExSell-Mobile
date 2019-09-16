package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;

public class Auction extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView textViewTitle, textViewDescription, textViewBackStory, textViewBounceBack, textViewBidPrice, textViewStartPrice, textViewMeetup, textViewOwner, textViewMoreDetails, textViewEndtime, auction_availTimeTextView, auction_availTime;
    private CircleImageView userCircleImageView;
    private List<String> stringImageUrl;
    private AuctionModel auctionModel;
    private UsersModel usersModel;
    private ExpandableRelativeLayout expandableRelativeLayout;
    private String auctionId, owner_id, user_id;
    private CarouselView auctionCarouselView;
    private EditText editTextbidAmount;
    private Button buttonPlaceBid, buttonBidders;
    private static final String TAG = "AUCTION";
    private double wallet, bidPricePartial, endTime1;
    private String firstName;
    private boolean qwe = false;
    private RelativeLayout auction_relativeAvaiLTime;
    private SimpleDateFormat df = new SimpleDateFormat("h:mm a");


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
        final DocumentReference docref = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

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
            }
        });

        DocumentReference docRef = firebaseFirestore.collection("remnants").document(auctionId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {

            SimpleDateFormat df = new SimpleDateFormat("h:mm a");

            auctionModel = documentSnapshot.toObject(AuctionModel.class);

            textViewTitle.setText(auctionModel.getTitle());
            textViewDescription.setText(auctionModel.getDescription());
            textViewBackStory.setText(auctionModel.getBackStory());
            textViewBounceBack.setText(auctionModel.getBounceBack());

            long endTime = auctionModel.getEndTime() * 1000;
            textViewEndtime.setText(df.format(endTime));


            double startPrice = auctionModel.getPrice();
            textViewStartPrice.setText("₱ " + startPrice);
            textViewMeetup.setText(auctionModel.getMeetup());


            owner_id = auctionModel.getUserId();
            stringImageUrl = auctionModel.getImageUrl();

            long listTime = auctionModel.getIdleDuration() * 1000;
            auction_availTime.setText(df.format(listTime));



            if (user_id.equals(owner_id)) {

                editTextbidAmount.setVisibility(View.GONE);
                buttonPlaceBid.setVisibility(View.GONE);
                buttonBidders.setVisibility(View.VISIBLE);
            }
            else {

                if (auctionModel.getStartTime().equals("Schedule Start Time")) {
                    editTextbidAmount.setVisibility(View.GONE);
                    buttonPlaceBid.setVisibility(View.GONE);

                    DocumentReference docRef7 = firebaseFirestore.collection("remnants").document(auctionId);
                    docRef7.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();


                                Thread thread = new Thread() {

                                    @Override
                                    public void run() {
                                        while (!isInterrupted()) {
                                            try {
                                                Thread.sleep(1000);

                                                runOnUiThread(() -> {

                                                    /*String currentTime = df.format(System.currentTimeMillis() / 1000);
                                                    String idleDuration = df.format(documentSnapshot.getLong("idleDuration") * 1000);
                                                    String endTime = df.format(documentSnapshot.getLong("endTime") * 1000);*/


                                                    if (documentSnapshot.getLong("idleDuration").intValue() <= (System.currentTimeMillis() / 1000)) {
                                                        editTextbidAmount.setVisibility(View.VISIBLE);
                                                        buttonPlaceBid.setVisibility(View.VISIBLE);
                                                        auction_relativeAvaiLTime.setVisibility(View.GONE);


                                                    }
                                                    if (documentSnapshot.getLong("endTime") < (System.currentTimeMillis() / 1000)) {

                                                        textViewEndtime.setText("Expired");
                                                        editTextbidAmount.setVisibility(View.GONE);
                                                        buttonPlaceBid.setVisibility(View.GONE);

                                                    }
                                                });


                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
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
            //HIDE AVAILABLE AND TIME AND EXPIRED IF THE START TIME IS START IMMEDIATELY

            if(auctionModel.getStartTime().equals("Start Immediately")){

                auction_relativeAvaiLTime.setVisibility(View.GONE);

                DocumentReference docRef8 = firebaseFirestore.collection("remnants").document(auctionId);
                docRef8.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();


                            Thread thread = new Thread() {

                                @Override
                                public void run() {
                                    while (!isInterrupted()) {
                                        try {
                                            Thread.sleep(1000);

                                            runOnUiThread(() -> {


                                                    if(documentSnapshot.getLong("endTime") < (System.currentTimeMillis() / 1000)){


                                                        //DELETE AUCTION IF NO ONE BID
                                                        DocumentReference docRef = firebaseFirestore.collection("remnants").document(auctionId);
                                                        docRef.update("isExpired", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Log.d(TAG, "HAHA EXPIRED REMNANT SUCCESSFUL");
                                                            }
                                                        });


                                                        textViewEndtime.setText("Expired");
                                                        editTextbidAmount.setVisibility(View.GONE);
                                                        buttonPlaceBid.setVisibility(View.GONE);

                                                    }

                                            });


                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            };

                            thread.start();
                        }
                    }
                });
            }

            //END HIDE AVAILABLE AND TIME AND EXPIRED IF THE START TIME IS START IMMEDIATELY


            //FETCH LATEST BID PRICE
            firebaseFirestore.collection("remnants").document(auctionId)
                    .collection("bidders")
                    .get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    if (task.getResult().size() > 0) {

                        Log.d(TAG, "success startPrice: " + startPrice);

                        firebaseFirestore.collection("remnants").document(auctionId)
                                .collection("bidders").orderBy("bidAmount", Query.Direction.DESCENDING).limit(1)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                        if (e != null) {
                                            Log.w(TAG, "Listen failed.", e);
                                            return;
                                        }

                                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                            if (doc.get("bidAmount") != null) {

                                                bidPricePartial = doc.getDouble("bidAmount");
                                                textViewBidPrice.setText("₱ " + bidPricePartial);
                                            }
                                        }
                                    }
                                });
                    } else {

                        Log.d(TAG, "failed startPrice: " + startPrice);

                        bidPricePartial = startPrice;
                        textViewBidPrice.setText("₱ " + bidPricePartial);
                    }
                }
            });

            //FETCH OWNER PIC AND OWNER FIRSTNAME
            DocumentReference docRef1 = firebaseFirestore.collection("users").document(owner_id);
            docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    usersModel = documentSnapshot.toObject(UsersModel.class);


                    textViewOwner.setText(usersModel.getFirstName());
                    Picasso.get().load(usersModel.getImageUrl()).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(userCircleImageView);
                }

            });
            auctionCarouselView.setImageListener(imageListener);
            auctionCarouselView.setPageCount(stringImageUrl.size());

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.edit_toolbar_menu, menu);

        DocumentReference docRef1 = firebaseFirestore.collection("remnants").document(auctionId);
        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(mAuth.getCurrentUser().getUid().equals(documentSnapshot.getString("userId"))){


                        inflater.inflate(R.menu.edit_toolbar_menu, menu);
                        // Log.d(TAG, "haha ID "+documentSnapshot.getString("userId"));
                    }else{

                        inflater.inflate(R.menu.report_toolbar_menu, menu);
                    }
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.promote_remnant:

                Toast.makeText(this, "Promote Remnant", Toast.LENGTH_SHORT).show();
                break;

            case R.id.edit_remnant:

                Toast.makeText(this, "Edit Remnant", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_remnant:

                DocumentReference docRef = firebaseFirestore.collection("remnants").document(auctionId);

                docRef.update("isDeleted", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(Auction.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Auction.this, Dashboard.class);
                                startActivity(i);
                            }
                        });
                break;

            case R.id.reportRemnant:

                Intent i = new Intent(Auction.this, ReportRemnants.class);
                i.putExtra("seller_id", owner_id);
                i.putExtra("remnant_id", auctionId);
                startActivity(i);

                Toast.makeText(this, "report Remnant" + auctionId, Toast.LENGTH_SHORT).show();

                break;
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

                double bidAmount = Double.parseDouble(editTextbidAmount.getText().toString());
                if (wallet < bidAmount)  {

                    editTextbidAmount.setError("Insufficient Balance");

                }else if(wallet > bidAmount && wallet < bidAmount + 25) {

                    editTextbidAmount.setError("You must have an extra balance atleast 25 pesos for the transaction fee");

                }else {

                    Toast.makeText(this, "Bidding Successful", Toast.LENGTH_SHORT).show();
                    //SEND TO AUCTION REMNANTS BIDDER

                    Map<String, Object> bidAmountMap = new HashMap<>();
                    bidAmountMap.put("bidAmount", bidAmount);
                    bidAmountMap.put("userId", mAuth.getCurrentUser().getUid());
                    bidAmountMap.put("timeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("remnants").document(auctionId).collection("bidders")
                            .add(bidAmountMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(Auction.this, "Bidding Successful", Toast.LENGTH_SHORT).show();
                                    editTextbidAmount.setText("");
                                }
                            });


                    //SEND NOTIFICATION
                    SimpleDateFormat df = new SimpleDateFormat("MMM d");
                    Calendar cal = Calendar.getInstance();
                    String currentTime = df.format(Calendar.getInstance().getTime());

                    String message = firstName + " bid ₱" + bidAmount + " to your " + auctionModel.getTitle() + " remnant";
                    Map<String, Object> bidNotification = new HashMap<>();
                    bidNotification.put("message", message);
                    bidNotification.put("sender_id", mAuth.getCurrentUser().getUid());
                    bidNotification.put("receiver_id", owner_id);
                    bidNotification.put("notificationType", "bidder");
                    bidNotification.put("timeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("notification")
                            .add(bidNotification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    });
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

                if (bidAmount > bidPricePartial && bidAmount != null && bidAmount >= bidPricePartial5 && bidAmount <= bidPricePartial25) {

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





}
