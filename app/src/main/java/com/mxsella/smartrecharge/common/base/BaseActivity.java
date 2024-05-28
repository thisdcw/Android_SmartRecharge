package com.mxsella.smartrecharge.common.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.utils.LogUtil;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    public Context mContext;

    public T binding;

    public boolean isConnected = false;

    public abstract int layoutId();

    public abstract void initView();

    private TimeBroadcastReceiver timeBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityStackManager.getInstance().addActivity(this);
        registerBroadcastReceiver();
        try {
            binding = DataBindingUtil.setContentView(this, layoutId());
        } catch (Exception e) {
            LogUtil.e("error layout -> "+e.getMessage());
        }
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        mContext = null;
    }

    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BLE_CONNECT);
        intentFilter.addAction(Constants.BLE_DISCONNECT);
        timeBroadcastReceiver = new TimeBroadcastReceiver();
        registerReceiver(timeBroadcastReceiver, intentFilter);
    }

    class TimeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.BLE_CONNECT:
                    connect();
                    break;
                case Constants.BLE_DISCONNECT:
                    disConnect();
                    break;
            }
        }

    }

    private void connect() {

    }

    private void disConnect() {

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
}
