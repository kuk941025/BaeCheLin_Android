package com.leaderpower.baechelin_owner_android.adapter;

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
    private final String[] ORDER_TAB_TITLE = {"신규 처리중", "완료"};
    public OrderPageAdapter(FragmentManager fm) {
        super(fm);

        initFrags();
    }
    private void initFrags(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new OrderCompleteFragment());
        fragmentList.add(new OrderInProgressFragment());
    }

    @Override
    public Fragment getItem(int i)  {
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
