package com.leaderpower.baechelin_owner_android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.leaderpower.baechelin_owner_android.activity.OrderDetail;
import com.leaderpower.baechelin_owner_android.dialog.CheckOrderDialog;
import com.leaderpower.baechelin_owner_android.dialog.CheckOrderDialogListener;
import com.leaderpower.baechelin_owner_android.model.Food;
import com.leaderpower.baechelin_owner_android.model.Foods;
import com.leaderpower.baechelin_owner_android.model.Order;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private final String colorAccent = "#1a7cff";
    private final String colorSecondary = "#F44336";
    private final String colorTextPrimary = "#747988";

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
        Order item = orderList.get(i);
        SimpleDateFormat sdf;
        int status;

        try {
            status = Integer.parseInt(item.getStatus());
        } catch (Exception e) {
            status = 0;
        }


        if (status < 2) {
            sdf = new SimpleDateFormat("HH:mm");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ", Locale.KOREA);
        }

        String txtPaymentMethod;
        if (item.getPayment_method() <= 1){
            //후불
            orderViewHolder.txtPaymentMethod.setTextColor(Color.parseColor(colorSecondary));
            txtPaymentMethod = item.getPayment_method() == 0 ? "현장결제(카드)" : "현장결제(현금)";
        }
        else {
            orderViewHolder.txtPaymentMethod.setTextColor(Color.parseColor(colorTextPrimary));
            txtPaymentMethod = "결제완료";
        }
        txtPaymentMethod += " " + df.format(item.getTotal_price()) + "원";
        orderViewHolder.txtPaymentMethod.setText(txtPaymentMethod);


        orderViewHolder.txtAddress.setText(item.getAddress_road() + " " + item.getAddress_detail());
        orderViewHolder.txtFood.setText(item.getFood_ordered());
        orderViewHolder.txtPrice.setText(df.format(item.getTotal_price()) + "원");
        orderViewHolder.txtTime.setText(sdf.format(item.getCreated_at()));


        switch (status) {
            case 0:
                orderViewHolder.statusLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.order_status_0));
                orderViewHolder.txtStatus.setText("요청");
                break;

            case 1:
                orderViewHolder.statusLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.order_status_1));
                orderViewHolder.txtStatus.setText("준비중");
                break;

            case 2:
                orderViewHolder.statusLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.order_status_2));
                orderViewHolder.txtStatus.setText("완료");
                orderViewHolder.txtStatus.setTextColor(Color.parseColor(colorAccent));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class orderViewHolder extends RecyclerView.ViewHolder implements CheckOrderDialogListener {
        @BindView(R.id.template_order_address)
        TextView txtAddress;
        @BindView(R.id.template_order_time)
        TextView txtTime;
        @BindView(R.id.template_order_txt_status)
        TextView txtStatus;
        @BindView(R.id.template_order_price)
        TextView txtPrice;
        @BindView(R.id.template_order_food)
        TextView txtFood;
        @BindView(R.id.template_order_information_layout)
        View viewInfo;
        @BindView(R.id.template_order_main_layout)
        View mainLayout;
        @BindView(R.id.template_order_delivered_at)
        TextView txtDeliveredAt;
        @BindView(R.id.template_order_button_layout)
        View statusLayout;
        @BindView(R.id.template_order_txt_payment_method)
        TextView txtPaymentMethod;


        public orderViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.template_order_button_layout)
        void onOrderStatusClicked() {
            final Order order = orderList.get(getLayoutPosition());
            int status;
            try {
                status = Integer.parseInt(order.getStatus());
            } catch (Exception e) {
                status = 0;
            }
            if (status == 0) {
                CheckOrderDialog orderDialog = new CheckOrderDialog(mContext, this);
                orderDialog.show();
            } else if (status == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
                builder.setTitle("준비 완료").setMessage("배달을 시작하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> updatedStatus = new HashMap<>();
                                updatedStatus.put("status", "2");
                                dbRef.document(order.getId()).update(updatedStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(mContext, "오류 발생. 정상적을 처리되지 않았습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("취소", null);

                builder.show();
            } else {
                Toast.makeText(mContext, "완료된 주문입니다.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onConfirmClicked(final int delivery_time) {
            final Order order = orderList.get(getLayoutPosition());

            order.setDelivery_time(delivery_time);

            //set order time and mode;

            Map<String, Object> updatedStatus = new HashMap<>();
            updatedStatus.put("status", "1");
            dbRef.document(order.getId()).update(updatedStatus)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendAcceptedMessage(order, Integer.toString(delivery_time));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, "오류 발생. 정상적으로 처리되지 않았습니다.", Toast.LENGTH_LONG).show();
                        }
                    });


        }

        @OnClick(R.id.template_order)
        void onOrderClicked() {
            final Order order = orderList.get(getLayoutPosition());
            Intent intent = new Intent(mContext, OrderDetail.class);
            mContext.startActivity(intent);
            if (order.getMode() == 0) {

            } else if (order.getMode() == 1) {

            }
        }

//        public void setTimer(int remaining_time) {
//            timer = new CountDownTimer(remaining_time * 60 * 1000, 1000 * 60) {
//                @Override
//                public void onTick(long l) {
//                    Order order = orderList.get(getLayoutPosition());
//
//                    txtStatus.setText(order.getDelivery_time() - 1 + "분");
//                    order.setDelivery_time(order.getDelivery_time() - 1);
//                }
//
//                @Override
//                public void onFinish() {
//                    orderList.get(getLayoutPosition()).setDelivery_time(0);
//                    txtStatus.setText("0분");
//                }
//            };

//            timer.start();
    }
//        @OnClick(R.id.template_order_reject)
//        void onRejectClicked() {
//            Order order = orderList.get(getLayoutPosition());
//            if (order.getMode() == 0) {
//                //click rejected
//                order.setMode(2);
//            } else {
//                order.setMode(0);
//            }
//            notifyItemChanged(getLayoutPosition());
//        }

//        @OnClick(R.id.template_order_confirm)
//        void onConfirmClicked() {
//            final Order order = orderList.get(getLayoutPosition());
//            final String strMessage = editSelected.getText().toString();
//            if (order.getMode() == 0) {
//                order.setMode(1);
//            } else {
//                if (strMessage.equals("")){
//                    Toast.makeText(mContext, "빈칸을 입력해주세요.", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    if (order.getMode() == 1){
//                        //user has clicked accepting order
//                        int status;
//                        try {
//                            status = Integer.parseInt(order.getStatus());
//                        } catch (Exception e){
//                            status = 0;
//                        }
//
//                        if (status == 0){
//                            //new order, let user know the owner has accepted the order
////                            Toast.makeText(mContext, "주문승락", Toast.LENGTH_LONG).show();
//                            if (dbRef != null){
//                                Map<String, Object> updatedStatus = new HashMap<>();
//                                updatedStatus.put("status", 1);
//                                dbRef.document(order.getId()).update(updatedStatus)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                order.setStatus(Integer.toString(1));
//                                                sendAcceptedMessage(order, strMessage);
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(mContext, "주문이 정상적으로 처리되지 않았습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                            }
//                                        });
//
//
//                            }
//                            else {
//                                Toast.makeText(mContext, "알수없는 데이터베이스.", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        else {
//                            //order in preparation, let user know the delivery has started
////                            Toast.makeText(mContext, "배달시작", Toast.LENGTH_LONG).show();
//                            if (dbRef != null){
//                                Map<String, Object> updatedStatus = new HashMap<>();
//                                updatedStatus.put("status", 2);
//                                updatedStatus.put("delivered_at", new Date());
//                                dbRef.document(order.getId()).update(updatedStatus)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                order.setStatus(Integer.toString(2));
//                                                sendStartDelieveryMessage(order, strMessage);
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(mContext, "주문이 정상적으로 처리되지 않았습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                            }
//                                        });
//                            }
//                        }
//                    }
//                    else if (order.getMode() == 2){
//                        //user  has clicked rejecting order
////                        Toast.makeText(mContext, "주문 거절", Toast.LENGTH_LONG).show();
//                        if (dbRef != null ){
//                            dbRef.document(order.getId()).delete()
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(mContext, "주문 취소 완료", Toast.LENGTH_LONG).show();
//                                            sendRejectedMessage(order, strMessage);
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(mContext, "주문이 정상적으로 처리되지 않았습니다. " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    });
//                        }
//                    }
//                    order.setMode(0);
//                    editSelected.setText("");
//
//                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
//                }
//
//            }
//            notifyItemChanged(getLayoutPosition());
//        }

    private void sendStartDelieveryMessage(Order order, String strMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String food_ordered;

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("tmp_number", "7854");
        parameters.put("kakao_sender", KAKAO_SENDER);
        parameters.put("kakao_name", "고객");
        parameters.put("kakao_phone", "01024421848");
        parameters.put("kakao_add1", strMessage + "분");
        parameters.put("kakao_add2", sdf.format(order.getCreated_at()));
        parameters.put("kakao_add3", owner.getShop_name());
//            parameters.put("kakao_add4", order.getFood_ordered());
        parameters.put("kakao_080", "N");

        sendMessage(parameters);
    }

    private void sendRejectedMessage(Order order, String strMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("tmp_number", "7858");
        parameters.put("kakao_sender", KAKAO_SENDER);
        parameters.put("kakao_name", "고객");
        parameters.put("kakao_phone", "01024421848");
        parameters.put("kakao_add1", "[" + strMessage + "]");
        parameters.put("kakao_add2", sdf.format(order.getCreated_at()));
        parameters.put("kakao_add3", owner.getShop_name());
//            parameters.put("kakao_add4", order.getFood_ordered());
        parameters.put("kakao_add5", order.getAddress_road() + " " + order.getAddress_detail());

        sendMessage(parameters);
    }

    private void sendAcceptedMessage(Order order, String strMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("tmp_number", "7853");
        parameters.put("kakao_sender", KAKAO_SENDER);
        parameters.put("kakao_name", "고객");
        parameters.put("kakao_phone", order.getUser_phone());
        parameters.put("kakao_add1", strMessage + "분");
        parameters.put("kakao_add2", sdf.format(order.getCreated_at()));
        parameters.put("kakao_add3", owner.getShop_name());
        parameters.put("kakao_add4", order.getFood_ordered());
        parameters.put("kakao_add5", order.getAddress_road() + " " + order.getAddress_detail());
        parameters.put("kakao_080", "N");

        sendMessage(parameters);
    }

    private void sendMessage(HashMap<String, Object> params) {
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


