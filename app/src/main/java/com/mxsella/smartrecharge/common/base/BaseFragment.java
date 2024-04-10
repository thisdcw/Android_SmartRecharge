package com.mxsella.smartrecharge.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    public static AppCompatActivity context;
    protected T binding = null;

    public abstract void initEventAndData();

    public abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.context = (AppCompatActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (binding != null) {
            initEventAndData();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        binding = null;
    }

    protected void showMsg(String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    // 封装的跳转方法，接受上下文和目标Activity的类作为参数
    public static void navTo(Class<? extends Activity> targetActivityClass) {
        ActivityStackManager.getInstance().startActivityNoFinish(context, targetActivityClass);
    }

    public static void navTo(Class<? extends Activity> targetActivityClass, Bundle bundle) {
        ActivityStackManager.getInstance().startActivityNoFinish(context, targetActivityClass, bundle);
    }

    public static void navToWithFinish(Class<? extends Activity> targetActivityClass) {
        ActivityStackManager.getInstance().startActivity(context, targetActivityClass);
    }
}
