package com.mxsella.smartrecharge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.mxsella.smartrecharge.MyApplication;
import com.mxsella.smartrecharge.R;

public class CustomerImageView extends ShapeableImageView {
    private static final RequestOptions OPTIONS_LOCAL = new RequestOptions()
            .placeholder(R.drawable.ic_default_avatar)
            .fallback(R.drawable.ic_default_avatar)
            .error(R.drawable.ic_default_avatar)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true);

    public CustomerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @BindingAdapter("url")
    public static void setUrl(ImageView imageView, String url) {
        Glide.with(MyApplication.getInstance()).load(url).apply(OPTIONS_LOCAL).into(imageView);
    }
    public void setImageUrl(String url) {
        Glide.with(getContext()).load(url).apply(OPTIONS_LOCAL).into(this);
    }
}
