package com.leaderpower.baechelin_owner_android.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.Retrofit.RetroCallBack;
import com.leaderpower.baechelin_owner_android.Retrofit.RetroClient;
import com.leaderpower.baechelin_owner_android.model.Food;
import com.leaderpower.baechelin_owner_android.model.Order;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetail extends AppCompatActivity {
    @BindView(R.id.detail_layout_request)
    View requestLayout;
    @BindView(R.id.detail_txt_request)
    TextView txtRequest;
    @BindView(R.id.detail_txt_address_jibun)
    TextView txtAddressJibun;
    @BindView(R.id.detail_txt_address_road)
    TextView txtAddressRoad;
    @BindView(R.id.detail_txt_user)
    TextView txtUser;
    @BindView(R.id.detail_linear_layout_order)
    LinearLayout orderLayout;
    @BindView(R.id.detail_liner_layout_order_result)
    LinearLayout orderResultLayout;
    @BindView(R.id.detail_liner_layout_order_table)
    LinearLayout orderInfoLayout;
    @BindView(R.id.detail_layout_menu_total)
    View viewMenuTotal;
    @BindView(R.id.detail_layout_total_price)
    View viewPriceTotal;
    @BindView(R.id.detail_order_info_store_name)
    View viewStoreName;
    @BindView(R.id.detail_order_info_order_id)
    View viewOrderID;
    @BindView(R.id.detail_order_info_order_time)
    View viewOrderTime;
    @BindView(R.id.detail_order_info_payment)
    View viewOrderPayment;
    @BindView(R.id.detail_txt_time)
    TextView txtCreatedAt;
    @BindView(R.id.detail_txt_order_status)
    TextView txtOrderStatus;
    @BindView(R.id.detail_btn_action)
    Button btnAction;

    private OwnerItem owner;
    private Order order;
    private FirebaseFirestore db;
    private DocumentReference orderRef;
    private RetroClient retroClient;
    private final String KAKAO_SENDER = "01024421848";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ButterKnife.bind(this);

        owner = (OwnerItem) getIntent().getSerializableExtra("owner");
        order = (Order) getIntent().getSerializableExtra("order");

        initView();

        retroClient = RetroClient.getInstance(this).createBaseApi();

//        orderRef = db.collection("owner").document(owner.getOid()).collection("order").document(order.getId());
    }

    private void initView() {
        final String colorSecondary = "#F44336";
        final String colorAccent = "#1a7cff";
        final String colorTextPrimary = "#747988";

        //set status bar
        if (order.getStatus().equals("2")) {
            txtCreatedAt.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).format(order.getCreated_at()));
            btnAction.setText("완료");
            btnAction.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_dialog_btn_status_2));
            btnAction.setTextColor(Color.parseColor(colorAccent));
            txtOrderStatus.setText("결제완료");
        } else {
            if (order.getStatus().equals("1")) {
                btnAction.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_dialog_btn_status_1));
                btnAction.setTextColor(Color.parseColor("#FFFFFF"));
            } else if (order.getStatus().equals("0")) {
                btnAction.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_dialog_btn_status_0));
                btnAction.setTextColor(Color.parseColor("#FFFFFF"));
            }

            if (order.getPayment_method() == 2) txtOrderStatus.setText("결제완료 - 앱에서 미리결제");
            else {
                txtOrderStatus.setText("현장결제 " + (order.getPayment_method() == 0 ? "(카드)" : "(현금)"));
            }
            txtCreatedAt.setText(new SimpleDateFormat("HH:mm", Locale.KOREA).format(order.getCreated_at()));
        }


        DecimalFormat df = new DecimalFormat("#,###");
        if (order.getRequest() == "") requestLayout.setVisibility(View.GONE);
        else {
            txtRequest.setText(order.getRequest());
        }

        txtAddressJibun.setText(order.getAddress_jibun() + " " + order.getAddress_detail());
        txtAddressRoad.setText(order.getAddress_road() + " " + order.getAddress_detail());
        txtUser.setText(order.getUser_name());

        //set bill table
        LayoutInflater inflater = LayoutInflater.from(this);
        int food_price = 0;
        for (Food food : order.getFood()) {
            View order_view = (View) inflater.inflate(R.layout.template_bill_table, null, false);
            orderLayout.addView(order_view);

            food_price += Integer.parseInt(food.getPrice()) * Integer.parseInt(food.getCount());
            setBillTableItem(order_view, food.getName(), food.getCount(), df.format(Integer.parseInt(food.getPrice())));
        }

        //set bill result table
        setBillTableItem(viewMenuTotal, "메뉴합계", Integer.toString(order.getFood().size()), Integer.toString(food_price));
        setBillTableItem(viewPriceTotal, "총 결제금액", "", df.format(order.getTotal_price()));


        //set order table item
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        setOrderInfoItem(viewStoreName, "업소명", owner.getShop_name());
        setOrderInfoItem(viewOrderID, "주문번호", order.getId());
        setOrderInfoItem(viewOrderTime, "주문시간", sdf.format(order.getCreated_at()));
        setOrderInfoItem(viewOrderPayment, "결제", (order.getPayment_method() < 2 ? "현장결제" : "앱에서 결제") + " " + df.format(order.getTotal_price()) + "원");

    }


    private void setBillTableItem(View viewTemplate, String item1, String item2, String item3) {
        ((TextView) viewTemplate.findViewById(R.id.template_txt_bill_table_menu)).setText(item1);
        ((TextView) viewTemplate.findViewById(R.id.template_txt_bill_table_quantity)).setText(item2);
        ((TextView) viewTemplate.findViewById(R.id.template_txt_bill_table_price)).setText(item3);
    }

    private void setOrderInfoItem(View viewOrder, String item1, String item2) {
        ((TextView) viewOrder.findViewById(R.id.template_order_table_header)).setText(item1);
        ((TextView) viewOrder.findViewById(R.id.template_order_table_content)).setText(item2);
    }

    @OnClick(R.id.detail_btn_cancel_order)
    void onOrderCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        final EditText editInput = new EditText(this);
        editInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        builder.setView(editInput);

        builder.setTitle("주문 취소").setMessage("선택하신 주문을 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strMessage = editInput.getText().toString();
                        Toast.makeText(OrderDetail.this, strMessage, Toast.LENGTH_SHORT).show();
//                        orderRef.delete()
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        Toast.makeText(OrderDetail.this, "주문이 취소되었습니다.", Toast.LENGTH_LONG).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(OrderDetail.this, "오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                    }
//                                });
                    }
                })
                .setNegativeButton("취소", null);

        builder.show();
    }

    private void sendRejectedMessage(String strMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

        HashMap<String, Object> params = new HashMap<>();
        params.put("temp_number", "7858");
        params.put("kakao_sender", KAKAO_SENDER);
        params.put("kakao_name", "고객");
        params.put("kakao_phone", order.getUser_phone());
        params.put("kakao_add1", "[" + strMessage + "]");
        params.put("kakao_add2", sdf.format(order.getCoupon_amount()));
        params.put("kakao_add3", owner.getShop_name());
        params.put("kakao_add4", order.getFood_ordered());
        params.put("kakao_add5", order.getAddress_road() + " " + order.getAddress_detail());

        retroClient.postSendKakao(params, new RetroCallBack() {
            @Override
            public void onError(Throwable t) {
                Toast.makeText(OrderDetail.this, "카카오 알림 메세지 알수없는 오류: " + t.getMessage(), Toast.LENGTH_LONG).setDuration();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Toast.makeText(OrderDetail.this, "고객님에게 카카오 알림 메세지를 발송했습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(OrderDetail.this, "카카오 알림 발송실패. 오류코드: " + code, Toast.LENGTH_LONG).show();
            }
        });
    }
}
