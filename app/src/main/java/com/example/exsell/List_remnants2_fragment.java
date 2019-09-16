package com.example.exsell;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class List_remnants2_fragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "remnants2";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private RecyclerView recyclerView;


    private LinearLayoutManager linearLayoutManager;
    private String user_id;

    //RADIO BUTTON
    private LinearLayout linearPrice, linearAuction;
    private EditText editTextSelectCategory, editTextMeetup;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth mAuth;
    private Button listitBtn;
    private RadioGroup radioGroupFormat;
    private RadioButton radioBtnFixedPrice, radioBtnAuction;
    private int radioSelectedId;
    private String categoryId, categoryName;
    private EditText editTextBreakUpPrice, editTextBreakUpQuantity;
    private EditText editTextAuctionPrice, editTextAuctionDuration, editTextAuctionEndTime, editTextListTime;
    private ArrayList<String> listImageUrl = new ArrayList<>();
    private String title, description, backStory, bounceBack;
    private List<Uri> listOfPic;
    private AutoCompleteTextView autoCompleteTextView;
    private String radioBtnData = "fixedPriceSelected";
    private int count = 0;
    private int count2= 0;
    private ProgressBar progressBar;
    private View view;
    private SimpleDateFormat df = new SimpleDateFormat(" MM/dd/yy h:mm a");
    private String stringStartTime = "Start Immediately";
    private long currentTimeMills, currentTimeSeconds;
    private long idleDurationSeconds, idleDurationMills;
    private RelativeLayout auction_relativeAvailTime;

    String[] durationDays = new String[] {"3 days","5 days","7 days","10 days"};
    private LinearLayout linearLayoutDate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.list_remnants2_fragment, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        linearPrice =  view.findViewById(R.id.linearPrice);
        linearAuction =  view.findViewById(R.id.linearAuction);

        //PROGRESS BAR
        progressBar = view.findViewById(R.id.lm2_progressBar);

        //FIXED PRICE
        editTextBreakUpPrice = view.findViewById(R.id.breakUpPrice);
        editTextBreakUpQuantity = view.findViewById(R.id.lm2_quantity);

        //LINEARLAYOUT
        linearLayoutDate = view.findViewById(R.id.linearlayout_startTime);

        //AUCTION
        editTextAuctionPrice =  view.findViewById(R.id.lm2_auctionStartPrice);
        editTextAuctionEndTime = view.findViewById(R.id.lm2_auctionEndTime);
        editTextListTime = view.findViewById(R.id.lm2_auctionListTime);

        //RELATIVE LAYOUT
        auction_relativeAvailTime = view.findViewById(R.id.auction_relativeAvailTime);



        //IDLE DURATION
        AutoCompleteTextView editTextFilledExposedDropdownTime = view.findViewById(R.id.filled_exposed_dropdownIdleDuration);
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, durationDays);
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
        AutoCompleteTextView editTextFilledExposedDropdownDuration = view.findViewById(R.id.filled_exposed_dropdownDuration);
        editTextFilledExposedDropdownDuration.setText("3 days");
        currentTimeMills = System.currentTimeMillis() + 86400000 * 3;
        currentTimeSeconds = currentTimeMills / 1000;
        editTextAuctionEndTime.setText(df.format(currentTimeMills));


        ArrayAdapter<String> adapterDuration = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item, durationDays);
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
        AutoCompleteTextView editTextFilledExposedDropdownStartTime = (AutoCompleteTextView) view.findViewById(R.id.filled_exposed_dropdownStartTime);
        editTextFilledExposedDropdownStartTime.setText("Start Immediately");
        ArrayAdapter<String> adapterStartTime = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item, startTime);
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

        listOfPic = getArguments().getParcelableArrayList("listOfPic");
        Log.d(TAG,""+listOfPic);
        title = getArguments().getString("title");
        description = getArguments().getString("description");
        backStory = getArguments().getString("backStory");
        bounceBack = getArguments().getString("bounceBack");

        //INITIALIZE THE SDK
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyCtPPdDFjHfJ0FWCSpm1OBKWq2HA_UtQLg");
        PlacesClient placesClient  = Places.createClient(getActivity());

        Toast.makeText(getContext(), ""+title, Toast.LENGTH_SHORT).show();
        //SELECT CATEGORY EDITTEXT
        editTextSelectCategory = view.findViewById(R.id.lm_select_category);
        editTextSelectCategory.setOnClickListener(this);
        //MEETUP EDITTEXT
        editTextMeetup =  view.findViewById(R.id.lm_meetup);
        editTextMeetup.setOnClickListener(this);
        //LIST THE DATA
        listitBtn =  view.findViewById(R.id.listit_btn);
        listitBtn.setOnClickListener(this);


        //RADIO BUTTON
        radioGroupFormat = view.findViewById(R.id.radigroup_format);
        radioBtnFixedPrice = view.findViewById(R.id.fixedprice_radioBtn);
        radioBtnAuction = view.findViewById(R.id.auction_radioBtn);

        radioGroupFormat.setOnCheckedChangeListener((group, checkedId) -> {

            if(checkedId == R.id.fixedprice_radioBtn){

                linearAuction.setVisibility(View.GONE);
                linearPrice.setVisibility(View.VISIBLE);
                radioBtnData = "fixedPriceSelected";

            }else{

                disclaimerDialog();
                linearAuction.setVisibility(View.VISIBLE);
                linearPrice.setVisibility(View.GONE);
                radioBtnData = "auctionSelected";
            }
        });

        return view;
    }

    private void disclaimerDialog() {

        new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Disclaimer")
                .setMessage("Items must be collectors items and relationship remnants")
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                String meetup = place.getName();
                editTextMeetup.setText(meetup);


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
            }
        }else if(requestCode == 1023){

            if (resultCode == RESULT_OK){

                categoryId = data.getStringExtra("categoryId");
                categoryName = data.getStringExtra("categoryName");
                editTextSelectCategory.setText(categoryName);
            }
        }else{

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.lm_select_category:

                Intent categoryIntent = new Intent(getContext(), Select_Category.class);
                startActivity(categoryIntent);

                startActivityForResult(categoryIntent, 1023);
                break;

            case R.id.lm_meetup:

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("PH")
                        .build(getActivity());

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;

            case R.id.listit_btn:

                progressBar.setVisibility(View.VISIBLE);
                if(radioBtnData == "fixedPriceSelected"){

                    double price = Double.parseDouble(editTextBreakUpPrice.getText().toString());
                    int quantity  = Integer.parseInt(editTextBreakUpQuantity.getText().toString());
                    String meetup = editTextMeetup.getText().toString().trim();

                            for(Uri pic: listOfPic) {

                                StorageReference storageReference = firebaseStorage.getReference().child("product_Images").child(pic.toString());
                                UploadTask uploadTask = (UploadTask) storageReference.putFile(pic);

                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(!task.isSuccessful()){

                                            throw task.getException();
                                        }
                                        return storageReference.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(task -> {

                                    if(task.isSuccessful()){

                                        String urlString = task.getResult().toString();
                                        listImageUrl.add(urlString);
                                        count2++;

                                        Log.d(TAG, " HAHAH listURl: "+listImageUrl);


                                    }
                                    else{

                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                                    }


                                    if(count2 == listOfPic.size()){

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
                                        fixedPriceRemnants.put("userId", user_id);
                                        fixedPriceRemnants.put("report", 0);
                                        fixedPriceRemnants.put("type", "Fixed Price");
                                        fixedPriceRemnants.put("isSoldOut", false);
                                        fixedPriceRemnants.put("isDeleted", false);
                                        fixedPriceRemnants.put("isBanned", false);
                                        fixedPriceRemnants.put("isActive", true);
                                        fixedPriceRemnants.put("isFeatured", false);
                                        fixedPriceRemnants.put("timeStamp", FieldValue.serverTimestamp());


                                        firebaseFirestore.collection("remnants").add(fixedPriceRemnants)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if(task.isSuccessful()){

                                                            Toast.makeText(getContext(), "Fixed Price Added Successfully", Toast.LENGTH_SHORT).show();
                                                            Intent fixedPrice = new Intent(getContext(), Dashboard.class);
                                                            startActivity(fixedPrice);
                                                        }
                                                        else{
                                                            Toast.makeText(getContext(), "Error fixed PRice", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                }else{

                    double auctionStartPrice = Double.parseDouble(editTextAuctionPrice.getText().toString());
                    String meetup = editTextMeetup.getText().toString().trim();

                    for(Uri pic: listOfPic) {

                        StorageReference storageReference = firebaseStorage.getReference().child("auction_images").child(pic.toString());
                        UploadTask uploadTask = (UploadTask) storageReference.putFile(pic);

                        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                            if(!task.isSuccessful()){

                                throw task.getException();
                            }
                            return storageReference.getDownloadUrl();
                        }).addOnCompleteListener(task -> {

                            if(task.isSuccessful()){

                                String urlString = task.getResult().toString();
                                listImageUrl.add(urlString);
                                count++;

                                Log.d(TAG, " HAHAH listURl: "+listImageUrl);

                            }
                            else{

                                String error = task.getException().getMessage();
                                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                            }

                            if(count == listOfPic.size()){

                                HashMap<String, Object> auctionRemnants = new HashMap<>();
                                auctionRemnants.put("imageUrl", listImageUrl);
                                auctionRemnants.put("title", title);
                                auctionRemnants.put("description", description);
                                auctionRemnants.put("backStory", backStory);
                                auctionRemnants.put("bounceBack", bounceBack);
                                auctionRemnants.put("price", auctionStartPrice);
                                auctionRemnants.put("endTime", currentTimeSeconds);
                               // auctionRemnants.put("endTime", System.currentTimeMillis());
                                auctionRemnants.put("meetup", meetup);
                                auctionRemnants.put("categoryId", categoryId);
                                auctionRemnants.put("userId", user_id);
                                auctionRemnants.put("type","Auction");
                                auctionRemnants.put("startTime", stringStartTime);
                                auctionRemnants.put("report", 0);
                                auctionRemnants.put("isSoldOut", false);
                                auctionRemnants.put("isDeleted", false);
                                auctionRemnants.put("isBanned", false);
                                auctionRemnants.put("isActive", true);
                                auctionRemnants.put("isFeatured", false);
                                auctionRemnants.put("isFeatureDuration", 0);
                                auctionRemnants.put("isExpired", false);
                                auctionRemnants.put("idleDuration", idleDurationSeconds);
                                auctionRemnants.put("timeStamp", FieldValue.serverTimestamp());

                                DocumentReference remnants = firebaseFirestore.collection("remnants").document();
                                String remnants_id = remnants.getId();

                                firebaseFirestore.collection("remnants").document(remnants_id)
                                        .set(auctionRemnants)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


                                                Intent dashboard = new Intent(getActivity(), Dashboard.class);
                                                startActivity(dashboard);

                                                if(stringStartTime == "Schedule Start Time"){

                                                    firebaseFirestore.collection("users")
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                    if(task.isSuccessful()){

                                                                        for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                                                                            if(!documentSnapshot.getId().equals(mAuth.getCurrentUser().getUid())){

                                                                                String message = "There's an upcoming auction";
                                                                                String message2 = "Check it Now";

                                                                                HashMap<String, Object> notifData = new HashMap<>();
                                                                                notifData.put("sender_id", mAuth.getCurrentUser().getUid());
                                                                                notifData.put("receiver_id", documentSnapshot.getId());
                                                                                notifData.put("remnants_id", remnants_id);
                                                                                notifData.put("message", message);
                                                                                notifData.put("message2", message2);
                                                                                notifData.put("imageUrl", "https://i.ibb.co/XWLptv0/auction.png");
                                                                                notifData.put("notificationType", "upcomingBid");
                                                                                notifData.put("timeStamp", FieldValue.serverTimestamp());

                                                                                firebaseFirestore.collection("notification")
                                                                                        .add(notifData)
                                                                                        .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()));
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }else{
                                                }
                                            }
                                        });
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
                break;
        }
    }
}

