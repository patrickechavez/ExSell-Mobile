package com.example.exsell;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Adapter.MessageAdapter;
import com.example.exsell.Adapter.UsersAdapter;
import com.example.exsell.Models.MessageModel;
import com.example.exsell.Models.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.exsell.R.id.custom_bar_image;


public class Chat extends AppCompatActivity {


    private String user_id;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private CircleImageView custom_bar_image;
    private TextView custom_bar_fname;
    private TextView custom_bar_lname;
    private TextView custom_bar_seen;

    private ImageButton addImage;
    private EditText addMessage;
    private ImageButton sendBtn;
    private String currentUserId;
    private RecyclerView recyclerView;

    private List<MessageModel> messageModels = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        toolbar = findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firebaseFirestore = FirebaseFirestore.getInstance();
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        user_id = getIntent().getStringExtra("receiver_id");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);

        actionBar.setCustomView(action_bar_view);


        //_______CUSTOM ACTION BAR ITEMS_____//

        custom_bar_image = findViewById(R.id.custom_bar_image);
        custom_bar_fname = findViewById(R.id.custom_bar_fname);
        custom_bar_lname = findViewById(R.id.custom_bar_lname);
        custom_bar_seen = findViewById(R.id.custom_bar_seen);

        DocumentReference docRef = firebaseFirestore.collection("users").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if (task.getResult().exists()) {

                        final String fname = task.getResult().getString("firstName");
                        final String lname = task.getResult().getString("lastName");
                        final String image = task.getResult().getString("imageUrl");

                        custom_bar_fname.setText(fname);
                        custom_bar_lname.setText(lname);

                        if(image.equals("default")){
                            custom_bar_image.setImageResource(R.drawable.user);
                        }else{
                            Picasso.get().load(image).into(custom_bar_image);
                        }
                    }
                }
            }
        });



        addImage = findViewById(R.id.chat_image);
        addMessage = findViewById(R.id.chat_message);
        sendBtn = findViewById(R.id.chat_sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
            }
        });

        messageAdapter = new MessageAdapter(messageModels);
        recyclerView = findViewById(R.id.chat_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);


        loadMessages();



    }



    private void loadMessages() {

        Query query = FirebaseFirestore.getInstance().collection("chat")
                .orderBy("time", Query.Direction.ASCENDING);

        firebaseFirestore.collection("chat").orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        List<MessageModel> newMessageModel = new ArrayList<>();

                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){

                            if(doc.get("receiver").equals(currentUserId) && doc.get("sender").equals(user_id) ||
                                    doc.get("receiver").equals(user_id) && doc.get("sender").equals(currentUserId)){

                                newMessageModel.add(new MessageModel(
                                        doc.getString("message"),
                                        doc.getString("type"),
                                        doc.getString("sender"),
                                        doc.getString("receiver"),
                                        doc.getDate("time")

                                ));


                            }
                        }
                      //  Collections.reverse(newMessageModel);
                        messageAdapter = new MessageAdapter(newMessageModel);
                        recyclerView.setAdapter(messageAdapter);

                       // addMessage.setText("");
                    }
                });
    }

    private void sendMessage() {

        String message = addMessage.getText().toString();

        if(!TextUtils.isEmpty(message)){



            HashMap<String, Object> chatMap =  new HashMap<>();
            chatMap.put("message", message);
            // chatMap.put("seen", false);
            chatMap.put("type", "text");
            chatMap.put("time", FieldValue.serverTimestamp());
            chatMap.put("sender", currentUserId);
            chatMap.put("receiver", user_id);

            firebaseFirestore.collection("chat").add(chatMap);
            addMessage.setText("");




        }
    }


}


