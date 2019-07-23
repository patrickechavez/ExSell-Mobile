package com.example.exsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.exsell.Adapter.UploadPicAdapter;
import com.example.exsell.Models.UploadPicModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;
import java.util.List;


public class ListRemnants extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private LinearLayoutManager linearLayoutManager;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_remnants_activity);

        FishBun.with(ListRemnants.this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(10)
                .setMinCount(1)
                .setCamera(true)
                .textOnNothingSelected("Please select atleast 1")
                .startAlbum();

        recyclerView = findViewById(R.id.list_remnants_recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case Define
                    .ALBUM_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){

                    List<Uri> path  = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    int size = path.size();

                    //Toast.makeText(this, ""+ size, Toast.LENGTH_SHORT).show();
                    UploadPicModel up;
                    List<UploadPicModel> uploadPicModels = new ArrayList<>();

                    try {

                        for(Uri pathfile : path) {
                            up = new UploadPicModel();
                            up.setUpload_image(pathfile);
                            uploadPicModels.add(up);

                            StorageReference filetoUpload = storageReference.child("productImages").child(pathfile.toString());
                            UploadTask uploadTask = filetoUpload.putFile(pathfile);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                    if(!task.isSuccessful()){

                                        throw task.getException();
                                    }
                                    return filetoUpload.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    if(task.isSuccessful()){

                                        Uri downloadUri = task.getResult();

                                        Toast.makeText(ListRemnants.this, "SUccess: "+ downloadUri.toString(), Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });


                        }

                        recyclerView.setAdapter(new UploadPicAdapter(this, uploadPicModels));
                       // Toast.makeText(this, "Size: " +uploadPicModels.size(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){


                    }

                }
        }
    }




}
