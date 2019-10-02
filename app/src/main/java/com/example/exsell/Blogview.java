package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Adapter.CommentAdapter;
import com.example.exsell.Models.BlogModel;
import com.example.exsell.Models.CommentModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;

public class Blogview extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BLOGVIEW";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private TextView textViewGist, textViewJuice, textViewBreakupStage;
    private ImageView imageViewEmoji;
    private String blogId;
    private List<String> stringImageUrl;
    private CarouselView carouselView;
    private BlogModel blogModel;
    private CircleImageView circleImageView, circleImageViewCommentImageView;
    private TextView textViewOwner;
    private Button buttonAddComment;
    private EditText editTextWriteComment;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blogview_activity);


        blogId = getIntent().getStringExtra("blogId");
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        carouselView = findViewById(R.id.blogView_carouselView);
        textViewGist = findViewById(R.id.blogView_theGist);
        textViewJuice = findViewById(R.id.blogView_theJuice);
        imageViewEmoji = findViewById(R.id.blogView_emoji);
        carouselView = findViewById(R.id.blogView_carouselView);
        textViewBreakupStage = findViewById(R.id.blogView_emojiTextView);

        circleImageView = findViewById(R.id.blogImageView);
        textViewOwner = findViewById(R.id.blogTextView);
        //COMMENT SECTION
       // circleImageViewCommentImageView = findViewById(R.id.blogView_commentImageView);
        editTextWriteComment = findViewById(R.id.blogView_commentDetail);
        buttonAddComment = findViewById(R.id.blogView_commentButton);


        Toolbar toolbar = findViewById(R.id.blogview_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        buttonAddComment.setOnClickListener(this);
        editTextWriteComment.addTextChangedListener(addComment);

        setUpRecyclerView();
        fetchBlog();

    }

    private void setUpRecyclerView() {

    Query query = firebaseFirestore.collection("blogs").document(blogId)
            .collection("comment")
            .orderBy("timeStamp", Query.Direction.DESCENDING);

    FirestoreRecyclerOptions<CommentModel> options = new FirestoreRecyclerOptions.Builder<CommentModel>()
            .setQuery(query, CommentModel.class)
            .build();


    commentAdapter = new CommentAdapter(options, this);

    RecyclerView recyclerView = findViewById(R.id.blog_commentRecyclerView);
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



    private void fetchBlog() {

        DocumentReference docref = firebaseFirestore.collection("blogs").document(blogId);
        docref.get().addOnSuccessListener(documentSnapshot -> {

            blogModel = documentSnapshot.toObject(BlogModel.class);

            textViewGist.setText(blogModel.getGist());
            textViewJuice.setText(blogModel.getJuice());
            textViewBreakupStage.setText(blogModel.getBreakupStage());
            stringImageUrl = blogModel.getImageUrl();
            textViewBreakupStage.setText(blogModel.getBreakupStage());

            switch (blogModel.getBreakupStage()){

                    case "Shocked":

                        Picasso.get().load("https://i.ibb.co/99xmCgQ/blog-shocked.png").into(imageViewEmoji);
                        break;

                    case "Heartbroken":

                        Picasso.get().load("https://i.ibb.co/QYRtDw0/blog-broken-Hearted.png").into(imageViewEmoji);
                        break;

                    case "Super Angry":

                        Picasso.get().load("https://i.ibb.co/wM5yBH2/blog-angry.png").into(imageViewEmoji);
                        break;

                    case "On The Rebound":

                        Picasso.get().load("https://i.ibb.co/gmjgjgv/blog-on-The-Rebound.png").into(imageViewEmoji);
                        break;

                    case "Better and Ever":

                        Picasso.get().load("https://i.ibb.co/9HDrqDc/smile.png").into(imageViewEmoji);
                        break;
                }

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(stringImageUrl.size());

            firebaseFirestore.collection("users").document(blogModel.getUserId())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                   if(task.isSuccessful()){
                       DocumentSnapshot documentSnapshot1 = task.getResult();

                       Picasso.get().load(documentSnapshot1.getString("imageUrl")).into(circleImageView);
                       textViewOwner.setText(documentSnapshot1.getString("firstName"));
                   }
                }
            });

        });

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            for(String s: stringImageUrl)
                Picasso.get().load(stringImageUrl.get(position)).into(imageView);
        }
    };




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        DocumentReference docRef = firebaseFirestore.collection("blogs").document(blogId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document  = task.getResult();

                    if(document.getString("userId").equals(mAuth.getCurrentUser().getUid())){

                        inflater.inflate(R.menu.blog_toolbar_menu, menu);

                    }
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.edit_blog:

                break;

            case R.id.delete_blog:
                firebaseFirestore.collection("blogs").document(blogId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(Blogview.this, "Blog Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent toDashboard = new Intent(Blogview.this, Dashboard.class);
                                startActivity(toDashboard);
                            }
                        });
                break;


        }
        return super.onOptionsItemSelected(item);
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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.blogView_commentButton:

                String comment = editTextWriteComment.getText().toString().trim();

                DocumentReference remnants = firebaseFirestore.collection("blogs").document();
                String documentId = remnants.getId();

                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("userId", mAuth.getCurrentUser().getUid());
                commentMap.put("comment", comment);
                commentMap.put("blogId",blogId);
                commentMap.put("timeStamp", FieldValue.serverTimestamp());


                firebaseFirestore.collection("blogs").document(blogId).collection("comment")
                        .document(documentId).set(commentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Blogview.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                /*firebaseFirestore.collection("blogs").document(blogId).collection("comment").add(commentMap)
                        .addOnSuccessListener(documentReference ->
                                Toast.makeText(Blogview.this, "Added Successfully", Toast.LENGTH_SHORT).show());*/

                editTextWriteComment.setText("");
                buttonAddComment.setEnabled(false);
                buttonAddComment.setBackgroundColor(getResources().getColor(buttonDisabled));

                break;
        }

    }

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


