package com.leaderpower.baechelin_owner_android.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderPageAdapter;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.order_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.order_toolbar)
    Toolbar toolbar;
    @BindView(R.id.order_view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar_txt_title)
    TextView txtToolTitle;
    @BindView(R.id.toolbar_right_btn)
    Button btnTool;
    @BindView(R.id.toolbar_left_btn)
    ImageView btnLeft;

    private OrderPageAdapter pageAdapter;
    private OwnerItem owner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ButterKnife.bind(this);

        //Receive owner information from the main activity
        owner = (OwnerItem) getIntent().getSerializableExtra("Owner");

        //send oid to fragment
//        pageAdapter = new OrderPageAdapter(getSupportFragmentManager(), owner.getOid());
        pageAdapter = new OrderPageAdapter(getSupportFragmentManager(), owner);
        viewPager.setAdapter(pageAdapter);


        tabLayout.setupWithViewPager(viewPager);

        setToolbar();
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        txtToolTitle.setText("주문 현황");
        btnTool.setVisibility(View.GONE);
        btnLeft.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.toolbar_left_btn)
    void onLeftBtnClicked(){
        finish();
    }
}
