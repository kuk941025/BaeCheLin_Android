package com.leaderpower.baechelin_owner_android.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_nested_scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.main_layout_loading)
    View loadingLayout;
    @BindView(R.id.main_txt_total_owners)
    TextView txtOwnerNum;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_txt_title)
    TextView txtToolTitle;


    private ArrayList<OwnerItem> ownerLists;
    private OwnersListAdapter ownersListAdapter;
    private FirebaseUser currentUser;
    private String uid;
    private FirebaseFirestore db;
    private BaechelinApp baechelinApp;
    private final String TAG = "MAIN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        baechelinApp = BaechelinApp.getInstance();

        //get current user
        currentUser = BaechelinApp.getCurrentUser();
        uid = currentUser.getUid();

        initRecyclerView();
        setToolbar();


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
                            baechelinApp.setBusinessInfo(businessInfo);
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

    @OnClick(R.id.toolbar_right_btn)
    void onSettingClicked(){
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        txtToolTitle.setText("배슐랭");
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

                                    ownersListAdapter = new OwnersListAdapter(ownerLists, MainActivity.this);
                                    recyclerView.setAdapter(ownersListAdapter);

                                    loadingLayout.setVisibility(View.GONE);
                                    nestedScrollView.setVisibility(View.VISIBLE);
                                    txtOwnerNum.setText("사장님, 현재 등록되어 있는 업소는 총 " + owner_num + "개 입니다.");
                                }
                            }
                        }
                    });
        }
    }

}
