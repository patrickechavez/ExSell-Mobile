package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Models.BlogModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

public class Blogview extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private TextView textViewGist, textViewJuice, textViewBreakupStage;
    private ImageView imageViewEmoji;
    private String blogId;
    private List<String> stringImageUrl;
    private CarouselView carouselView;
    private BlogModel blogModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloview_activity);



        blogId = getIntent().getStringExtra("blogId");

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        carouselView = findViewById(R.id.blogView_carouselView);
        textViewGist = findViewById(R.id.blogView_theGist);
        textViewJuice = findViewById(R.id.blogView_theJuice);
        imageViewEmoji = findViewById(R.id.blogView_emoji);
        carouselView = findViewById(R.id.blogView_carouselView);
        textViewBreakupStage = findViewById(R.id.blogView_emojiTextView);

        Toolbar toolbar = findViewById(R.id.blogview_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        fetchBlog();
    }

    private void fetchBlog() {

        DocumentReference docref = firebaseFirestore.collection("blogs").document(blogId);
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

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

            }
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
}


