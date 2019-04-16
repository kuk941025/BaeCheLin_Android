package com.leaderpower.baechelin_owner_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
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
import com.leaderpower.baechelin_owner_android.Retrofit.Response.ResponseKakao;
import com.leaderpower.baechelin_owner_android.Retrofit.RetroCallBack;
import com.leaderpower.baechelin_owner_android.Retrofit.RetroClient;
import com.leaderpower.baechelin_owner_android.model.Order;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.orderViewHolder> {
    private ArrayList<Order> orderList;
    private Context mContext;
    private CollectionReference dbRef;
    private RetroClient retroClient;
    private OwnerItem owner;
    private final String KAKAO_SENDER = "01024421848";

    public OrderListAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.mContext = context;
        dbRef = null;

        retroClient = RetroClient.getInstance(context).createBaseApi();
    }

    public OrderListAdapter(ArrayList<Order> orderList, Context mContext, CollectionReference dbRef, OwnerItem owner) {
        this.orderList = orderList;
        this.mContext = mContext;
        this.dbRef = dbRef;
        this.owner = owner;

        retroClient = RetroClient.getInstance(mContext).createBaseApi();
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
                orderViewHolder.btnReject.setVisibility(View.VISIBLE);
            }
            else if (item.getStatus() == 1 ){
                orderViewHolder.btnConfirm.setText("준비완료");
                orderViewHolder.btnReject.setVisibility(View.GONE);
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
            final String strMessage = editSelected.getText().toString();
            if (order.getMode() == 0) {
                order.setMode(1);
            } else {
                if (strMessage.equals("")){
                    Toast.makeText(mContext, "빈칸을 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else{
                    if (order.getMode() == 1){
                        //user has clicked accepting order
                        if (order.getStatus() == 0){
                            //new order, let user know the owner has accepted the order
//                            Toast.makeText(mContext, "주문승락", Toast.LENGTH_LONG).show();
                            if (dbRef != null){
                                Map<String, Object> updatedStatus = new HashMap<>();
                                updatedStatus.put("status", 1);
                                dbRef.document(order.getId()).update(updatedStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                order.setStatus(1);
                                                sendAcceptedMessage(order, strMessage);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(mContext, "주문이 정상적으로 처리되지 않았습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });


                            }
                            else {
                                Toast.makeText(mContext, "알수없는 데이터베이스.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            //order in preparation, let user know the delivery has started
//                            Toast.makeText(mContext, "배달시작", Toast.LENGTH_LONG).show();
                            if (dbRef != null){
                                Map<String, Object> updatedStatus = new HashMap<>();
                                updatedStatus.put("status", 2);
                                dbRef.document(order.getId()).update(updatedStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                order.setStatus(2);
                                                sendStartDelieveryMessage(order, strMessage);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(mContext, "주문이 정상적으로 처리되지 않았습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    }
                    else if (order.getMode() == 2){
                        //user  has clicked rejecting order
//                        Toast.makeText(mContext, "주문 거절", Toast.LENGTH_LONG).show();
                        if (dbRef != null ){
                            dbRef.document(order.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(mContext, "주문 취소 완료", Toast.LENGTH_LONG).show();
                                            sendRejectedMessage(order, strMessage);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(mContext, "주문이 정상적으로 처리되지 않았습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
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

        private void sendStartDelieveryMessage(Order order, String strMessage){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("tmp_number", "7854");
            parameters.put("kakao_sender", KAKAO_SENDER);
            parameters.put("kakao_name", "고객");
            parameters.put("kakao_phone", "01024421848");
            parameters.put("kakao_add1", strMessage + "분");
            parameters.put("kakao_add2", sdf.format(order.getCreated_at()));
            parameters.put("kakao_add3", owner.getShop_name());
            parameters.put("kakao_add4", order.getFood_ordered());
            parameters.put("kakao_080", "N");

            sendMessage(parameters);
        }
        private void sendRejectedMessage(Order order, String strMessage){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("tmp_number", "7858");
            parameters.put("kakao_sender", KAKAO_SENDER);
            parameters.put("kakao_name", "고객");
            parameters.put("kakao_phone", "01024421848");
            parameters.put("kakao_add1", "[" + strMessage + "]");
            parameters.put("kakao_add2", sdf.format(order.getCreated_at()));
            parameters.put("kakao_add3", owner.getShop_name());
            parameters.put("kakao_add4", order.getFood_ordered());
            parameters.put("kakao_add5", order.getAddress());

            sendMessage(parameters);
        }
        private void sendAcceptedMessage(Order order, String strMessage){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("tmp_number", "7853");
            parameters.put("kakao_sender", KAKAO_SENDER);
            parameters.put("kakao_name", "고객");
            parameters.put("kakao_phone", "01024421848");
            parameters.put("kakao_add1", strMessage + "분");
            parameters.put("kakao_add2", sdf.format(order.getCreated_at()));
            parameters.put("kakao_add3", owner.getShop_name());
            parameters.put("kakao_add4", order.getFood_ordered());
            parameters.put("kakao_add5", order.getAddress());
            parameters.put("kakao_080", "N");

            sendMessage(parameters);
        }

        private void sendMessage(HashMap<String, Object> params){
            retroClient.postSendKakao(params, new RetroCallBack() {
                @Override
                public void onError(Throwable t) {
                    Log.d("TAG", t.getMessage());
                    Toast.makeText(mContext, "카카오 알림 메세지 알수없는 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int code, Object receivedData) {
                    ResponseKakao responseKakao = (ResponseKakao) receivedData;
                    Log.d("TAG", code + " " + responseKakao.response_code);
                    Toast.makeText(mContext, "고객님에게 카카오 알림 메세지를 발송했습니다.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int code) {
                    Log.d("TAG", code + ".");
                    Toast.makeText(mContext, "카카오 알림 발송실패. 오류코드: " + code, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
