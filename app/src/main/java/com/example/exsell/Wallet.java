    package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Config.PaypalConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

    public class Wallet extends AppCompatActivity {
        private static final String TAG = "WALLET";

        public static final int PAYPAL_REQUEST_CODE = 7171;

        private static PayPalConfiguration config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

        private FirebaseAuth mAuth;
        private FirebaseFirestore firebaseFirestore;

        private Button buttonAddMoney;
        private TextView textViewMoneyBalance;
        private EditText editTextAddMoney;
        private String amount = "";


        @Override
        protected void onDestroy() {
            stopService(new Intent(this, PayPalService.class));
            super.onDestroy();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.wallet_activity);


            textViewMoneyBalance = findViewById(R.id.wallet_textViewMoneyBalance);


            //FIRESTORE
            mAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();

            final DocumentReference docRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {


                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {

                        textViewMoneyBalance.setText(documentSnapshot.getString("wallet"));
                      //  Log.d(TAG, "Current data: " + documentSnapshot.getData());
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });


            //START PAYPAL SERVICE
            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            startService(intent);


            Toolbar toolbar = findViewById(R.id.wallet_app_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("My Wallet");

            editTextAddMoney = findViewById(R.id.wallet_addMoneyEditText);
            buttonAddMoney = findViewById(R.id.wallet_addMoneyButton);

            buttonAddMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    processPayment();

                }
            });

        }

        private void processPayment() {

            amount = editTextAddMoney.getText().toString().trim();
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "PHP",
                    "Add money in ExSell", PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);

            buttonAddMoney.setText("");

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PAYPAL_REQUEST_CODE) {

                if (resultCode == RESULT_OK) {

                    PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (confirmation != null) {

                        try {

                            String paymentDetails = confirmation.toJSONObject().toString(4);

                            startActivity(new Intent(this, WalletDetails.class)
                                    .putExtra("PaymentDetails", paymentDetails)
                                    .putExtra("PaymentAmount", amount)

                            );

                        } catch (JSONException e) {
                            Log.d("paymentExample", "an extremely unlikely failure occurred: ",e);
                        }

                    }

                } else if (resultCode == Activity.RESULT_CANCELED) {

                    Log.i("paymentExample", "The user canceled.");
                }

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");

            }
        }

    }







