package com.leaderpower.baechelin_owner_android.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OwnersListAdapter;
import com.leaderpower.baechelin_owner_android.app.BaechelinApp;
import com.leaderpower.baechelin_owner_android.model.BusinessInfo;
import com.leaderpower.baechelin_owner_android.model.BusinessOwners;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    private ArrayList<OwnerItem> ownerLists;
    private OwnersListAdapter ownersListAdapter;
    private FirebaseUser currentUser;
    private String uid;
    private FirebaseFirestore db;

    private final String TAG = "MAIN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        for (int i = 0; i < 100; i++) ownerLists.add(new OwnerItem("name " + i, "address " + i));
//        ownersListAdapter = new OwnersListAdapter(ownerLists);
//
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setAdapter(ownersListAdapter);
//        recyclerView.setLayoutManager(layoutManager);

        //get current user
        currentUser = BaechelinApp.getCurrentUser();
        uid = currentUser.getUid();

        initRecyclerView();

        //get firestore
        db = FirebaseFirestore.getInstance();
        db.collection("auths").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()){
                            Log.d(TAG, "DocumentSnapshot data " + doc.getData());
                            BusinessInfo businessInfo = doc.toObject(BusinessInfo.class);
                            BaechelinApp.setBusinessInfo(businessInfo);

                            getOwnerInfo(businessInfo.getOwners());

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "옳바르지 않은 로그인.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ownerLists = new ArrayList<>();


    }

    private void getOwnerInfo(ArrayList<BusinessOwners> owners){
        final int owner_num = owners.size();
        for (BusinessOwners owner : owners ){
            db.collection("owner").document(owner.getOid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()){
                                OwnerItem ownerItem = doc.toObject(OwnerItem.class);
                                ownerLists.add(ownerItem);

                                if (owner_num == ownerLists.size()){
                                    ownersListAdapter = new OwnersListAdapter(ownerLists);
                                    recyclerView.setAdapter(ownersListAdapter);
                                }
                            }
                        }
                    });
        }
    }

}
