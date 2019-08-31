package com.example.exsell;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    private EditText editTextAuctionPrice, editTextAuctionDuration, editTextAuctionEndTime;
    private ArrayList<String> listImageUrl = new ArrayList<>();
    private String title, description, backStory, bounceBack;
    private List<Uri> listOfPic;
    private AutoCompleteTextView autoCompleteTextView;
    private String radioBtnData = "fixedPriceSelected";
    private int count = 0;
    private int count2= 0;
    private ProgressBar progressBar;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.list_remnants2_fragment, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        linearPrice = (LinearLayout) view.findViewById(R.id.linearPrice);
        linearAuction = (LinearLayout) view.findViewById(R.id.linearAuction);

        //PROGRESS BAR
        progressBar = view.findViewById(R.id.lm2_progressBar);

        //FIXED PRICE
        editTextBreakUpPrice = (EditText) view.findViewById(R.id.breakUpPrice);
        editTextBreakUpQuantity = (EditText) view.findViewById(R.id.lm2_quantity);


        //AUCTION
        editTextAuctionPrice = (EditText) view.findViewById(R.id.lm2_auctionStartPrice);
        editTextAuctionEndTime = (EditText) view.findViewById(R.id.lm2_auctionEndTime);

        String[] durationDays = new String[] {"3 days","5 days","7 days","12 days"};

        AutoCompleteTextView editTextFilledExposedDropdown = (AutoCompleteTextView) view.findViewById(R.id.filled_exposed_dropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item, durationDays);
        editTextFilledExposedDropdown.setAdapter(adapter);


        SimpleDateFormat df = new SimpleDateFormat("h:mm a, MMM d yyyy");

       // String currentTime = df.format(cal.getTime());

        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String durationDays = adapter.getItem(position).toString();
                switch (durationDays){
                    case "3 days":

                        Calendar cal3 = Calendar.getInstance();
                        cal3.add(Calendar.DAY_OF_MONTH, 3);
                        String currentDate3 = df.format(cal3.getTime());

                        editTextAuctionEndTime.setText(currentDate3);
                        break;

                    case "5 days":

                        Calendar cal5 = Calendar.getInstance();
                        cal5.add(Calendar.DAY_OF_MONTH, 5);
                        String currentDate5 = df.format(cal5.getTime());

                        editTextAuctionEndTime.setText(currentDate5);
                        break;

                    case "7 days":

                        Calendar cal7 = Calendar.getInstance();
                        cal7.add(Calendar.DAY_OF_MONTH, 7);
                        String currentDate7 = df.format(cal7.getTime());

                        editTextAuctionEndTime.setText(currentDate7);
                        break;

                    case "12 days":

                        Calendar cal12 = Calendar.getInstance();
                        cal12.add(Calendar.DAY_OF_MONTH, 12);
                        String currentDate12 = df.format(cal12.getTime());
                        editTextAuctionEndTime.setText(currentDate12);

                        break;
                }
            }
        });

        //GETTNG DATA FROM LIST REMNANTS 1
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
        editTextSelectCategory = (EditText) view.findViewById(R.id.lm_select_category);
        editTextSelectCategory.setOnClickListener(this);
        //MEETUP EDITTEXT
        editTextMeetup = (EditText) view.findViewById(R.id.lm_meetup);
        editTextMeetup.setOnClickListener(this);
        //LIST THE DATA
        listitBtn = (Button) view.findViewById(R.id.listit_btn);
        listitBtn.setOnClickListener(this);






        //RADIO BUTTON
        radioGroupFormat = (RadioGroup) view.findViewById(R.id.radigroup_format);
        radioBtnFixedPrice = (RadioButton) view.findViewById(R.id.fixedprice_radioBtn);
        radioBtnAuction = (RadioButton) view.findViewById(R.id.auction_radioBtn);

        radioGroupFormat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.fixedprice_radioBtn){

                    linearAuction.setVisibility(View.GONE);
                    linearPrice.setVisibility(View.VISIBLE);
                    radioBtnData = "fixedPriceSelected";
                }else{
                    linearAuction.setVisibility(View.VISIBLE);
                    linearPrice.setVisibility(View.GONE);
                    radioBtnData = "auctionSelected";

                }
            }
        });




        return view;
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
        }else if(requestCode == 1010){

            if (resultCode == RESULT_OK){
                String categoryId = data.getStringExtra("categoryId");
                String categoryName = data.getStringExtra("categoryName");

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

                startActivityForResult(categoryIntent, 1010);
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
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

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
                                            fixedPriceRemnants.put("remnantsPicUrl", listImageUrl);
                                            fixedPriceRemnants.put("title", title);
                                            fixedPriceRemnants.put("description", description);
                                            fixedPriceRemnants.put("backStory", backStory);
                                            fixedPriceRemnants.put("bounceBack", bounceBack);
                                            fixedPriceRemnants.put("price", price);
                                            fixedPriceRemnants.put("quantity", quantity);
                                            fixedPriceRemnants.put("meetup", meetup);
                                            fixedPriceRemnants.put("timeStamp", FieldValue.serverTimestamp());
                                            fixedPriceRemnants.put("categoryId", categoryId);
                                            fixedPriceRemnants.put("userId", user_id);
                                            fixedPriceRemnants.put("report", 0);
                                            fixedPriceRemnants.put("status", "active");

                                            firebaseFirestore.collection("fixedPriceRemnants").add(fixedPriceRemnants)
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
                                    }
                                });
                            }
                }else{

                    double auctionStartPrice = Double.parseDouble(editTextAuctionPrice.getText().toString());
                    String auctionEndTime = editTextAuctionEndTime.getText().toString();
                    String meetup = editTextMeetup.getText().toString().trim();

                    for(Uri pic: listOfPic) {

                        StorageReference storageReference = firebaseStorage.getReference().child("auction_images").child(pic.toString());
                        UploadTask uploadTask = (UploadTask) storageReference.putFile(pic);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){

                                    throw task.getException();
                                }
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

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
                                    auctionRemnants.put("auctionImageUrl", listImageUrl);
                                    auctionRemnants.put("title", title);
                                    auctionRemnants.put("description", description);
                                    auctionRemnants.put("backStory", backStory);
                                    auctionRemnants.put("bounceBack", bounceBack);
                                    auctionRemnants.put("startPrice", auctionStartPrice);
                                    auctionRemnants.put("endTime", auctionEndTime);
                                    auctionRemnants.put("meetup", meetup);
                                    auctionRemnants.put("timeStamp", FieldValue.serverTimestamp());
                                    auctionRemnants.put("categoryId", categoryId);
                                    auctionRemnants.put("userId", user_id);
                                    auctionRemnants.put("report", 0);
                                    auctionRemnants.put("active", "active");

                                    firebaseFirestore.collection("auctionRemnants").add(auctionRemnants).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "SUCCESSFULLY ADDED");


                                                Intent dashboard = new Intent(getContext(), Dashboard.class);
                                                startActivity(dashboard);

                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "ERROR: "+error);
                                            }

                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
                break;
        }

    }
}
