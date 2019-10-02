package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Models.FixedPriceModel;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Featured extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String remnantId;
    private CarouselView carouselView;
    private TextView textViewPrice, textViewTitle, textViewDescription;
    private Button button1day, button3day,  button5day;
    private FixedPriceModel fixedPriceModel;
    private List<String> stringList;
    private Double wallet;
    private SimpleDateFormat df = new SimpleDateFormat("h:mm a, MMM d yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.featured_activity);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Toolbar toolbar = findViewById(R.id.featuredRemnant_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FEATURED REMNANT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        remnantId = getIntent().getStringExtra("remnantId");

        carouselView = findViewById(R.id.featured_fixedPricecarouselView);
      //  textViewPrice = findViewById(R.id.featured_price);
        textViewTitle = findViewById(R.id.featured_title);
        textViewDescription = findViewById(R.id.featured_description);
        //textViewFeaturedDurationEndTime = findViewById(R.id.)

        button1day = findViewById(R.id.featured_1day);
        button5day = findViewById(R.id.featured_5day);


        button1day.setOnClickListener(this);
        button5day.setOnClickListener(this);

        fetchRemnant();
        getWallet();

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            for(String s: stringList)
                Picasso.get().load(stringList.get(position)).into(imageView);
        }
    };



    private void getWallet() {

        DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();
                    wallet = document.getDouble("wallet");

                }
            }
        });
    }

    private void fetchRemnant() {

        DocumentReference docRef = firebaseFirestore.collection("remnants").document(remnantId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();

                    stringList = (List<String>) document.get("imageUrl");
                    textViewTitle.setText(document.getString("title"));
                    textViewDescription.setText(document.getString("description"));

                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(stringList.size());

                }
            }
        });
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.featured_1day:

                openDialog1();
                break;

            case R.id.featured_5day:

                openDialog2();

                break;
        }
    }

    private void openDialog1() {

        new MaterialAlertDialogBuilder(this, R.style.ShapeAppearance_MaterialComponents_LargeComponent)
                .setTitle("Do you want to proceed?")
                .setPositiveButton("PROCEED", (dialog, which) -> {

                    /*Calendar cal1 = Calendar.getInstance();
                    cal1.add(Calendar.MINUTE, 1);
                    String currentDate1 = df.format(cal1.getTime());*/
                    long day1 = System.currentTimeMillis() + 86400000;

                    Map<String, Object> data = new HashMap<>();
                    data.put("featuredDay", 1);
                    data.put("featuredDuration", day1);
                    data.put("isFeatured", true);

                    firebaseFirestore.collection("remnants").document(remnantId).set(data, SetOptions.merge());

                    DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
                    docRef.update("wallet", FieldValue.increment(-40));


                    Map<String, Object> data1 = new HashMap<>();
                    data1.put("boostingFee", 40);
                    data1.put("senderUser_id", mAuth.getCurrentUser().getUid());
                    data1.put("type", "featuredRemnant");

                    firebaseFirestore.collection("generateReport").add(data1)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent i = new Intent(Featured.this, Dashboard.class);
                                    startActivity(i);

                                    Toast.makeText(Featured.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(Featured.this, "Failed", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                })
                .show();
    }

    private void openDialog2() {

        new MaterialAlertDialogBuilder(this, R.style.ShapeAppearance_MaterialComponents_LargeComponent)
                .setTitle("Do you want to proceed?")
                .setPositiveButton("PROCEED", (dialog, which) -> {


                   // long day1 = (System.currentTimeMillis() + (60000 * 5)) / 1000;
                    long day3 = System.currentTimeMillis() + 86400000 * 3;

                    Map<String, Object> data = new HashMap<>();
                    data.put("featuredDay", 3);
                    data.put("featuredDuration", day3);
                    data.put("isFeatured", true);

                    firebaseFirestore.collection("remnants").document(remnantId).set(data, SetOptions.merge());

                    DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
                    docRef.update("wallet", FieldValue.increment(-100));


                    Map<String, Object> data1 = new HashMap<>();
                    data1.put("transactionFee", 100);
                    data1.put("senderUser_id", mAuth.getCurrentUser().getUid());
                    data1.put("type", "Featured Remnant");

                    firebaseFirestore.collection("generateReport").add(data1)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent i = new Intent(Featured.this, Dashboard.class);
                                    startActivity(i);

                                    Toast.makeText(Featured.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Featured.this, "Failed", Toast.LENGTH_SHORT).show();

                                }
                            });

                })
                .setNegativeButton("CANCEL", (dialog, which) -> {

                   /* DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
                    docRef.update("wallet", FieldValue.increment(-80));

                    Intent i = new Intent(Featured.this, Dashboard.class);
                    startActivity(i);*/
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
