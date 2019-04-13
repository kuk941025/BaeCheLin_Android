package com.leaderpower.baechelin_owner_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderListAdapter;
import com.leaderpower.baechelin_owner_android.model.Order;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderInProgressFragment extends Fragment {
    @BindView(R.id.fragment_order_progress_recycle)
    RecyclerView recyclerView;

    private View fragView = null;
    private OrderListAdapter orderAdapter;
    private ArrayList<Order> orderList;
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

            initRecyclerView();
        }
        return fragView;
    }

    private void initRecyclerView() {
        orderList = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            orderList.add(new Order("주소" + i, "음식" + i));
        }
        orderAdapter = new OrderListAdapter(orderList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);
    }

}
