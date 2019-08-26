package com.example.exsell;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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


public class List_remnants2_fragment extends Fragment {

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

    private View view;
    private String imageString;

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
        Calendar cal = Calendar.getInstance();
        String currentTime = df.format(Calendar.getInstance().getTime());


        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String durationDays = adapter.getItem(position).toString();
                switch (durationDays){
                    case "3 days":

                        cal.add(Calendar.DAY_OF_MONTH, 3);
                        editTextAuctionEndTime.setText(df.format(cal.getTime()).toString());
                        break;

                    case "5 days":

                        cal.add(Calendar.DAY_OF_MONTH, 5);
                        editTextAuctionEndTime.setText(df.format(cal.getTime()).toString());
                        break;

                    case "7 days":

                        cal.add(Calendar.DAY_OF_MONTH, 7);
                        editTextAuctionEndTime.setText(df.format(cal.getTime()).toString());
                        break;

                    case "12 days":

                        cal.add(Calendar.DAY_OF_MONTH, 12);
                        editTextAuctionEndTime.setText(df.format(cal.getTime()).toString());
                        break;
                }
            }
        });

        //GETTNG DATA FROM LIST REMNANTS 1
        listOfPic = getArguments().getParcelableArrayList("listOfPic");
        Log.d(TAG,""+listOfPic);
        title = getArguments().getString("title");
        Toast.makeText(getActivity(), ""+title, Toast.LENGTH_SHORT).show();
        description = getArguments().getString("description");
        backStory = getArguments().getString("backStory");
        bounceBack = getArguments().getString("bounceBack");

        //INITIALIZE THE SDK
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyCtPPdDFjHfJ0FWCSpm1OBKWq2HA_UtQLg");
        PlacesClient placesClient  = Places.createClient(getActivity());


        //SELECT CATEGORY EDITTEXT
        editTextSelectCategory = (EditText) view.findViewById(R.id.lm_select_category);
        editTextSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putParcelableArrayList("listOfPic", (ArrayList<? extends Parcelable>) listOfPic);
                b.putString("title", title);
                b.putString("description", description);
                b.putString("backStory", backStory);
                b.putString("bounceBack", bounceBack);

                b.putString("price",editTextBreakUpPrice.getText().toString().trim());
                b.putString("quantity", editTextBreakUpQuantity.getText().toString().trim());
                b.putString("meetup", editTextMeetup.getText().toString().trim());

                Fragment lm_category  = new List_remnants_category_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                lm_category.setArguments(b);
                transaction.replace(R.id.list_remnants_container, lm_category);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        //DATA FROM SELECT CATEGORY
       if(getArguments()!= null){

           listOfPic = getArguments().getParcelableArrayList("listOfPic");
           title = getArguments().getString("title");
           description = getArguments().getString("description");
           backStory = getArguments().getString("backStory");
           bounceBack = getArguments().getString("bounceBack");

           categoryId = getArguments().getString("id");
           editTextBreakUpPrice.setText(getArguments().getString("price"));
           editTextBreakUpQuantity.setText(getArguments().getString("quantity"));
           editTextSelectCategory.setText(getArguments().getString("categoryName"));
       }

        //MEETUP EDITTEXT
        editTextMeetup = (EditText) view.findViewById(R.id.lm_meetup);
        editTextMeetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);


                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("PH")
                        .build(getActivity());



                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });


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


        //LIST THE DATA
        listitBtn = (Button) view.findViewById(R.id.listit_btn);
        listitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radioBtnData == "fixedPriceSelected"){

                    String price = editTextBreakUpPrice.getText().toString().trim();
                    String quantity  = editTextBreakUpQuantity.getText().toString().trim();
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

                                                                Toast.makeText(getContext(), "Fixed Succesfully Added", Toast.LENGTH_SHORT).show();
                                                                Intent fixedPrice = new Intent(getContext(), Dashboard.class);
                                                                startActivity(fixedPrice);
                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                });
                            }
                }else{

                    String auctionStartPrice = editTextAuctionPrice.getText().toString().trim();
                    String auctionEndTime = editTextAuctionEndTime.getText().toString().trim();
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
                                        }
                                    });


                                }
                            }
                        });
                    }
                }
            }//END OF ONCLICK
        }); //END OF SETONCLICK LISTENER
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

               // Toast.makeText(getActivity(), ""+place.getName(), Toast.LENGTH_SHORT).show();
                String meetup = place.getName();
                //String placeId = place.
                //editTextMeetup.setText(meetup);

                //Toast.makeText(getContext(), "placeId: "+placeId, Toast.LENGTH_SHORT).show();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



}
