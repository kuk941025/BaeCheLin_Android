package com.leaderpower.baechelin_owner_android.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderPageAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ButterKnife.bind(this);

        pageAdapter = new OrderPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

}