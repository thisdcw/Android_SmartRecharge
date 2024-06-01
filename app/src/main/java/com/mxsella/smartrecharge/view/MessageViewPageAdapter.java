package com.mxsella.smartrecharge.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MessageViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private String[] titles;

    public MessageViewPageAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentList = fragments;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
