package com.leaderpower.baechelin_owner_android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OwnersListAdapter;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    private ArrayList<OwnerItem> ownerLists = new ArrayList<>();
    private OwnersListAdapter ownersListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        for (int i = 0; i < 100; i++) ownerLists.add(new OwnerItem("name " + i, "address " + i));
        ownersListAdapter = new OwnersListAdapter(ownerLists);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(ownersListAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
