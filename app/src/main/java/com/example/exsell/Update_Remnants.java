package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.exsell.Adapter.StringUploadPicAdapter;
import com.example.exsell.Adapter.UploadPicAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Update_Remnants extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UPDATE REMNANT";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String remnantId;

    //PAGE 1
    private EditText editTextTitle, editTextDescription, editTextBackStory, editTextBounceBack;

    //PAGE 2
    private RadioGroup radioGroup;
    private LinearLayout linearLayoutPriceUpdate, linearLayoutAuction;

    //AUCTION
    private EditText editTextAuctionPrice, editTextEndTime, editTextListTime, editTextAuctionEndTime;

    //FIXED PRICE
    private EditText editTextQuantity, editTextBreakUpPrice;


    //MEETUP AND CATEGORY
    private EditText editTextMeetUp, editTextCategory;

    private long currentTimeMills, currentTimeSeconds;
    private long idleDurationMills, idleDurationSeconds;
    private RelativeLayout auction_relativeAvailTime;
    private LinearLayout linearLayoutDate;
    private String categoryId;
    private RadioGroup radioGroupFormat;
    private RadioButton radioBtnFixedPrice, radioBtnAuction;
    private int radioSelectedId;
    private Button updateBtn;
    private String subCategoryId, subCategoryName;
    private String radioBtnData;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String stringStartTime = "Start Immediately";
    private SimpleDateFormat df = new SimpleDateFormat(" MM/dd/yy h:mm a");
    String[] durationDays = new String[] {"3 days","5 days","7 days","10 days"};
    private ArrayList<String> listImageUrl = new ArrayList<>();

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_remnants_activity);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.update_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("UPDATE REMNANT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        remnantId = getIntent().getStringExtra("remnantId");

        linearLayoutPriceUpdate = findViewById(R.id.update_linearPrice);
        linearLayoutAuction =  findViewById(R.id.update_linearAuction);



        //PAGE 1
         recyclerView = findViewById(R.id.update_recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        editTextTitle = findViewById(R.id.update_title);
        editTextDescription = findViewById(R.id.update_description);
        editTextBackStory = findViewById(R.id.update_backStory);
        editTextBounceBack = findViewById(R.id.update_bounceBack);

        //AUCTION
        editTextAuctionPrice = findViewById(R.id.update_auctionStartPrice);
        editTextAuctionEndTime = findViewById(R.id.update_auctionEndTime);
        editTextListTime = findViewById(R.id.update_auctionListTime);

        //FIXED PRICE
        editTextBreakUpPrice = findViewById(R.id.update_breakUpPrice);
        editTextQuantity = findViewById(R.id.update_quantity);

        //MEETUP AND CATEGORY
        editTextMeetUp = findViewById(R.id.update_meetup);
        editTextCategory = findViewById(R.id.update_select_category);


        //INITIALIZE THE SDK
        Places.initialize(getApplicationContext(), "AIzaSyCtPPdDFjHfJ0FWCSpm1OBKWq2HA_UtQLg");
        PlacesClient placesClient  = Places.createClient(this);

        updateBtn = findViewById(R.id.update_listit_btn);



        editTextMeetUp.setOnClickListener(this);
        editTextCategory.setOnClickListener(this);
        updateBtn.setOnClickListener(this);


        //IDLE DURATION
        AutoCompleteTextView editTextFilledExposedDropdownTime = findViewById(R.id.update_filled_exposed_dropdownIdleDuration);
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(Update_Remnants.this, R.layout.dropdown_menu_popup_item, durationDays);
        editTextFilledExposedDropdownTime.setAdapter(adapterTime);

        editTextFilledExposedDropdownTime.setOnItemClickListener((parent, view, position, id) -> {

            String time = adapterTime.getItem(position);
            switch (time){
                case "3 days":


                    idleDurationMills = System.currentTimeMillis() + 86400000 * 3;
                    idleDurationSeconds = idleDurationMills / 1000;

                    currentTimeMills = currentTimeMills + 86400000 * 3;
                    currentTimeSeconds = currentTimeMills / 1000;

                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextListTime.setText(df.format(idleDurationMills));
                    break;

                case "5 days":

                    idleDurationMills = System.currentTimeMillis() + 86400000 * 5;
                    idleDurationSeconds = idleDurationMills / 1000;

                    currentTimeMills = currentTimeMills + 86400000 * 5;
                    currentTimeSeconds = currentTimeMills / 1000;

                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextListTime.setText(df.format(idleDurationMills));
                    break;

                case "7 days":

                    idleDurationMills = System.currentTimeMillis() + 86400000 * 7;
                    idleDurationSeconds = idleDurationMills / 1000;

                    currentTimeMills = currentTimeMills + 86400000 * 7;
                    currentTimeSeconds = currentTimeMills / 1000;

                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextListTime.setText(df.format(idleDurationMills));
                    break;

                case "10 days":

                    idleDurationMills = System.currentTimeMillis() + 86400000 * 10;
                    idleDurationSeconds = idleDurationMills / 1000;

                    currentTimeMills = currentTimeMills + 86400000 * 10;
                    currentTimeSeconds = currentTimeMills / 1000;

                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextListTime.setText(df.format(idleDurationMills));
                    break;
            }


        });


        //DURATION
        AutoCompleteTextView editTextFilledExposedDropdownDuration = findViewById(R.id.update_filled_exposed_dropdownDuration);
        editTextFilledExposedDropdownDuration.setText("3 days");
        currentTimeMills = System.currentTimeMillis() + 86400000 * 3;
        currentTimeSeconds = currentTimeMills / 1000;
        editTextAuctionEndTime.setText(df.format(currentTimeMills));


        ArrayAdapter<String> adapterDuration = new ArrayAdapter<>(this,R.layout.dropdown_menu_popup_item, durationDays);
        editTextFilledExposedDropdownDuration.setAdapter(adapterDuration);

        editTextFilledExposedDropdownDuration.setOnItemClickListener((parent, view, position, id) -> {

            String durationDays = adapterDuration.getItem(position);
            switch (durationDays){
                case "3 days":

                    currentTimeMills = System.currentTimeMillis() + 86400000 * 3;
                    currentTimeSeconds = currentTimeMills / 1000;
                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextFilledExposedDropdownTime.setText("");
                    editTextListTime.setText("");

                    break;

                case "5 days":

                    currentTimeMills = System.currentTimeMillis() + 86400000 * 5;
                    currentTimeSeconds = currentTimeMills / 1000;
                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextFilledExposedDropdownTime.setText("");
                    editTextListTime.setText("");
                    break;

                case "7 days":

                    currentTimeMills = System.currentTimeMillis() + 86400000 * 7;
                    currentTimeSeconds = currentTimeMills / 1000;
                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextFilledExposedDropdownTime.setText("");
                    editTextListTime.setText("");
                    break;

                case "10 days":

                    currentTimeMills = System.currentTimeMillis() + 86400000 * 10;
                    currentTimeSeconds = currentTimeMills / 1000;
                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextFilledExposedDropdownTime.setText("");
                    editTextListTime.setText("");
                    break;
            }
        });

        //START TIME
        String[] startTime = new String[] {"Start Immediately", "Schedule Start Time"};
        AutoCompleteTextView editTextFilledExposedDropdownStartTime = (AutoCompleteTextView) findViewById(R.id.update_filled_exposed_dropdownStartTime);
        editTextFilledExposedDropdownStartTime.setText("Start Immediately");
        ArrayAdapter<String> adapterStartTime = new ArrayAdapter<>(Update_Remnants.this,R.layout.dropdown_menu_popup_item, startTime);
        editTextFilledExposedDropdownStartTime.setAdapter(adapterStartTime);

        editTextFilledExposedDropdownStartTime.setOnItemClickListener((parent, view, position, id) -> {

            String startTime1 = "Start Immediately";
            startTime1 = adapterStartTime.getItem(position);

            switch (startTime1){

                case "Start Immediately":

                    linearLayoutDate.setVisibility(View.GONE);
                    editTextAuctionEndTime.setText(df.format(currentTimeMills));
                    editTextFilledExposedDropdownDuration.setText("");
                    editTextListTime.setText("");
                    editTextAuctionEndTime.setText("");
                    idleDurationMills = 0;
                    idleDurationSeconds = 0;
                    currentTimeMills = 0;
                    currentTimeSeconds = 0;
                    stringStartTime = "Start Immediately";
                    break;

                case "Schedule Start Time":

                    linearLayoutDate.setVisibility(View.VISIBLE);
                    editTextFilledExposedDropdownDuration.setText("");
                    editTextAuctionEndTime.setText("");
                    stringStartTime = "Schedule Start Time";
                    break;
            }
        });


        //RADIO BUTTON
        radioGroupFormat = findViewById(R.id.update_radigroup_format);
        radioBtnFixedPrice = findViewById(R.id.update_fixedprice_radioBtn);
        radioBtnAuction = findViewById(R.id.update_auction_radioBtn);

        radioGroupFormat.setOnCheckedChangeListener((group, checkedId) -> {

            if(checkedId == R.id.update_fixedprice_radioBtn){

                linearLayoutPriceUpdate.setVisibility(View.VISIBLE);
                linearLayoutAuction.setVisibility(View.GONE);
                radioBtnData = "fixedPriceSelected";

            }else{

               // disclaimerDialog();
                linearLayoutAuction.setVisibility(View.VISIBLE);
                linearLayoutPriceUpdate.setVisibility(View.GONE);
                radioBtnData = "auctionSelected";
            }
        });


        fetchData();
    }

    private void fetchData() {

        firebaseFirestore.collection("remnants").document(remnantId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();

                editTextTitle.setText(documentSnapshot.getString("title"));
                editTextDescription.setText(documentSnapshot.getString("description"));
                editTextBackStory.setText(documentSnapshot.getString("backStory"));
                editTextBounceBack.setText(documentSnapshot.getString("bounceBack"));
                editTextMeetUp.setText(documentSnapshot.getString("meetup"));
                categoryId = documentSnapshot.getString("categoryId");

                listImageUrl = (ArrayList<String>) documentSnapshot.get("imageUrl");


                recyclerView.setAdapter(new StringUploadPicAdapter(getApplicationContext(), listImageUrl));
               // recyclerView.setAdapter(new UploadPicAdapter(getApplicationContext()), listImageUrl);


                //FETCH DATA
                firebaseFirestore.collection("category").document(categoryId)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot1 = task.getResult();

                            editTextCategory.setText(documentSnapshot1.getString("categoryName"));
                        }

                    }
                });

                if(documentSnapshot.getString("type").equals("Fixed Price")){

                    radioGroupFormat.check(R.id.update_fixedprice_radioBtn);

                    editTextBreakUpPrice.setText(String.valueOf(documentSnapshot.getDouble("price")));
                    editTextQuantity.setText(String.valueOf(documentSnapshot.getLong("quantity").intValue()));

                }else{

                    radioGroupFormat.check(R.id.update_auction_radioBtn);

                    editTextAuctionPrice.setText(String.valueOf(documentSnapshot.getDouble("price")));

                }

            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.update_select_category:

                Intent categoryIntent = new Intent(Update_Remnants.this, Select_Category.class);
                startActivity(categoryIntent);

                Intent subCategoryIntent = new Intent();

                startActivityForResult(categoryIntent, 1024);

                break;

            case R.id.update_meetup:

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("PH")
                        .build(Update_Remnants.this);

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;


            case R.id.update_listit_btn:

               // Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show();
                if(radioBtnData == "fixedPriceSelected"){

                    double price = Double.parseDouble(editTextBreakUpPrice.getText().toString());
                    int quantity  = Integer.parseInt(editTextQuantity.getText().toString());
                    String meetup = editTextMeetUp.getText().toString().trim();
                    String title = editTextTitle.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    String backStory = editTextBackStory.getText().toString().trim();
                    String bounceBack = editTextBounceBack.getText().toString().trim();


                    //FIXED PRICE
                    HashMap<String, Object> fixedPriceRemnants = new HashMap<>();
                    fixedPriceRemnants.put("imageUrl", listImageUrl);
                    fixedPriceRemnants.put("title", title);
                    fixedPriceRemnants.put("description", description);
                    fixedPriceRemnants.put("backStory", backStory);
                    fixedPriceRemnants.put("bounceBack", bounceBack);
                    fixedPriceRemnants.put("price", price);
                    fixedPriceRemnants.put("quantity", quantity);
                    fixedPriceRemnants.put("meetup", meetup);
                    fixedPriceRemnants.put("categoryId", categoryId);
                    fixedPriceRemnants.put("userId", mAuth.getCurrentUser().getUid());
                    fixedPriceRemnants.put("report", 0);
                    fixedPriceRemnants.put("type", "Fixed Price");
                    fixedPriceRemnants.put("isSoldOut", false);
                    fixedPriceRemnants.put("isDeleted", false);
                    fixedPriceRemnants.put("isExpired", false);
                    fixedPriceRemnants.put("isBanned", false);
                    fixedPriceRemnants.put("featuredDuration",0);
                    fixedPriceRemnants.put("isActive", true);
                    fixedPriceRemnants.put("isFeatured", false);
                    fixedPriceRemnants.put("timeStamp", FieldValue.serverTimestamp());
                    //END FIXED PRICE

                    firebaseFirestore.collection("remnants").document(remnantId)
                            .set(fixedPriceRemnants)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(Update_Remnants.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    Intent toDashboard1 = new Intent(Update_Remnants.this, Dashboard.class);
                                    startActivity(toDashboard1);
                                }
                            });


                }else{

                    String meetup = editTextMeetUp.getText().toString().trim();
                    String title = editTextTitle.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    String backStory = editTextBackStory.getText().toString().trim();
                    String bounceBack = editTextBounceBack.getText().toString().trim();
                    double auctionStartPrice = Double.parseDouble(editTextAuctionPrice.getText().toString());

                    //AUCTION
                    HashMap<String, Object> auctionRemnants = new HashMap<>();
                    auctionRemnants.put("imageUrl", listImageUrl);
                    auctionRemnants.put("title", title);
                    auctionRemnants.put("description", description);
                    auctionRemnants.put("backStory", backStory);
                    auctionRemnants.put("bounceBack", bounceBack);
                    auctionRemnants.put("price", auctionStartPrice);
                    auctionRemnants.put("endTime", currentTimeMills);
                    // auctionRemnants.put("endTime", System.currentTimeMillis());
                    auctionRemnants.put("meetup", meetup);
                    auctionRemnants.put("categoryId", categoryId);
                    auctionRemnants.put("userId", mAuth.getCurrentUser().getUid());
                    auctionRemnants.put("type","Auction");
                    auctionRemnants.put("startTime", stringStartTime);
                    auctionRemnants.put("report", 0);
                    auctionRemnants.put("isSoldOut", false);
                    auctionRemnants.put("isDeleted", false);
                    auctionRemnants.put("isBanned", false);
                    auctionRemnants.put("isActive", true);
                    auctionRemnants.put("isFeatured", false);
                    auctionRemnants.put("featuredDuration", 0);
                    auctionRemnants.put("isExpired", false);
                    auctionRemnants.put("idleDuration", idleDurationMills);
                    auctionRemnants.put("timeStamp", FieldValue.serverTimestamp());
                    //ENF AUCTION

                    firebaseFirestore.collection("remnants").document(remnantId)
                            .set(auctionRemnants)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(Update_Remnants.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    Intent toDashboard = new Intent(Update_Remnants.this, Dashboard.class);
                                    startActivity(toDashboard);
                                }
                            });
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                String meetup = place.getName();
                editTextMeetUp.setText(meetup);


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

            }
        }else if(requestCode == 1024){

            if (resultCode == RESULT_OK){



                subCategoryId = data.getStringExtra("subCategoryId");
                subCategoryName = data.getStringExtra("subCategoryName");
                editTextCategory.setText(subCategoryName);
            }
        }else{

        }

    }




}
