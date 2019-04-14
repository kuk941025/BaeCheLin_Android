package com.leaderpower.baechelin_owner_android.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderPageAdapter;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.order_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.order_toolbar)
    Toolbar toolbar;
    @BindView(R.id.order_view_pager)
    ViewPager viewPager;

    private OrderPageAdapter pageAdapter;
    private OwnerItem owner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ButterKnife.bind(this);

        //Receive owner information from the main activity
        owner = (OwnerItem) getIntent().getSerializableExtra("Owner");

        //send oid to fragment
        pageAdapter = new OrderPageAdapter(getSupportFragmentManager(), owner.getOid());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
//        setToolbar();

    }

//    private void setToolbar(){
//        setSupportActionBar(toolbar);
//        txtToolTitle.setText("회원 정보");
//    }


}
