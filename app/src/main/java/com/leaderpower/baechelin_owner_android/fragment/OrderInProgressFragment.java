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

import com.google.firebase.firestore.DocumentChange;
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
import java.util.Collections;
import java.util.Comparator;

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
    private boolean isSorted;
    private final String TAG = "ORDER_FRAGMENT";

    public OrderInProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fragView == null) {
            fragView = inflater.inflate(R.layout.fragment_order_in_progress, container, false);
            ButterKnife.bind(this, fragView);

            //receive oid
            oid = getArguments().getString("oid");
            Toast.makeText(getActivity(), "Received oid " + oid, Toast.LENGTH_LONG).show();
            initRecyclerView();

            //set collection reference
            dbRef = db.collection("owner").document(oid).collection("orders")
                    .whereLessThanOrEqualTo("status", 1);
            isSorted = false;
        }
        return fragView;
    }

    @Override
    public void onStart() {
        super.onStart();

        dbListener = dbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Listen failed " + e);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    int position = 0;
                    String id = dc.getDocument().getId();
                    switch (dc.getType()) {
                        case ADDED:
                            Order order = dc.getDocument().toObject(Order.class);
                            order.setId(dc.getDocument().getId());
                            orderList.add(order);
                            orderAdapter.notifyItemInserted(orderList.size());

                            break;
                        case REMOVED:
                            for (Order item : orderList) {
                                position++;
                                if (item.getId() == id) {
                                    orderList.remove(item);
                                    orderAdapter.notifyItemRemoved(position);
                                    break;
                                }
                            }
                            break;
                        case MODIFIED:
                            for (Order item : orderList) {
                                if (item.getId().equals(id)) {
                                    Order modifiedOrder = dc.getDocument().toObject(Order.class);
                                    modifiedOrder.setId(dc.getDocument().getId());
                                    orderList.set(position, modifiedOrder);
                                    orderAdapter.notifyItemChanged(position);
                                    break;
                                }

                                position++;
                            }
                            break;
                    }
                }


                if (!isSorted){
                    //initially sort orders by date
                    Collections.sort(orderList, new Comparator<Order>() {
                        @Override
                        public int compare(Order order, Order t1) {
                            return order.getCreated_at().compareTo(t1.getCreated_at());
                        }
                    });

                    orderAdapter.notifyDataSetChanged();
                    isSorted = true;
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

        orderAdapter = new OrderListAdapter(orderList, getActivity().getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);
    }

}
