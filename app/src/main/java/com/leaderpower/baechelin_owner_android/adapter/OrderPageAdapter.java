package com.leaderpower.baechelin_owner_android.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leaderpower.baechelin_owner_android.fragment.OrderCompleteFragment;
import com.leaderpower.baechelin_owner_android.fragment.OrderInProgressFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private final String[] ORDER_TAB_TITLE = {"신규·처리중", "완료"};
    private String oid;

    public OrderPageAdapter(FragmentManager fm, String oid) {
        super(fm);

        this.oid = oid;
        initFrags();
    }
    private void initFrags(){
        fragmentList = new ArrayList<>();

        //send oid to fragment
        Bundle progressBundle = new Bundle();
        progressBundle.putString("oid", this.oid);
        OrderInProgressFragment progressFragment = new OrderInProgressFragment();
        progressFragment.setArguments(progressBundle);

        fragmentList.add(progressFragment);
        fragmentList.add(new OrderCompleteFragment());
    }

    @Override
    public Fragment getItem(int i)  {
//        fragmentList.get(i).setAr
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return ORDER_TAB_TITLE[position];
    }
}
