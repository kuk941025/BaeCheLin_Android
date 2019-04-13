package com.leaderpower.baechelin_owner_android.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderListAdapter;
import com.leaderpower.baechelin_owner_android.model.Order;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderCompleteFragment extends Fragment {
    @BindView(R.id.fragment_edit_start_date)
    EditText startDate;
    @BindView(R.id.fragment_edit_end_date)
    EditText endDate;
    @BindView(R.id.fragment_order_complete_recycler)
    RecyclerView recyclerView;

    private OrderListAdapter orderAdapter;
    private View fragView = null;
    private ArrayList<Order> orderList;
    public OrderCompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragView == null){
            fragView = inflater.inflate(R.layout.fragment_order_complete, container, false);;
            ButterKnife.bind(this, fragView);

            initRecycleView();
        }

        return fragView;
    }

    private void initRecycleView(){
        orderList = new ArrayList<>();
        for (int i = 0; i < 100; i++) orderList.add(new Order("주소 " + i, "음식 " + i));

        orderAdapter = new OrderListAdapter(orderList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);
    }
}
