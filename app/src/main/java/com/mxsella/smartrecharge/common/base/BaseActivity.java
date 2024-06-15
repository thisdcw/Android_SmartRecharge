package com.mxsella.smartrecharge.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.mxsella.smartrecharge.common.manager.ActivityStackManager;
import com.mxsella.smartrecharge.common.manager.ObserverManager;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    public Context mContext;

    public DB binding;
    public ObserverManager observerManager;
    protected DeviceViewModel deviceViewModel;

    protected UserViewModel userViewModel;

    public abstract int layoutId();

    public abstract void initView();

    public abstract void initObserve();

    public abstract void initListener();

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
        observerManager = new ObserverManager(this);
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        initObserve();
        initView();
        initListener();
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
