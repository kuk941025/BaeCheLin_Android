package com.leaderpower.baechelin_owner_android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.model.Food;
import com.leaderpower.baechelin_owner_android.model.Order;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    private OwnerItem owner;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ButterKnife.bind(this);

        owner = (OwnerItem) getIntent().getSerializableExtra("owner");
        order = (Order) getIntent().getSerializableExtra("order");

        initView();
    }

    private void initView() {
        final String colorSecondary = "#F44336";

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
            setBillTableItem(order_view, food.getName(), food.getCount(), df.format(Integer.parseInt(food.getPrice())) + "원");
        }

        //set bill result table
        setBillTableItem(viewMenuTotal, "메뉴합계", "2", Integer.toString(food_price));
        setBillTableItem(viewPriceTotal, "총 결제금액", "", Integer.toString(order.getTotal_price()));


        //set order table item
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd Hh:mm:ss", Locale.KOREA);
        setOrderInfoItem(viewStoreName, "업소명", owner.getShop_name());
        setOrderInfoItem(viewOrderID, "주문번호", order.getId());
        setOrderInfoItem(viewOrderTime, "주문시간", sdf.format(order.getCreated_at()));
        setOrderInfoItem(viewOrderPayment, "결제", (order.getPayment_method() < 2 ? "현장결제" : "앱에서 결제") + " "  + df.format(order.getTotal_price()) + "원");

    }

    private void setBillTableItem(View viewTemplate, String item1, String item2, String item3){
        ((TextView)viewTemplate.findViewById(R.id.template_txt_bill_table_menu)).setText(item1);
        ((TextView)viewTemplate.findViewById(R.id.template_txt_bill_table_quantity)).setText(item2);
        ((TextView)viewTemplate.findViewById(R.id.template_txt_bill_table_price)).setText(item3);
    }

    private void setOrderInfoItem(View viewOrder, String item1, String item2){
        ((TextView)viewOrder.findViewById(R.id.template_order_table_header)).setText(item1);
        ((TextView)viewOrder.findViewById(R.id.template_order_table_content)).setText(item2);
    }

    @OnClick(R.id.detail_btn_cancel_order)
    void onOrderCancel() {

    }
}
