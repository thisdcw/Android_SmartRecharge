package com.mxsella.smartrecharge.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.mxsella.smartrecharge.R;

public class CustomerNavBar extends FrameLayout {
    private ImageView mIvLeft, mIvRight;
    private TextView mTvTitle, mTvSubTitle, mTvRight, mTvLeft;
    private RelativeLayout mRlRoot;

    /**
     * 显示返回
     */
    private boolean mShowBack;
    /**
     * 显示取消
     */
    private boolean mShowLeftText;
    @DrawableRes
    private int mLeftSrc, mRightSrc;
    @ColorInt
    private int mBackgroundColor, mTitleColor, mLeftColor, mRightTextColor;
    private String mTitle, mSubTitle;


    public CustomerNavBar(@NonNull Context context) {
        this(context, null, 0);
    }

    public CustomerNavBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerNavBar(@NonNull final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // init
        //高度WrapContent
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        styledAttributes.recycle();

        String textRight = null;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.NavBar, defStyleAttr, 0);
            mShowBack = typedArray.getBoolean(R.styleable.NavBar_showBack, false);
            mTitle = typedArray.getString(R.styleable.NavBar_navTitle);
            mTitleColor = typedArray.getColor(R.styleable.NavBar_navTitleColor, 0);
            mLeftColor = typedArray.getColor(R.styleable.NavBar_leftIconColor, 0);
            mShowLeftText = typedArray.getBoolean(R.styleable.NavBar_showLeftText, false);
            mRightTextColor = typedArray.getColor(R.styleable.NavBar_navRightTextColor, 0);
            mSubTitle = typedArray.getString(R.styleable.NavBar_navSubTitle);
            mLeftSrc = typedArray.getResourceId(R.styleable.NavBar_leftIcon, 0);
            mRightSrc = typedArray.getResourceId(R.styleable.NavBar_rightIcon, 0);
            mBackgroundColor = typedArray.getColor(R.styleable.NavBar_navBackground,
                    ContextCompat.getColor(context, R.color.white));
            textRight = typedArray.getString(R.styleable.NavBar_navRightText);
            typedArray.recycle();
        }


        View v = LayoutInflater.from(context).inflate(R.layout.customer_nav_bar, this, false);
        mRlRoot = v.findViewById(R.id.rl_root);
        mTvTitle = v.findViewById(R.id.base_tv_title);
        mTvSubTitle = v.findViewById(R.id.base_tv_sub_title);
        mIvLeft = v.findViewById(R.id.iv_left);
        mTvLeft = v.findViewById(R.id.tv_left);
        mIvRight = v.findViewById(R.id.iv_right);
        mTvRight = v.findViewById(R.id.tv_right);

        if (mTitleColor != 0) {
            mTvTitle.setTextColor(mTitleColor);
        }
        if (mLeftColor != 0) {
            mIvLeft.setColorFilter(mLeftColor);
        }

        v.setBackgroundColor(mBackgroundColor);

        if (mShowBack) {
            mIvLeft.setVisibility(VISIBLE);
            mIvLeft.setImageResource(R.mipmap.back_access);
            mIvLeft.setOnClickListener((view) -> {
                ((Activity) context).onBackPressed();
            });
            if (mShowLeftText) {
                mTvLeft.setVisibility(VISIBLE);
                mTvLeft.setOnClickListener((view) -> {
                    if (context instanceof Activity) {
                        ((Activity) context).onBackPressed();
                    }
                });
            }
        } else {
            if (mLeftSrc != 0) {
                mIvLeft.setVisibility(VISIBLE);
                mIvLeft.setImageResource(mLeftSrc);
            } else {
                mIvLeft.setVisibility(INVISIBLE);
            }
        }

        if (mSubTitle != null) {
            mTvSubTitle.setText(mSubTitle);
            mTvSubTitle.setVisibility(VISIBLE);
        } else {
            mTvSubTitle.setVisibility(GONE);
        }

        if (mRightSrc != 0) {
            mIvRight.setVisibility(VISIBLE);
            mIvRight.setImageResource(mRightSrc);
        } else {
            mIvRight.setVisibility(INVISIBLE);
        }

        if (textRight != null) {
            mTvRight.setVisibility(VISIBLE);
            mTvRight.setText(textRight);
            if (mRightTextColor != 0) {
                mTvRight.setTextColor(mRightTextColor);
            }
        } else {
            mTvRight.setVisibility(GONE);
        }

        mTvTitle.setText(mTitle);

        addView(v);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            measureChildren(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            measureChildren(widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    public RelativeLayout getRoot() {
        return mRlRoot;
    }

    /**
     * 获取左边imageView
     */
    public ImageView getLeftImageView() {
        return mIvLeft;
    }

    /**
     * 获取右边ImageView
     */
    public ImageView getRightImageView() {
        return mIvRight;
    }

    /**
     * 获取标题
     *
     * @return {@link TextView}
     */
    public TextView getTitleTextView() {
        return mTvTitle;
    }

    /**
     * 获取副标题
     */
    public TextView getSubTitleTextView() {
        return mTvSubTitle;
    }

    /**
     * 获取左面的TextView
     */
    public TextView getLeftTextView() {
        return mTvLeft;
    }

    /**
     * 获取右侧文本
     */
    public TextView getRightTextView() {
        return mTvRight;
    }
}
