package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;
    private EditText acc_fname, acc_lname, acc_email;
    private Button update_btn, changepassbtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseUser currentUser;
    private DocumentReference docRef;


    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Setup");

        acc_fname = findViewById(R.id.acc_fname);
        acc_lname = findViewById(R.id.acc_lname);
        update_btn = findViewById(R.id.acc_updatebtn);
        changepassbtn = findViewById(R.id.acc_changepassbtn);
        setupImage = findViewById(R.id.acc_image);

        String user_id = mAuth.getCurrentUser().getUid();

        docRef = firebaseFirestore.collection("users").document(user_id);


        DocumentReference docRef = firebaseFirestore.collection("users").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    String firstname = task.getResult().getString("firstName");
                    String lastname = task.getResult().getString("lastName");
                    String imageUrl = task.getResult().getString("imageUrl");

                    acc_fname.setText(firstname);
                    acc_lname.setText(lastname);

                    if(!imageUrl.equals("default")){

                        Picasso.get()
                                .load(imageUrl)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.user)
                                .error(R.drawable.user)
                                .into(setupImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get()
                                                .load(imageUrl)
                                                .placeholder(R.drawable.user)
                                                .error(R.drawable.user)
                                                .into(setupImage);
                                    }
                                });
                    }
                }
            }
        });


        update_btn.setOnClickListener(this);
        changepassbtn.setOnClickListener(this);
        setupImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.acc_updatebtn:

                final String firstName = acc_fname.getText().toString().trim();
                final String lastName = acc_lname.getText().toString().trim();
                final String user_id = mAuth.getCurrentUser().getUid();
                final StorageReference image_path = storageReference.child("profile_images").child(user_id+ ".jpg");
                    UploadTask uploadTask = image_path.putFile(mainImageURI);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        throw task.getException();

                        return image_path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            firebaseFirestore.collection("users").document(user_id)
                                    .update("firstName", firstName,
                                                "lastName", lastName,
                                                "imageUrl", downloadUri.toString());


                            /*mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
                            Map<String, Object> userUpdate = new HashMap<>();
                            userUpdate.put("firstName", firstName);
                            userUpdate.put("lastName", lastName);
                            userUpdate.put("imageUrl", downloadUri.toString());

                            mDatabase.updateChildren(userUpdate);*/

                            Toast.makeText(Account.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case R.id.acc_image:

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(Account.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(Account.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );
                    }else{

                        imagePicker();
                    }
                }else{

                    imagePicker();
                }
                break;

            case R.id.acc_changepassbtn:

                break;
        }
    }

    private void imagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(Account.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

               mainImageURI = result.getUri();
               setupImage.setImageURI(mainImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null) {
            sendToLogin();
        }else{

            docRef.update("online", "true");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(currentUser != null) {

            docRef.update("online", "true");
        }

    }

    private void sendToLogin() {

        Intent login = new Intent(Account.this, Login.class);
        startActivity(login);
        finish();
    }



}
