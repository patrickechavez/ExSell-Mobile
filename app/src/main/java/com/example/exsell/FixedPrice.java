package com.example.exsell;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.exsell.Adapter.CommentAdapter;
import com.example.exsell.Models.CommentModel;
import com.example.exsell.Models.FixedPriceModel;
import com.example.exsell.Models.UsersModel;
import com.example.exsell.fragment.UserProfile_auction_fragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;

public class FixedPrice extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "FIXED PRICE";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView textViewTitle, textViewDescription, textViewBackStory, textViewBounceBack, textViewPrice, textViewQuantity,  textViewMeetup, textViewOwner, fp_endDurationTextView;
    private String remnantId, owner_id, user_id;
    private CircleImageView userCircleImageView;
    private CarouselView carouselView;
    private List<String> stringImageUrl;
    private TextView moreDetailsTextView;
    private ExpandableRelativeLayout expandableRelativeLayout;
    private FixedPriceModel fixedPriceModel;
    private UsersModel usersModel;
    private Button fp1addCartButton, fp1viewCartButton;
    private Double currentTotal , price, wallet;
    private RelativeLayout relativeLayoutLayout, featured_relativelayout;
    private EditText editTextWriteComment;
    private Button buttonAddComment;
    private CommentAdapter commentAdapter;
    private SimpleDateFormat df = new SimpleDateFormat(" MM/dd/yy h:mm a");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixedprice_activity);

        Toolbar toolbar = findViewById(R.id.fixedprice_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



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
        carouselView = findViewById(R.id.fixedPricecarouselView);
        fp1addCartButton = findViewById(R.id.fp1_addtocartbtn);
        fp1viewCartButton = findViewById(R.id.fp1_viewOnCart);
        moreDetailsTextView = findViewById(R.id.moreDetailsTextView);

        featured_relativelayout = findViewById(R.id.featured_relativelayout);
        fp_endDurationTextView = findViewById(R.id.fp_endDurationTextView);

        //COMMENT SECTION
        editTextWriteComment = findViewById(R.id.fp_commentDetail);
        buttonAddComment = findViewById(R.id.fp_commentButton);

        editTextWriteComment.addTextChangedListener(addComment);
        setUpRecyclerView();

        fp1addCartButton.setOnClickListener(this);
        fp1viewCartButton.setOnClickListener(this);
        textViewOwner.setOnClickListener(this);
        buttonAddComment.setOnClickListener(this);

        checkifDeleted(); //CHECK IF DELETED

        DocumentReference docRef = firebaseFirestore.collection("remnants").document(remnantId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                 fixedPriceModel = documentSnapshot.toObject(FixedPriceModel.class);

                textViewTitle.setText(fixedPriceModel.getTitle());
                textViewDescription.setText(fixedPriceModel.getDescription());
                textViewBackStory.setText(fixedPriceModel.getBackStory());
                textViewBounceBack.setText(fixedPriceModel.getBounceBack());
                price = fixedPriceModel.getPrice();
                textViewPrice.setText("₱" +price);
                textViewQuantity.setText(String.valueOf(fixedPriceModel.getQuantity()));
                textViewMeetup.setText(fixedPriceModel.getMeetup());
                 owner_id = fixedPriceModel.getUserId();
                 stringImageUrl  = (ArrayList<String>) fixedPriceModel.getImageUrl();



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


                //CHECK THE THE USER BALANCE
                DocumentReference docRefBalance = firebaseFirestore.collection("users").document(owner_id);
                docRefBalance.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();

                            wallet = document.getDouble("wallet");

                            if(wallet < 25){

                                firebaseFirestore.collection("remnants")
                                        .whereEqualTo("userId", owner_id)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                                                    String remnants_id = documentSnapshot.getId();

                                                    DocumentReference docRefRemnants = firebaseFirestore.collection("remnants").document(remnants_id);
                                                    docRefRemnants.update("isActive", false);
                                                }
                                            }
                                        });
                            }else{

                                firebaseFirestore.collection("remnants")
                                        .whereEqualTo("userId", owner_id)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                                                    String remnants_id = documentSnapshot.getId();

                                                    DocumentReference docRefRemnants = firebaseFirestore.collection("remnants").document(remnants_id);
                                                    docRefRemnants.update("isActive", true);
                                                }
                                            }
                                        });
                            }

                        }
                    }
                });
                //CHECK IF THE FEATURE IS STILL EXIST

                if(mAuth.getCurrentUser().getUid().equals(owner_id)){


              //      Toast.makeText(FixedPrice.this, "OWNER", Toast.LENGTH_SHORT).show();

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            while (!isInterrupted()) {
                                try {
                                    Thread.sleep(1000);

                                    runOnUiThread(() -> {
                                       // Log.d(TAG, "sud "+ System.currentTimeMillis() / 1000);

                                        DocumentReference docRef = firebaseFirestore.collection("remnants").document(remnantId);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if(task.isSuccessful()){
                                                    DocumentSnapshot document = task.getResult();


                                                    if(document.getLong("featuredDuration") != 0){

                                                        long featuredDuration = document.getLong("featuredDuration");
                                                        String featuredDurationMs = df.format(featuredDuration);


                                                        if((System.currentTimeMillis() / 1000) <= featuredDuration / 1000){

                                                            featured_relativelayout.setVisibility(View.VISIBLE);
                                                            fp_endDurationTextView.setText(featuredDurationMs);


                                                            Log.d(TAG, "haha currentTime: "+System.currentTimeMillis() / 1000);
                                                            Log.d(TAG, "HAHA endTime:     " + featuredDuration /1000);

                                                        }else{

                                                            featured_relativelayout.setVisibility(View.GONE);
                                                            DocumentReference docRef = firebaseFirestore.collection("remnants").document(remnantId);
                                                            docRef.update("isFeatured", false
                                                                              , "featuredDuration", 0);

                                                            Log.d(TAG,"haha endTimes : "+featuredDuration / 1000);
                                                        }

                                                    }else{



                                                    }

                                                }

                                            }
                                        });
                                    });


                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };

                    thread.start();

                }
                //END CHECK IF THE FEATURE IS STILL EXIST
            }


        });

       // Log.d(TAG, "CURRENT USER: "+mAuth.getCurrentUser().getUid()+ "OWNER ID: "+owner_id);

        isThisMyItem();
        isRemnantExistOnCart();
       // checkIfUserHasBalance();
        addCurrentTotal();
        checkIfStillFeatured();
        //showFeaturedEndTime();

    }

    private void setUpRecyclerView() {

        Query query = firebaseFirestore.collection("remnants").document(remnantId)
                .collection("comment")
                .orderBy("timeStamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<CommentModel> options = new FirestoreRecyclerOptions.Builder<CommentModel>()
                .setQuery(query, CommentModel.class)
                .build();


        commentAdapter = new CommentAdapter(options, this);


        RecyclerView recyclerView = findViewById(R.id.fp_commentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {

                recyclerView.smoothScrollToPosition(0);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void checkIfStillFeatured() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        DocumentReference docRef1 = firebaseFirestore.collection("remnants").document(remnantId);
        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(mAuth.getCurrentUser().getUid().equals(documentSnapshot.getString("userId"))){

                        firebaseFirestore.collection("remnants").document(remnantId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){

                                    DocumentSnapshot documentSnapshot1 = task.getResult();
                                    if(!documentSnapshot1.getBoolean("isExpired") && documentSnapshot1.getString("type").equals("Fixed Price")){

                                        menu.findItem(R.id.promote_remnant).setEnabled(true);
                                    }else{

                                        menu.findItem(R.id.promote_remnant).setEnabled(false);
                                    }

                                    if(documentSnapshot1.getLong("quantity") == 0){

                                        menu.findItem(R.id.soldOut_remnant).setEnabled(true);
                                    }else{

                                        menu.findItem(R.id.soldOut_remnant).setEnabled(false);
                                    }
                                }
                            }
                        });

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

        switch (item.getItemId()){

            case R.id.promote_remnant:

                //Toast.makeText(this, "Promote Remnant", Toast.LENGTH_SHORT).show();
                Intent promote = new Intent(FixedPrice.this, Featured.class);
                promote.putExtra("remnantId", remnantId);
                startActivity(promote);
                break;

            case R.id.edit_remnant:

                Intent toUpdate = new Intent(FixedPrice.this, Update_Remnants.class);
                toUpdate.putExtra("remnantId", remnantId);
                startActivity(toUpdate);

                //Toast.makeText(this, "Edit Remnant", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_remnant:

                firebaseFirestore.collection("cart").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    for(QueryDocumentSnapshot documentSnapshot :task.getResult()){

                                        String user_id = documentSnapshot.getId();

                                        DocumentReference documentReference = firebaseFirestore.collection("cart").document(user_id)
                                                .collection("remnants").document(remnantId);

                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                DocumentSnapshot documentSnapshot1 = task.getResult();
                                                if(documentSnapshot1.exists()){

                                                    //FETCH DATA FROM CART -> REMNANT -> REMNANT ID AND SUBTOTAL
                                                    DocumentReference documentReference1 = firebaseFirestore.collection("cart").document(user_id)
                                                            .collection("remnants").document(remnantId);

                                                    documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                            if(task.isSuccessful()) {

                                                                DocumentSnapshot documentSnapshot2 = task.getResult();
                                                                Double subTotal = documentSnapshot2.getDouble("subTotal");
                                                                String buyerId = documentSnapshot2.getString("buyerId");


                                                                Log.d(TAG, "subTotal: "+subTotal+ "buyerId: "+buyerId );
                                                                DocumentReference documentReference2 = firebaseFirestore.collection("cart").document(buyerId);
                                                                documentReference2.update("total", FieldValue.increment(-subTotal)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        Log.d(TAG, "haha minus total");

                                                                        //DELETE REMNANT FROM CART
                                                                        firebaseFirestore.collection("cart").document(user_id)
                                                                                .collection("remnants").document(remnantId)
                                                                                .delete()
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        Toast.makeText(FixedPrice.this, "delete Remnant", Toast.LENGTH_SHORT).show();
                                                                                        Log.d(TAG, "haha delete remnant from cart");
                                                                                    }
                                                                                });

                                                                        //DELETE REMNANT FROM LIST
                                                                        DocumentReference docRef1 = firebaseFirestore.collection("remnants").document(remnantId);

                                                                        docRef1.update("isDeleted", true)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        Toast.makeText(FixedPrice.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                                                        Intent i = new Intent(FixedPrice.this, Dashboard.class);
                                                                                        startActivity(i);
                                                                                    }
                                                                                });
                                                                    }
                                                                });


                                                            }

                                                        }
                                                    });

                                                }else{

                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        });
                //DELETE REMNANT FROM LIST

                DocumentReference docRef = firebaseFirestore.collection("remnants").document(remnantId);

                docRef.update("isDeleted", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(FixedPrice.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(FixedPrice.this, Dashboard.class);
                                startActivity(i);
                            }
                        });

                break;

            //REPORT ITEM
            case R.id.reportRemnant:

               // Toast.makeText(this, "report Remnant", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FixedPrice.this, ReportRemnants.class);
                i.putExtra("seller_id", owner_id);
                i.putExtra("remnant_id", remnantId);
                startActivity(i);

             //   Toast.makeText(this, "report Remnant" + remnantId, Toast.LENGTH_SHORT).show();
                break;

            case R.id.soldOut_remnant:

                firebaseFirestore.collection("remnants").document(remnantId)
                        .update("isSoldOut", true,
                                    "isExpired", true);

                Toast.makeText(this, "Moved to Sold Remnants", Toast.LENGTH_SHORT).show();
                Intent toActivity = new Intent(FixedPrice.this, Activity.class);
                startActivity(toActivity);

                break;

        }
        return super.onOptionsItemSelected(item);
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


    public void addCurrentTotal(){

        DocumentReference docRef = firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        DocumentSnapshot documentSnapshot = task.getResult();
                            if(!documentSnapshot.exists()){


                                currentTotal = 25.00;

                            }else{

                                currentTotal =  documentSnapshot.getDouble("total");
                            }
                    }
                }
            });
    }
    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.fp1_addtocartbtn:

               DocumentReference docref10  = firebaseFirestore.collection("remnants").document(remnantId);
               docref10.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                       if(task.isSuccessful()){

                           DocumentSnapshot document = task.getResult();

                           if(document.getBoolean("isActive") == false){

                               Toast.makeText(FixedPrice.this, "The seller has no transaction fee", Toast.LENGTH_SHORT).show();

                           }else{

                               String ownerName = textViewOwner.getText().toString().trim();
                               String title = textViewTitle.getText().toString().trim();

                               HashMap<String, Object> totalData = new HashMap<>();
                               totalData.put("total", price + currentTotal);

                               HashMap<String, Object> cartData = new HashMap<>();

                               cartData.put("remnantId", remnantId);
                               cartData.put("quantity", 1);
                               cartData.put("timeStamp", FieldValue.serverTimestamp());
                               cartData.put("buyerId", mAuth.getCurrentUser().getUid());
                               cartData.put("subTotal", fixedPriceModel.getPrice());




                               firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid()).set(totalData)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {

                                               firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid())
                                                       .collection("remnants").document(remnantId)
                                                       .set(cartData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {

                                                       Toast.makeText(FixedPrice.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                                                       fp1addCartButton.setVisibility(View.GONE);
                                                       fp1viewCartButton.setVisibility(View.VISIBLE);
                                                   }
                                               });
                                           }
                                       });
                           }
                       }
                   }
               });

                break;

            case R.id.fp1_viewOnCart:

                Intent toCart = new Intent(FixedPrice.this, Cart.class);
                startActivity(toCart);
                break;

            case R.id.fp1_ownerTextView:

                if(!mAuth.getCurrentUser().getUid().equals(owner_id)) {

                    Intent i = new Intent(FixedPrice.this, UserProfile.class);
                    i.putExtra("seller_id", owner_id);
                    // Toast.makeText(this, ""+owner_id, Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
                break;

            case R.id.fp_commentButton:

                String comment = editTextWriteComment.getText().toString().trim();

                DocumentReference remnants = firebaseFirestore.collection("blogs").document();
                String documentId = remnants.getId();

                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("userId", mAuth.getCurrentUser().getUid());
                commentMap.put("comment", comment);
                commentMap.put("remnantId",remnantId);
                commentMap.put("timeStamp", FieldValue.serverTimestamp());


                firebaseFirestore.collection("remnants").document(remnantId).collection("comment")
                        .document(documentId).set(commentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(FixedPrice.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                editTextWriteComment.setText("");
                buttonAddComment.setEnabled(false);
                buttonAddComment.setBackgroundColor(getResources().getColor(buttonDisabled));

                break;
        }

    }

    public void isRemnantExistOnCart(){

        firebaseFirestore.collection("cart").document(mAuth.getCurrentUser().getUid()).collection("remnants")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                        if(remnantId.equals(documentSnapshot.getId())) {
                            Log.d(TAG, "CURRENT REMNANT ID: " + remnantId + " REMNANT ID: " + documentSnapshot.getId());
                            fp1addCartButton.setVisibility(View.GONE);
                            fp1viewCartButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    public void isThisMyItem(){

       firebaseFirestore.collection("remnants").whereEqualTo(FieldPath.documentId(), remnantId)
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                       if(task.isSuccessful()){

                           for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                               if(mAuth.getCurrentUser().getUid().equals(documentSnapshot.getString("userId"))){

                                   fp1addCartButton.setVisibility(View.GONE);
                               }

                           }
                       }
                   }
               });
    }

    private void checkifDeleted(){

        firebaseFirestore.collection("remnants").document(remnantId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.getBoolean("isDeleted")){

                    Intent i = new Intent(FixedPrice.this, Dashboard.class);
                    startActivity(i);
                }
            }
        });

    }

    public TextWatcher addComment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String comment = editTextWriteComment.getText().toString().trim();
            if(!comment.equals(null) || !comment.equals("")){

                buttonAddComment.setEnabled(true);
                buttonAddComment.setBackgroundColor(getResources().getColor(colorPrimaryDark));
            }else{

                buttonAddComment.setEnabled(false);
                buttonAddComment.setBackgroundColor(getResources().getColor(buttonDisabled));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();


        commentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        commentAdapter.stopListening();
    }
}
