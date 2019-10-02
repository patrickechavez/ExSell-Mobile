package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReportRemnants extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private RadioGroup radioGroupReportRemnants;
    private EditText editTextReportRemnants;
    private TextInputLayout textInputLayoutGone;
    private Button buttonsendFeedBack;
    private String reasonData = "This is spam";
    private String seller_id, remnant_id, remanant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_remnants_activity);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        radioGroupReportRemnants = findViewById(R.id.radioGroup_reportRemnant);
        textInputLayoutGone = findViewById(R.id.texinputLayoutGone);
        editTextReportRemnants = findViewById(R.id.report_others);
        buttonsendFeedBack = findViewById(R.id.button_remnantsButtonSendFeedBack);


        //FETCH REMNANT NAME
        seller_id = getIntent().getStringExtra("seller_id");
        remnant_id = getIntent().getStringExtra("remnant_id");

        fetchRemnantName();

        radioGroupReportRemnants.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId){

                    case R.id.radioBtn_Spam:

                        reasonData = "This is spam";
                        textInputLayoutGone.setVisibility(View.GONE);
                      //  Toast.makeText(ReportRemnants.this, ""+reasonData, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.radioBtn_notRealItem:

                        textInputLayoutGone.setVisibility(View.GONE);
                        reasonData = "This is not a real item for sale";
                       // Toast.makeText(ReportRemnants.this, ""+reasonData, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.radioBtn_harmfulIllegal:

                        textInputLayoutGone.setVisibility(View.GONE);
                        reasonData = "This post is abusive, harmful or illegal";
                       // Toast.makeText(ReportRemnants.this, ""+reasonData, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.radioBtn_fraudScam:

                        textInputLayoutGone.setVisibility(View.GONE);
                        reasonData = "This is fraud or scam";
                      //  Toast.makeText(ReportRemnants.this, ""+reasonData, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.radioBtn_adultProduct:


                        textInputLayoutGone.setVisibility(View.GONE);
                        reasonData = "It is inappropriate or has aadult products";
                        break;

                    case R.id.radioBtn_others:

                        editTextReportRemnants.setText("");
                        textInputLayoutGone.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

        buttonsendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editTextReportRemnants.getText().toString().trim().equals("")) {

                    String reason = editTextReportRemnants.getText().toString().trim();


                    //STORE REPORTLIST DATA
                    Map<String, Object> reportData1 = new HashMap<>();
                    reportData1.put("buyer_id", mAuth.getCurrentUser().getUid());
                    reportData1.put("reason", reason);
                    reportData1.put("type", "reportRemnant");
                    reportData1.put("remnant_id", remnant_id);
                    reportData1.put("seller_id", seller_id);
                    reportData1.put("timeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("ReportList")
                            .add(reportData1)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(ReportRemnants.this, "Reported Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{

                    //STORE REPORTLIST DATA
                    Map<String, Object> reportData2 = new HashMap<>();
                    reportData2.put("buyer_id", mAuth.getCurrentUser().getUid());
                    reportData2.put("reason", reasonData);
                    reportData2.put("type", "reportRemnant");
                    reportData2.put("remnant_id", remnant_id);
                    reportData2.put("seller_id", seller_id);
                    reportData2.put("timeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("ReportList")
                            .add(reportData2)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(ReportRemnants.this, "Sending Report Successful", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
//SOMEONE MARK YOUR REMNANT AS INAPPROPRIATE AGAINST OUR COMMUNITY STANDARD

                String message = "Someone marked your remnant as inappropriate against our Community Standard";
                //SENDING REPORT REMNANT NOTIFICATION
                Map<String, Object> notifData = new HashMap<>();
                notifData.put("sender_id", mAuth.getCurrentUser().getUid());
                notifData.put("remnants_id", remnant_id);
                notifData.put("receiver_id", seller_id);
                notifData.put("message", message);
                notifData.put("imageUrl", "https://i.ibb.co/P1XBmJZ/report-Notification.png");
                notifData.put("notificationType", "reportRemnant");
                notifData.put("timeStamp", FieldValue.serverTimestamp());


                firebaseFirestore.collection("notification")
                        .add(notifData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                      //          Toast.makeText(ReportRemnants.this, "Sending Notif SUccessful", Toast.LENGTH_SHORT).show();
                            }
                        });

                firebaseFirestore.collection("ReportList")
                        .add(notifData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                          //      Toast.makeText(ReportRemnants.this, "Sending ReportList Successfuls", Toast.LENGTH_SHORT).show();
                            }
                        });


                //UPDATE REMNANT REPORTS COUNT
                DocumentReference docRef= firebaseFirestore.collection("remnants").document(remnant_id);
                docRef.update("report", FieldValue.increment(1));


                //SEND TO DASHBOARD
                finish();


            }
        });

    }

    private void fetchRemnantName() {

        DocumentReference docRef = firebaseFirestore.collection("remnants").document(remnant_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();
                    remanant_name = documentSnapshot.getString("title");
                }
            }
        });

    }


}
