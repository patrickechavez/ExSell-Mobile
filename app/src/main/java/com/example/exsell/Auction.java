package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Adapter.AuctionAdapter;
import com.example.exsell.Models.AuctionModel;
import com.example.exsell.Models.UsersModel;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;
import static com.example.exsell.R.drawable.toolbar_transparent;
import static com.example.exsell.R.drawable.wallet;

public class Auction extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView textViewTitle, textViewDescription, textViewBackStory, textViewBounceBack, textViewBidPrice, textViewStartPrice, textViewMeetup, textViewOwner, textViewMoreDetails, textViewEndtime;
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
    private double wallet, bidPricePartial;
    private String firstName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity);

        Toolbar toolbar = findViewById(R.id.auction_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

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

        buttonPlaceBid.setOnClickListener(this);
        buttonBidders.setOnClickListener(this);




        //GET THE VALUE OF WALLET FROM THE USER
        DocumentReference docWallet = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
        docWallet.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                     wallet = document.getDouble("wallet");
                     firstName = document.getString("firstName");
                }
            }
        });

        DocumentReference docRef = firebaseFirestore.collection("auctionRemnants").document(auctionId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

               auctionModel = documentSnapshot.toObject(AuctionModel.class);

               textViewTitle.setText(auctionModel.getTitle());
               textViewDescription.setText(auctionModel.getDescription());
               textViewBackStory.setText(auctionModel.getBackStory());
               textViewBounceBack.setText(auctionModel.getBounceBack());
                double startPrice = auctionModel.getStartPrice();
               textViewStartPrice.setText("₱ "+startPrice);
               textViewMeetup.setText(auctionModel.getMeetup());
                textViewEndtime.setText(auctionModel.getEndTime());
                owner_id = auctionModel.getUserId();
               stringImageUrl = auctionModel.getAuctionImageUrl();



               if(user_id.equals(owner_id)){

                   editTextbidAmount.setVisibility(View.GONE);
                   buttonPlaceBid.setVisibility(View.GONE);
                   buttonBidders.setVisibility(View.VISIBLE);
                   toolbar.setVisibility(View.VISIBLE);
               }


               //FETCH LATEST BID PRICE
                firebaseFirestore.collection("auctionRemnants").document(auctionId)
                        .collection("bidders")
                        .get().addOnCompleteListener(task -> {

                            if(task.isSuccessful()) {

                                if(task.getResult().size() > 0){

                                    Log.d(TAG, "success startPrice: "+startPrice);

                                   firebaseFirestore.collection("auctionRemnants").document(auctionId)
                                           .collection("bidders").orderBy("bidAmount").limit(1)
                                           .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                               @Override
                                               public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                                   if (e != null) {
                                                       Log.w(TAG, "Listen failed.", e);
                                                       return;
                                                   }

                                                   for(QueryDocumentSnapshot doc: queryDocumentSnapshots){

                                                       if(doc.get("bidAmount") != null){

                                                           bidPricePartial = doc.getDouble("bidAmount");
                                                           textViewBidPrice.setText("₱ " + bidPricePartial);
                                                       }
                                                   }

                                               }
                                           });
                                }else{

                                    Log.d(TAG, "failed startPrice: "+startPrice);

                                    bidPricePartial = startPrice;
                                    textViewBidPrice.setText("₱ "+bidPricePartial);
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

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.auction_toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.promote_remnant:

                Toast.makeText(this, "Promote Remnant", Toast.LENGTH_SHORT).show();
                break;

            case R.id.edit_remnant:

                Toast.makeText(this, "Edit Remnant", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_remnant:

                Toast.makeText(this, "Delete Remnant", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMoreDetails(View view){

        expandableRelativeLayout = findViewById(R.id.auction_expandableLinearLayout);
        expandableRelativeLayout.toggle();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            for(String s: stringImageUrl)
                Picasso.get().load(stringImageUrl.get(position)).into(imageView);

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.auction_placeBtn:

                double bidAmount = Double.parseDouble(editTextbidAmount.getText().toString());
                if(wallet < bidAmount){

                    editTextbidAmount.setError("Insufficient Balance");

                }else {

                    Toast.makeText(this, "Bidding Successful", Toast.LENGTH_SHORT).show();
                    //SEND TO AUCTION REMNANTS BIDDER
                    Map<String, Object> bidAmountMap = new HashMap<>();
                    bidAmountMap.put("bidAmount", bidAmount);
                    firebaseFirestore.collection("auctionRemnants").document(auctionId).collection("bidders")
                            .document(mAuth.getCurrentUser().getUid()).set(bidAmountMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

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
                    bidNotification.put("notificationType", "auction");
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

        try{
            Double bidAmount = Double.parseDouble(editTextbidAmount.getText().toString());
            Double bidPricePartial5 = bidPricePartial * 0.05 + bidPricePartial;
            Double bidPricePartial25 = bidPricePartial * 0.25 + bidPricePartial;

                if (bidAmount > bidPricePartial && bidAmount != null && bidAmount >= bidPricePartial5 && bidAmount <= bidPricePartial25){

                    buttonPlaceBid.setEnabled(true);
                    buttonPlaceBid.setBackgroundColor(getResources().getColor(colorPrimaryDark));
                }
                else{
                    buttonPlaceBid.setEnabled(false);
                    buttonPlaceBid.setBackgroundColor(getResources().getColor(buttonDisabled));
                    editTextbidAmount.setError("You must input "+bidPricePartial5+" above and "+bidPricePartial25+" below");
                }

            }catch (NumberFormatException e){
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}
