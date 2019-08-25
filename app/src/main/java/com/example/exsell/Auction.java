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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;
import static com.example.exsell.R.drawable.toolbar_transparent;

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
    private Button buttonPlaceBid;
    private String startPrice , bidPricePartial;
    private static final String TAG = "AUCTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_activity);

       /* Toolbar toolbar = findViewById(R.id.auction_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(toolbar_transparent);*/

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

        editTextbidAmount = findViewById(R.id.auction_bidAmount);
        buttonPlaceBid = findViewById(R.id.auction_placeBtn);
        editTextbidAmount.addTextChangedListener(bidTextWatcher);

        buttonPlaceBid.setOnClickListener(this);

        DocumentReference docRef = firebaseFirestore.collection("auctionRemnants").document(auctionId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

               auctionModel = documentSnapshot.toObject(AuctionModel.class);

               textViewTitle.setText(auctionModel.getTitle());
               textViewDescription.setText(auctionModel.getDescription());
               textViewBackStory.setText(auctionModel.getBackStory());
               textViewBounceBack.setText(auctionModel.getBounceBack());
              // textViewBidPrice.setText("₱ "+auctionModel.getStartPrice());
               textViewStartPrice.setText("₱ "+auctionModel.getStartPrice());
               startPrice = auctionModel.getStartPrice();
               textViewMeetup.setText(auctionModel.getMeetup());
                textViewEndtime.setText(auctionModel.getEndTime());
                owner_id = auctionModel.getUserId();
               stringImageUrl = auctionModel.getAuctionImageUrl();

               Log.d(TAG, "startPrice: "+startPrice);


               if(user_id.equals(owner_id)){

                   editTextbidAmount.setVisibility(View.GONE);
                   buttonPlaceBid.setVisibility(View.GONE);
               }


               //FETCH LATEST BID PRICE
                firebaseFirestore.collection("auctionRemnants").document(auctionId)
                        .collection("bidders")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()) {

                            if(task.getResult().size() > 0){

                                Log.d(TAG, "success startPrice: "+startPrice);

                                // Log.d(TAG, "haha: false");
                        Query query = firebaseFirestore.collection("auctionRemnants").document(auctionId)
                                .collection("bidders").orderBy("bidAmount").limit(1);

                        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            private static final String TAG = "AUCTION";

                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                                    bidPricePartial = queryDocumentSnapshot.getString("bidAmount");
                                    textViewBidPrice.setText("₱ " + bidPricePartial);
                                }
                            }
                        });

                            }else{

                                Log.d(TAG, "failed startPrice: "+startPrice);

                                bidPricePartial = startPrice;
                                textViewBidPrice.setText("₱ "+bidPricePartial);
                            }
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

        String bidAmount = editTextbidAmount.getText().toString().trim();
        String bidPrice = textViewBidPrice.getText().toString().trim();

       /* Toast.makeText(this, "bidAmount: "+bidAmount+ " bidPrice: "+bidPrice, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "bidAmount: "+bidAmount, Toast.LENGTH_SHORT).show();*/
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

    }

    private TextWatcher bidTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String bidAmount = editTextbidAmount.getText().toString().trim();
            String bidPrice = bidPricePartial.toString().trim();



            try {

               // Toast.makeText(Auction.this, "bidPrice:"+Integer.parseInt(bidPrice), Toast.LENGTH_SHORT).show();
                if (Integer.parseInt(bidAmount) > Integer.parseInt(bidPrice)  && !bidAmount.isEmpty()) {

                    buttonPlaceBid.setEnabled(true);
                    buttonPlaceBid.setBackgroundColor(getResources().getColor(colorPrimaryDark));
                } else {
                    buttonPlaceBid.setEnabled(false);
                    buttonPlaceBid.setBackgroundColor(getResources().getColor(buttonDisabled));
                }
            }catch (NumberFormatException e){

                Toast.makeText(Auction.this, "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}
