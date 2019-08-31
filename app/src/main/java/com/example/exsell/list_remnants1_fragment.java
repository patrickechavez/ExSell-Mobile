package com.example.exsell;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exsell.Adapter.UploadPicAdapter;
import com.example.exsell.Models.UploadPicModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.exsell.R.color.buttonDisabled;
import static com.example.exsell.R.color.colorPrimaryDark;


public class list_remnants1_fragment extends Fragment {

    private static final String TAG = "list_remnants_1";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText lm_remnantsTitle, lm_remnantDescription, lm_backStory, lm_bounceBack;
    private Button nextBtn;
    private List<String> imageUrl;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private String user_id;
    private List<Uri> pathUri;





    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.list_remnants1_fragment, container, false);
        FishBun.with(list_remnants1_fragment.this   )
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(10)
                .setMinCount(1)
                .setCamera(true)
                .textOnNothingSelected("Please select atleast 1")
                .startAlbum();



        recyclerView = view.findViewById(R.id.list_remnants_recyclerview1);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
         user_id = mAuth.getCurrentUser().getUid();

        lm_remnantsTitle =  view.findViewById(R.id.lm_title);
        lm_remnantDescription =  view.findViewById(R.id.lm_description);
        lm_backStory =  view.findViewById(R.id.lm_backStory);
        lm_bounceBack = view.findViewById(R.id.lm_bounceBack);
        nextBtn =  view.findViewById(R.id.lm_nextBtn);

       lm_remnantsTitle.addTextChangedListener(nextWatcher);
       lm_remnantDescription.addTextChangedListener(nextWatcher);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = lm_remnantsTitle.getText().toString().trim();
                String description = lm_remnantDescription.getText().toString().trim();
                String backStory = lm_backStory.getText().toString().trim();
                String bounceBack = lm_bounceBack.getText().toString().trim();


                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listOfPic", (ArrayList<? extends Parcelable>) pathUri);
                bundle.putString("title", title);
                bundle.putString("description", description);
                bundle.putString("backStory", backStory);
                bundle.putString("bounceBack", bounceBack);


                Fragment lm_fragment2  = new List_remnants2_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                lm_fragment2.setArguments(bundle);
                transaction.replace(R.id.list_remnants_container, lm_fragment2);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        return view;
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

                        recyclerView.setAdapter(new UploadPicAdapter(getActivity(), uploadPicModels));
                       // Toast.makeText(getActivity(), "Size: "+ uploadPicModels.size(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){

                    }
                }
        }
    }
    private TextWatcher nextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String title = lm_remnantsTitle.getText().toString().trim();
            String description = lm_remnantDescription.getText().toString().trim();

            if(!title.isEmpty() &&!description.isEmpty()){


                nextBtn.setEnabled(true);
                nextBtn.setBackgroundColor(getResources().getColor(colorPrimaryDark));

            }else{


                nextBtn.setEnabled(false);
                nextBtn .setBackgroundColor(getResources().getColor(buttonDisabled));
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
