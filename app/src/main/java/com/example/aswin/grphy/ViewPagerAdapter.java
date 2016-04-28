package com.example.aswin.grphy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<> ();
    ArrayList<String> tabTitles = new ArrayList<> ();

    public void addFragments(Fragment fragments,String tabTitles)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(tabTitles);
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    public CharSequence getPageTitle(int position) {
       return tabTitles.get(position);
    }

}