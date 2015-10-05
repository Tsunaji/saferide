package com.digitopolis.saferide;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bank on 8/3/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList ;

    public TabPagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        fragmentList = list;
        // TODO Auto-generated constructor stub
    }

    public TabPagerAdapter(FragmentManager fm) {

        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void setFragmentList(List<Fragment> fragmentList){
        this.fragmentList = fragmentList;
    }
}
