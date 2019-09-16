package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
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

public class ReportUsers extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private RadioGroup radioGroupReportUsers;
    private EditText editTextReportUsers;
    private TextInputLayout textInputLayoutUserGone;
    private Button buttonSendUserFeedback;
    private String reasonData = "Bogus Buyer/Seller";
    private String seller_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_users_activity);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        seller_id = getIntent().getStringExtra("seller_id");

        radioGroupReportUsers = findViewById(R.id.radioGroup_reportUser);
        editTextReportUsers = findViewById(R.id.user_report_others);
        textInputLayoutUserGone = findViewById(R.id.user_texinputLayoutGone);
        buttonSendUserFeedback = findViewById(R.id.user_button_remnantsButtonSendFeedBack);

        radioGroupReportUsers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId){

                    case R.id.user_radioBtn_bogusBuyer:

                        textInputLayoutUserGone.setVisibility(View.GONE);
                        reasonData = "Bogus Buyer/Seller";
                        Toast.makeText(ReportUsers.this, ""+reasonData, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.user_radioBtn_postingThings:

                        textInputLayoutUserGone.setVisibility(View.GONE);
                        reasonData = "Posting Inappropriate Things";
                        Toast.makeText(ReportUsers.this, ""+reasonData, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.user_radioBtn_others:

                        editTextReportUsers.setText("");
                        textInputLayoutUserGone.setVisibility(View.VISIBLE);
                        Toast.makeText(ReportUsers.this, "Others", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        buttonSendUserFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!editTextReportUsers.getText().toString().trim().equals("")){

                    String reason = editTextReportUsers.getText().toString().trim();

                    //STORE REPORT LIST COLLECTION
                    Map<String, Object> reportUser1 = new HashMap<>();
                    reportUser1.put("buyer_id", mAuth.getCurrentUser().getUid());
                    reportUser1.put("reason", reason);
                    reportUser1.put("type", "reportUser");
                    reportUser1.put("seller_id", seller_id);
                    reportUser1.put("timeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("ReportList").add(reportUser1)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(ReportUsers.this, "REPORT LIST SUCCESSFUL1", Toast.LENGTH_SHORT).show();
                                }
                            });


                }else{

                 //   Toast.makeText(ReportUsers.this, "amaw ka ", Toast.LENGTH_SHORT).show();

                    //STORE REPORT COLLECTION
                    Map<String, Object> reportUser2 = new HashMap<>();
                    reportUser2.put("buyer_id", mAuth.getCurrentUser().getUid());
                    reportUser2.put("reason", reasonData);
                    reportUser2.put("type", "reportUser");
                    reportUser2.put("seller_id", seller_id);
                    reportUser2.put("timeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("ReportList").add(reportUser2)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(ReportUsers.this, "REPORT LIST SUCCESSFUL2", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                //SEND NOTIFICATION
                Map<String, Object> notifData = new HashMap<>();
                notifData.put("seller_id", seller_id);
                notifData.put("notificationType", "reportedUser");
                notifData.put("message", "Someone has reported you");
                notifData.put("buyer_id", mAuth.getCurrentUser().getUid());

                firebaseFirestore.collection("notification")
                        .add(notifData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(ReportUsers.this, "Notif Data Successful", Toast.LENGTH_SHORT).show();
                            }
                        });

                //UPDATE REPORT COUNT USER
                DocumentReference docRef = firebaseFirestore.collection("users").document(seller_id);
                docRef.update("isReport", FieldValue.increment(1))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(ReportUsers.this, "Updated Report", Toast.LENGTH_SHORT).show();
                            }
                        });

                //SEND TO USER PROFILE
                finish();

            }


        });
    }
}
