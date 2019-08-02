package com.example.exsell;



import android.os.Bundle;


import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class list_remnants1_fragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText lm_remnantsTitle, lm_remnantDescription, lm_backStory, lm_bounceBack;
    private Button nextBtn;






    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.list_remnants1_fragment, container, false);
        /*FishBun.with(list_remnants1_fragment.this   )
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(10)
                .setMinCount(1)
                .setCamera(true)
                .textOnNothingSelected("Please select atleast 1")
                .startAlbum();*/

        recyclerView = view.findViewById(R.id.list_remnants_recyclerview1);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        lm_remnantsTitle = view.findViewById(R.id.lm_title);
        lm_remnantDescription = view.findViewById(R.id.lm_description);
        lm_backStory = view.findViewById(R.id.lm_backStory);
        lm_bounceBack = view.findViewById(R.id.lm_bounceBack);
        nextBtn = view.findViewById(R.id.lm_nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment lm_fragment2  = new List_remnants2_fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.list_remnants_container, lm_fragment2);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });



        return view;
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case Define
                    .ALBUM_REQUEST_CODE:
                if(resultCode == RESULT_OK){

                    List<Uri> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    int size = path.size();

                    UploadPicModel up;
                    List<UploadPicModel> uploadPicModels = new ArrayList<>();

                    try{

                        for(Uri pathfile: path){

                            up = new UploadPicModel();
                            up.setUpload_image(pathfile);
                            uploadPicModels.add(up);

                        }

                        recyclerView.setAdapter(new UploadPicAdapter(getActivity(), uploadPicModels));
                        Toast.makeText(getActivity(), "Size: "+ uploadPicModels.size(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){

                    }
                }
        }
    }*/
}
