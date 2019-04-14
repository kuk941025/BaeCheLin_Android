package com.leaderpower.baechelin_owner_android.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.model.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.orderViewHolder>{
    private ArrayList<Order> orderList;

    public OrderListAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public orderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_order_item, viewGroup, false);
        return new orderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull orderViewHolder orderViewHolder, int i) {
        DecimalFormat df = new DecimalFormat("#,###");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Order item = orderList.get(i);
        orderViewHolder.txtAddress.setText(item.getAddress());
        orderViewHolder.txtFood.setText(item.getFood_ordered());
        orderViewHolder.txtPrice.setText(df.format(item.getTotal_price()) + "원");
        orderViewHolder.txtTime.setText(sdf.format(item.getCreated_at()));

        switch (item.getStatus()){
            case 0:
                orderViewHolder.txtStatus.setBackgroundColor(Color.parseColor("#FF9800"));
                orderViewHolder.txtStatus.setText("신규");
                break;
            case 1:
                orderViewHolder.txtStatus.setBackgroundColor(Color.parseColor("#FF5722"));
                orderViewHolder.txtStatus.setText("처리중");
                break;
            case 2:
                orderViewHolder.txtStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
                orderViewHolder.txtStatus.setText("완료됨");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class orderViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.template_order_address)
        TextView txtAddress;
        @BindView(R.id.template_order_time)
        TextView txtTime;
        @BindView(R.id.template_order_status)
        TextView txtStatus;
        @BindView(R.id.template_order_price)
        TextView txtPrice;
        @BindView(R.id.template_order_food)
        TextView txtFood;
        @BindView(R.id.template_order_confirm)
        Button btnConfirm;
        @BindView(R.id.template_order_reject)
        Button btonReject;

        public orderViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
