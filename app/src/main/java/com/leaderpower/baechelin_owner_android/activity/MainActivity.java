package com.leaderpower.baechelin_owner_android.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OwnersListAdapter;
import com.leaderpower.baechelin_owner_android.app.BaechelinApp;
import com.leaderpower.baechelin_owner_android.model.BusinessInfo;
import com.leaderpower.baechelin_owner_android.model.BusinessOwners;
import com.leaderpower.baechelin_owner_android.model.Notification;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*
 * Logic behind getting data from firestore
 * User logs in and gets user data.
 * 1. From the received user data, find out oid that the user has.
 * 2. Load all owner data.
 * 3. For each owner data, get notification data which can be retrieved from owner/{oid}/notifications
 * 4. document id of notification is token_id for the device; save it to ownerItem, which stores all owner-related data.
 * */
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
    @BindView(R.id.main_layout_error)
    View errorLayout;
    @BindView(R.id.main_error_dscrp)
    TextView txtError;
    @BindView(R.id.toolbar_right_btn)
    Button btnToolRight;
    private ArrayList<OwnerItem> ownerLists;
    private OwnersListAdapter ownersListAdapter;
    private FirebaseUser currentUser;
    private String uid;
    private FirebaseFirestore db;
    private BaechelinApp baechelinApp;
    private BusinessInfo businessInfo;
    private FirebaseAuth mAuth;
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
        btnToolRight.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db.collection("auths").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            btnToolRight.setVisibility(View.VISIBLE);
                            Log.d(TAG, "DocumentSnapshot data " + doc.getData());
                            businessInfo = doc.toObject(BusinessInfo.class);
                            baechelinApp.setBusinessInfo(businessInfo);

                            //check if owner has applied stores;
                            //only users who have applied store can use
                            if (businessInfo.getBusiness_status() < 2){

                                errorLayout.setVisibility(View.VISIBLE);
                                txtError.setText("웹사이트에서 사업자 정보를 승인을 받은 후 이용할 수 있습니다.");
                            }
                            else if (businessInfo.getOwners().size() == 0){
                                //if no owner is added

                                errorLayout.setVisibility(View.VISIBLE);
                                txtError.setText("웹사이트에서 업체 등록을 먼저 하시고 이용해주시기 바랍니다.");

                            }
                            else {
                                getOwnerInfo(businessInfo.getOwners());
                            }
                        } else {
                            errorLayout.setVisibility(View.VISIBLE);
                            btnToolRight.setVisibility(View.GONE);
                            txtError.setText("사장님 계정으로만 서비스를 이용할 수 있습니다.");
                            Toast.makeText(getApplicationContext(), "옳바르지 않은 로그인.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ownerLists = new ArrayList<>();
    }

    @OnClick(R.id.toolbar_right_btn)
    void onSettingClicked() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);


    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        txtToolTitle.setText("배슐랭");

    }
    private void sendToLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            sendToLoginActivity();
        }

    }

    private void getOwnerInfo(ArrayList<BusinessOwners> owners) {
        final int owner_num = owners.size();
        final ArrayList<BusinessOwners> businessOwners = businessInfo.getOwners();

        for (BusinessOwners owner : owners) {
            db.collection("owner").document(owner.getOid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                final OwnerItem ownerItem = doc.toObject(OwnerItem.class);

                                //get notification data to setup switch
                                db.collection("owner").document(ownerItem.getOid()).collection("notifications").get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                                                List<Notification> notificationList = new ArrayList<>();

                                                for (DocumentSnapshot doc : documentSnapshots) {
                                                    Notification notification = doc.toObject(Notification.class);
                                                    notification.setToken_id(doc.getId());
                                                    notificationList.add(notification);
                                                }

                                                ownerItem.setToken(notificationList);
                                                ownerLists.add(ownerItem);
                                                if (owner_num == ownerLists.size()) {

                                                    ownersListAdapter = new OwnersListAdapter(ownerLists, MainActivity.this, db);
                                                    recyclerView.setAdapter(ownersListAdapter);

                                                    loadingLayout.setVisibility(View.GONE);
                                                    nestedScrollView.setVisibility(View.VISIBLE);
                                                    Collections.sort(ownerLists, new Comparator<OwnerItem>() {
                                                        @Override
                                                        public int compare(OwnerItem ownerItem, OwnerItem t1) {
                                                            return ownerItem.getCreated_at().compareTo(t1.getCreated_at());
                                                        }
                                                    });
                                                    txtOwnerNum.setText("사장님, 현재 등록되어 있는 업소는 총 " + owner_num + "개 입니다.");
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "네트워크 통신 오류가 발생했습니다. 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "네트워크 통신 오류가 발생했습니다. 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @OnClick(R.id.main_error_btn)
    void onErrorClicked(){
        mAuth.signOut();
        sendToLoginActivity();
    }
}
