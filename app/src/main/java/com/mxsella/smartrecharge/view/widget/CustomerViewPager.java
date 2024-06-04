package com.mxsella.smartrecharge.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomerViewPager extends ViewPager {
    //是否可以滚动切换
    private boolean isCanScroll = false;
    //是否开启切换动画
    private boolean smoothScrollEnabled = false;

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    public boolean isSmoothScrollEnabled() {
        return smoothScrollEnabled;
    }

    public void setSmoothScrollEnabled(boolean smoothScrollEnabled) {
        this.smoothScrollEnabled = smoothScrollEnabled;
    }

    public CustomerViewPager(Context context) {
        super(context);
    }

    public CustomerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        if (smoothScrollEnabled) {
            super.setCurrentItem(item, true); // enable smooth scroll
        } else {
            super.setCurrentItem(item, false); // disable smooth scroll
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }
}
