package com.leaderpower.baechelin_owner_android.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.leaderpower.baechelin_owner_android.fragment.OrderCompleteFragment;
import com.leaderpower.baechelin_owner_android.fragment.OrderInProgressFragment;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.util.ArrayList;
import java.util.List;

public class OrderPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private final String[] ORDER_TAB_TITLE = {"신규·처리중", "주문 기록"};
    private OwnerItem owner;


    public OrderPageAdapter(FragmentManager fm, OwnerItem owner) {
        super(fm);

        this.owner = owner;

        initFrags();
    }

    private void initFrags(){
        fragmentList = new ArrayList<>();

        //send oid to fragment
        Bundle progressBundle = new Bundle();
        progressBundle.putSerializable("owner", owner);

        OrderInProgressFragment progressFragment = new OrderInProgressFragment();
        progressFragment.setArguments(progressBundle);


        Bundle completeBundle = new Bundle();
        completeBundle.putString("oid", owner.getOid());
        OrderCompleteFragment completeFragment = new OrderCompleteFragment();
        completeFragment.setArguments(completeBundle);

        fragmentList.add(progressFragment);
        fragmentList.add(completeFragment);
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
