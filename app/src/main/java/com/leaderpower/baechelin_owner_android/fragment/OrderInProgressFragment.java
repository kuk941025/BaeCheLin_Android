package com.leaderpower.baechelin_owner_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderListAdapter;
import com.leaderpower.baechelin_owner_android.model.Order;

import java.util.ArrayList;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderInProgressFragment extends Fragment {
    @BindView(R.id.fragment_order_progress_recycle)
    RecyclerView recyclerView;

    private View fragView = null;
    private OrderListAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private String oid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Query dbRef;
    private ListenerRegistration dbListener;

    private final String TAG = "ORDER_FRAGMENT";
    public OrderInProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fragView == null){
            fragView = inflater.inflate(R.layout.fragment_order_in_progress, container, false);
            ButterKnife.bind(this, fragView);

            //receive oid
            oid = getArguments().getString("oid");
            Toast.makeText(getActivity(), "Received oid " + oid, Toast.LENGTH_LONG).show();
            initRecyclerView();

            //set collection reference
            dbRef = db.collection("owner").document(oid).collection("orders").whereLessThanOrEqualTo("status", 1 );
        }
        return fragView;
    }

    @Override
    public void onStart() {
        super.onStart();

        dbListener = dbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d(TAG, "Listen failed "  + e);
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots ){
                        Log.d(TAG, "Current data : " + snapshot.getData());

                        orderList.add(snapshot.toObject(Order.class));
                        orderAdapter.notifyItemInserted(orderList.size());
                    }
                }
                else {
                    Log.d(TAG, "current data is null");
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        dbListener.remove();
    }

    private void initRecyclerView() {
        orderList = new ArrayList<>();

        orderAdapter = new OrderListAdapter(orderList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);
    }

}
