package com.mxsella.smartrecharge;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.litepal.LitePal;

import java.util.Objects;

import me.jessyan.autosize.AutoSizeConfig;

public class MyApplication extends Application {
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.primary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    private static MyApplication instance;
    private TimeBroadcastReceiver timeBroadcastReceiver;
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AutoSizeConfig.getInstance().setExcludeFontScale(true).setUseDeviceSize(true);
        registerGlobalBroadcastReceiver();
        BleService.getInstance().init(instance);
        LitePal.initialize(this);
        Config.mainSP = this.getSharedPreferences("main", 0);
    }

    private void registerGlobalBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BLE_CONNECT);
        intentFilter.addAction(Constants.BLE_DISCONNECT);
        timeBroadcastReceiver = new TimeBroadcastReceiver();
        registerReceiver(timeBroadcastReceiver, intentFilter);
    }

    private void unregisterGlobalBroadcastReceiver() {
        if (timeBroadcastReceiver != null) {
            unregisterReceiver(timeBroadcastReceiver);
            timeBroadcastReceiver = null;
        }
    }

    class TimeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Constants.BLE_CONNECT:
                    connect();
                    break;
                case Constants.BLE_DISCONNECT:
                    disConnect();
                    break;
                default:
                    LogUtil.d("Unknown action received: " + intent.getAction());
                    break;
            }
        }

    }

    private void connect() {


    }

    private void disConnect() {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterGlobalBroadcastReceiver();
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
