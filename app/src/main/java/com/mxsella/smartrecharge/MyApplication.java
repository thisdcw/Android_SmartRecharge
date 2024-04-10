package com.mxsella.smartrecharge;

import android.app.Application;
import android.content.Context;

import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.AGConnectOptionsBuilder;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.common.Config;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import java.io.IOException;

import me.jessyan.autosize.AutoSizeConfig;

public class MyApplication extends Application {
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            layout.setPrimaryColorsId(R.color.primary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AutoSizeConfig.getInstance().setExcludeFontScale(true).setUseDeviceSize(true);
        try {
            AGConnectOptionsBuilder builder = new AGConnectOptionsBuilder();
            builder.setInputStream(getAssets().open("agconnect-services.json"));
            AGConnectInstance.initialize(this, builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BleService.getInstance().init(instance);
        Config.mainSP = this.getSharedPreferences("main", 0);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
