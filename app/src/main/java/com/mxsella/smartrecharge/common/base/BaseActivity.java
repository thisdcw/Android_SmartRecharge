package com.mxsella.smartrecharge.common.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewbinding.ViewBinding;

import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.view.dialog.LoadingDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    public Context mContext;

    public DB binding;

    public abstract int layoutId();

    public abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityStackManager.getInstance().addActivity(this);
        try {
            binding = DataBindingUtil.setContentView(this, layoutId());
        } catch (Exception e) {
            LogUtil.e("error layout -> " + e.getMessage());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
            binding = null;
        }
        mContext = null;
    }

    public void navTo(Class<? extends Activity> cls) {
        ActivityStackManager.getInstance().startActivityNoFinish(this, cls);

    }

    public void navToFinishAll(Class<? extends Activity> cls) {
        ActivityStackManager.getInstance().finishAllAndStart(this, cls);
    }

    public void navToWithFinish(Class<? extends Activity> cls) {
        ActivityStackManager.getInstance().startActivity(this, cls);
    }

    public void navToWithParam(Class<? extends Activity> cls, Bundle bundle) {
        ActivityStackManager.getInstance().startActivity(this, cls, bundle);
    }

    public void finish(Activity activity) {
        ActivityStackManager.getInstance().finishCurrentActivity(activity);
    }
}
