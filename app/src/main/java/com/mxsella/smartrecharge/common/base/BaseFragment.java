package com.mxsella.smartrecharge.common.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mxsella.smartrecharge.common.manager.ActivityStackManager;
import com.mxsella.smartrecharge.common.manager.ObserverManager;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    public AppCompatActivity context;
    protected T binding = null;
    public ObserverManager observerManager;

    protected DeviceViewModel deviceViewModel;

    protected UserViewModel userViewModel;

    public abstract void initObserve();

    public abstract void initOnClick();

    public abstract void initView();

    public abstract int getLayoutId();

    public void createView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            observerManager = new ObserverManager(getViewLifecycleOwner());
        } catch (Exception e) {
            LogUtil.e("error layout -> " + e.getMessage());
            return null;
        }
        createView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding != null) {
            deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
            userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            initObserve();
            initView();
            initOnClick();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.context = (AppCompatActivity) context;
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
    public void navTo(Class<? extends Activity> targetActivityClass) {
        ActivityStackManager.getInstance().startActivityNoFinish(context, targetActivityClass);
    }

    public void navTo(Class<? extends Activity> targetActivityClass, Bundle bundle) {
        ActivityStackManager.getInstance().startActivityNoFinish(context, targetActivityClass, bundle);
    }

    public void navToWithFinish(Class<? extends Activity> targetActivityClass) {
        ActivityStackManager.getInstance().startActivity(context, targetActivityClass);
    }

    public void navToFinishAll(Class<? extends Activity> cls) {
        ActivityStackManager.getInstance().finishAllAndStart(context, cls);
    }
}
