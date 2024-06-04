package com.mxsella.smartrecharge.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MessageViewPageAdapter extends FragmentPagerAdapter {

    String[] titleArr;
    List<Fragment> mFragmentList;

    public MessageViewPageAdapter(FragmentManager fm, List<Fragment> list, String[] titleArr) {
        super(fm);
        mFragmentList = list;
        this.titleArr = titleArr;
    }



    @NonNull
    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList != null ? mFragmentList.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleArr[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 不要调用 super 方法，以防止销毁页面
//        super.destroyItem(container, position, object);
    }
}
