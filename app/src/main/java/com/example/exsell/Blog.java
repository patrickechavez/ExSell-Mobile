package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exsell.Adapter.UploadPicAdapter;
import com.example.exsell.Models.UploadPicModel;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.exsell.R.color.colorPrimaryDark;

public class Blog extends AppCompatActivity implements View.OnClickListener {

    private static final int PReqCode = 2;
    private static final int REQUESTCODE = 2;

    private EditText editTextTitle, editTextGist, editTextJuice;
    private Uri mainImageURI = null;
    private Button buttonTellIt;

    private ImageView imageView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private RecyclerView recyclerView;
    private String breakUpStageString = "Shocked";
    private List<Uri> pathUri;
    private ArrayList<String> listImageUrl = new ArrayList<>();
    private int count = 0;
    private String TAG = "BLOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_activity);

        Toolbar toolbar = findViewById(R.id.blog_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SHARE YOUR STORY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage =  FirebaseStorage.getInstance();

        editTextTitle = findViewById(R.id.blog_title);
        editTextGist = findViewById(R.id.blog_theGist);
        editTextJuice = findViewById(R.id.blog_theJuice);
        imageView = findViewById(R.id.blog_imageView);
        buttonTellIt = findViewById(R.id.blog_postBtn);


        recyclerView = findViewById(R.id.blog_addRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        editTextTitle.addTextChangedListener(textWatcher);
        editTextGist.addTextChangedListener(textWatcher);
        editTextJuice.addTextChangedListener(textWatcher);


        imageView.setOnClickListener(this);
        buttonTellIt.setOnClickListener(this);

        String[] breakUpStage = new String[] {"Shocked", "Heartbroken", "Super Angry", "On The Rebound", "Better and Ever"};

        AutoCompleteTextView editTextFilledExposedDropdownBreakUpStage = (AutoCompleteTextView) findViewById(R.id.blog_breakUpStage);
        editTextFilledExposedDropdownBreakUpStage.setText("Shocked");

        ArrayAdapter<String> adapterBreakUpStage = new ArrayAdapter<>(this,R.layout.dropdown_menu_popup_item, breakUpStage);
        editTextFilledExposedDropdownBreakUpStage.setAdapter(adapterBreakUpStage);

        editTextFilledExposedDropdownBreakUpStage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                breakUpStageString = "Shocked";
                breakUpStageString = adapterBreakUpStage.getItem(position);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.blog_imageView:

                FishBun.with(this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(10)
                        .setMinCount(1)
                        .setButtonInAlbumActivity(true)
                        .setCamera(true)
                        .textOnNothingSelected("Please select atleast 1")
                        .startAlbum();

                imageView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;



            case R.id.blog_postBtn:

                String title = editTextTitle.getText().toString().trim();
                String gist = editTextGist.getText().toString().trim();
                String juice = editTextJuice.getText().toString().trim();

                for(Uri pic: pathUri){

                    StorageReference storageReference = firebaseStorage.getReference().child("product_Images").child(pic.toString());
                    UploadTask uploadTask = (UploadTask) storageReference.putFile(pic);



                    Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                        if(!task.isSuccessful()){

                            throw task.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if(task.isSuccessful()){

                                String urlString = task.getResult().toString();
                                listImageUrl.add(urlString);
                                count++;

                                Log.d(TAG, " HAHAH listURl: "+listImageUrl);

                            }else{

                                String error = task.getException().getMessage();
                                Toast.makeText(Blog.this, ""+error, Toast.LENGTH_SHORT).show();
                            }

                            if(count == pathUri.size()){

                               Map<String, Object> postBlog = new HashMap<>();
                               postBlog.put("imageUrl", listImageUrl);
                               postBlog.put("title", title);
                               postBlog.put("gist", gist);
                               postBlog.put("juice", juice);
                               postBlog.put("breakupStage", breakUpStageString);
                               postBlog.put("userId", mAuth.getCurrentUser().getUid());
                               postBlog.put("timeStamp", FieldValue.serverTimestamp());


                               firebaseFirestore.collection("blogs").add(postBlog)
                                       .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                           @Override
                                           public void onSuccess(DocumentReference documentReference) {

                                               Toast.makeText(Blog.this, "Added Successfully", Toast.LENGTH_SHORT).show();

                                               Intent toDashboard = new Intent(Blog.this, Dashboard.class);
                                               startActivity(toDashboard);


                                           }
                                       });
                            }
                        }
                    });

                }

                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case Define
                    .ALBUM_REQUEST_CODE:
                if(resultCode == RESULT_OK){

                    pathUri = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    // pathString = data.getStringArrayListExtra(Define.INTENT_PATH);

                    UploadPicModel up;
                    List<UploadPicModel> uploadPicModels = new ArrayList<>();

                    //  Log.d(TAG, ""+pathUri);

                    try{

                        for(Uri pathfile: pathUri){

                            up = new UploadPicModel();
                            up.setUpload_image(pathfile);
                            uploadPicModels.add(up);

                        }

                        recyclerView.setAdapter(new UploadPicAdapter(this, uploadPicModels));
                        // Toast.makeText(getActivity(), "Size: "+ uploadPicModels.size(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){

                    }
                }
        }
    }




    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String title = editTextTitle.getText().toString().trim();
            String gist = editTextGist.getText().toString().trim();
            String juice = editTextJuice.getText().toString().trim();


            if(!title.isEmpty() && !gist.isEmpty() && !juice.isEmpty()){

                buttonTellIt.setEnabled(true);
                buttonTellIt.setBackgroundColor(getResources().getColor(colorPrimaryDark));
            }else{

                buttonTellIt.setEnabled(false);
                //buttonTellIt.setBackgroundColor(getResources().getColor());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
