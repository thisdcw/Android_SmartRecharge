package com.mxsella.smartrecharge;

import android.app.Application;

import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.common.Config;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AutoSizeConfig.getInstance().setExcludeFontScale(true).setUseDeviceSize(true);
        BleService.getInstance().init(instance);
        Config.mainSP = this.getSharedPreferences("main", 0);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
