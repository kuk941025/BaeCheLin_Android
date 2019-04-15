package com.leaderpower.baechelin_owner_android.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderListAdapter;
import com.leaderpower.baechelin_owner_android.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    private final Calendar calendar = Calendar.getInstance();
    public OrderCompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragView == null){
            fragView = inflater.inflate(R.layout.fragment_order_complete, container, false);;
            ButterKnife.bind(this, fragView);

            setDatePicker();
        }

        return fragView;
    }
    private void setDatePicker(){

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
//    private void initRecycleView(){
//        orderList = new ArrayList<>();
////        for (int i = 0; i < 100; i++) orderList.add(new Order("주소 " + i, "음식 " + i));
//
//        orderAdapter = new OrderListAdapter(orderList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(orderAdapter);
//    }

    private void updateLabel() {
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        startDate.setText(sdf.format(calendar.getTime()));
    }
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        }
    };
}
