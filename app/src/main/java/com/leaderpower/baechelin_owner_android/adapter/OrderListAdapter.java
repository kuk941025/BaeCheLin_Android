package com.leaderpower.baechelin_owner_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.model.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.orderViewHolder> {
    private ArrayList<Order> orderList;
    private Context mContext;
    private CollectionReference dbRef;

    public OrderListAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.mContext = context;
        dbRef = null;
    }

    public OrderListAdapter(ArrayList<Order> orderList, Context mContext, CollectionReference dbRef) {
        this.orderList = orderList;
        this.mContext = mContext;
        this.dbRef = dbRef;
    }

    public void setDbRef(CollectionReference dbRef) {
        this.dbRef = dbRef;
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


        //update ui based on order status
        switch (item.getStatus()) {
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

        if (item.getMode() == 0) {
            orderViewHolder.viewInfo.setVisibility(View.VISIBLE);
            orderViewHolder.viewSelected.setVisibility(View.GONE);
            if (item.getStatus() == 0){
                orderViewHolder.btnConfirm.setText("승락");
                orderViewHolder.btnReject.setText("거절");
            }
            else if (item.getStatus() == 1 ){
                orderViewHolder.btnConfirm.setText("준비완료");
            }
            else {
                orderViewHolder.btnConfirm.setVisibility(View.GONE);
                orderViewHolder.btnReject.setVisibility(View.GONE);
            }
        } else {
            //new order
            orderViewHolder.viewInfo.setVisibility(View.GONE);
            orderViewHolder.viewSelected.setVisibility(View.VISIBLE);
            orderViewHolder.btnReject.setVisibility(View.VISIBLE);

            if (item.getStatus() == 0) {
                //accepted clicked
                if (item.getMode() == 1) {
                    orderViewHolder.btnConfirm.setText("배달시작");
                    orderViewHolder.btnReject.setText("취소");

                    orderViewHolder.editSelected.setHint("예상 시간(분)");
                    orderViewHolder.editSelected.setInputType(InputType.TYPE_CLASS_NUMBER);
                    orderViewHolder.txtSelectedDscrp.setText("배달예상 시간");
                }
                else if (item.getMode() == 2){
                    //rejected clicked
                    orderViewHolder.btnConfirm.setText("주문취소");
                    orderViewHolder.btnReject.setText("취소");

                    orderViewHolder.editSelected.setHint("고객에게 보내질 메세지를 입력해주세요.");
                    orderViewHolder.editSelected.setInputType(InputType.TYPE_CLASS_TEXT);
                    orderViewHolder.txtSelectedDscrp.setText("거절 사유");
                }
            } else {
                //order in preparation
                if (item.getMode() == 1) {
                    //accepted clicked
                    orderViewHolder.txtSelectedDscrp.setText("배달예상 시간: ");
                    orderViewHolder.editSelected.setHint("예상 시간(분)");
                    orderViewHolder.editSelected.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else {
                    //rejected
                    orderViewHolder.txtSelectedDscrp.setText("거절사유: ");
                    orderViewHolder.editSelected.setHint("고객에게 전달될 메세지입니다.");
                    orderViewHolder.editSelected.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class orderViewHolder extends RecyclerView.ViewHolder {
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
        Button btnReject;
        @BindView(R.id.template_order_information_layout)
        View viewInfo;
        @BindView(R.id.template_order_selected_layout)
        View viewSelected;
        @BindView(R.id.template_order_selected_dscrp)
        TextView txtSelectedDscrp;
        @BindView(R.id.template_order_selected_edit)
        EditText editSelected;
        @BindView(R.id.template_order_main_layout)
        View mainLayout;

        public orderViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.template_order_reject)
        void onRejectClicked() {
            Order order = orderList.get(getLayoutPosition());
            if (order.getMode() == 0) {
                //click rejected
                order.setMode(2);

            } else {
                order.setMode(0);
            }
            notifyItemChanged(getLayoutPosition());

        }

        @OnClick(R.id.template_order_confirm)
        void onConfirmClicked() {
            final Order order = orderList.get(getLayoutPosition());
            if (order.getMode() == 0) {
                order.setMode(1);
            } else {
                if (editSelected.getText().toString().equals("")){
                    Toast.makeText(mContext, "빈칸을 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else{
                    if (order.getMode() == 1){
                        //user has clicked accepting order
                        if (order.getStatus() == 0){
                            //new order, let user know the owner has accepted the order
                            Toast.makeText(mContext, "주문승락", Toast.LENGTH_LONG).show();
                            if (dbRef != null){
                                Map<String, Object> updatedStatus = new HashMap<>();
                                updatedStatus.put("status", 1);
                                dbRef.document(order.getId()).update(updatedStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                order.setStatus(1);
                                                Toast.makeText(mContext, "주문 변경 선공", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                            else {
                                Toast.makeText(mContext, "알수없는 데이터베이스.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            //order in preparation, let user know the delivery has started
                            Toast.makeText(mContext, "배달시작", Toast.LENGTH_LONG).show();
                            if (dbRef != null){
                                Map<String, Object> updatedStatus = new HashMap<>();
                                updatedStatus.put("status", 2);
                                dbRef.document(order.getId()).update(updatedStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                order.setStatus(2);
                                                Toast.makeText(mContext, "배달시작", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    }
                    else if (order.getMode() == 2){
                        //user  has clicked rejecting order
                        Toast.makeText(mContext, "주문 거절", Toast.LENGTH_LONG).show();
                        if (dbRef != null ){
                            dbRef.document(order.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(mContext, "주문 취소 완료", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }

                    order.setMode(0);
                    editSelected.setText("");

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                }

            }
            notifyItemChanged(getLayoutPosition());
        }
    }
}
