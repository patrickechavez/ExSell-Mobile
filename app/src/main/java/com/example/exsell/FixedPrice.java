package com.example.exsell;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.Models.UsersModel;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FixedPrice extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView textViewTitle, textViewDescription,  textViewBackStory, textViewBounceBack, textViewPrice, textViewQuantity,  textViewMeetup, textViewOwner;
    private String remnantId, owner_id, user_id;
    private CircleImageView userCircleImageView;
    private CarouselView carouselView;
    private List<String> stringImageUrl;
    private TextView moreDetailsTextView;
    private ExpandableRelativeLayout expandableRelativeLayout;
    private FixedPriceModel fixedPriceModel;
    private UsersModel usersModel;
    private Button fp1addCartButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixedprice_activity);

        Toolbar toolbar = findViewById(R.id.fixedprice_toolbar);
        setSupportActionBar(toolbar);
      //  toolbar.setBackgroundColor(Color.parseColor());

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        remnantId = getIntent().getStringExtra("remnantId");

        textViewTitle = findViewById(R.id.fp1_title);
        textViewDescription = findViewById(R.id.fp1_description);
        textViewBackStory = findViewById(R.id.backStoryTextView);
        textViewBounceBack = findViewById(R.id.bounceBackTextView);
        textViewPrice = findViewById(R.id.fp1_price);
        textViewQuantity = findViewById(R.id.fp1_quantity);
        textViewMeetup = findViewById(R.id.fp1_meetup);
        userCircleImageView = findViewById(R.id.fp1_circleImageView);
        textViewOwner = findViewById(R.id.fp1_ownerTextView);
        carouselView = findViewById(R.id.carouselView);
        fp1addCartButton = findViewById(R.id.fp1_addtocartbtn);

        moreDetailsTextView = findViewById(R.id.moreDetailsTextView);

        user_id = mAuth.getCurrentUser().getUid();

        fp1addCartButton.setOnClickListener(this);

        DocumentReference docRef = firebaseFirestore.collection("fixedPriceRemnants").document(remnantId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                 fixedPriceModel = documentSnapshot.toObject(FixedPriceModel.class);

                textViewTitle.setText(fixedPriceModel.getTitle());
                textViewDescription.setText(fixedPriceModel.getDescription());
                textViewBackStory.setText(fixedPriceModel.getBackStory());
                textViewBounceBack.setText(fixedPriceModel.getBounceBack());
                textViewPrice.setText("₱" +fixedPriceModel.getPrice());
                textViewQuantity.setText(fixedPriceModel.getQuantity());
                textViewMeetup.setText(fixedPriceModel.getMeetup());
                 owner_id = fixedPriceModel.getUserId();
                 stringImageUrl  = (ArrayList<String>) fixedPriceModel.getRemnantsPicUrl();

                Log.d("FIXED PRICE", ""+stringImageUrl);


                 //OWNER OF THE ITEM
                DocumentReference docRef1 = firebaseFirestore.collection("users").document(owner_id);
                docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                         usersModel = documentSnapshot.toObject(UsersModel.class);

                           textViewOwner.setText(usersModel.getFirstName());
                        Picasso.get().load(usersModel.getImageUrl()).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).into(userCircleImageView);
                    }
                });

                carouselView.setImageListener(imageListener);
                carouselView.setPageCount(stringImageUrl.size());
            }


        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            for(String s: stringImageUrl)
            Picasso.get().load(stringImageUrl.get(position)).into(imageView);
        }
    };

    public void showMoreDetails(View view){

        expandableRelativeLayout = findViewById(R.id.expandableLinearLayout);
        expandableRelativeLayout.toggle();
    }


    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.fp1_addtocartbtn) {

            String owner = textViewOwner.getText().toString().trim();
            String title = textViewTitle.getText().toString().trim();
            String price = textViewPrice.getText().toString().trim();
            String imageUrl = fixedPriceModel.getRemnantsPicUrl().get(0).toString();


            HashMap<String, Object> cartData = new HashMap<>();
            cartData.put("owner", usersModel.getFirstName());
            cartData.put("title", fixedPriceModel.getTitle());
            cartData.put("price", fixedPriceModel.getPrice());
            cartData.put("imageUrl", fixedPriceModel.getRemnantsPicUrl().get(0).toString());

            firebaseFirestore.collection("cart").document(user_id).collection("product").document(remnantId)
                    .set(cartData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(FixedPrice.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(FixedPrice.this, "Cart Error: Error wring document", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}
