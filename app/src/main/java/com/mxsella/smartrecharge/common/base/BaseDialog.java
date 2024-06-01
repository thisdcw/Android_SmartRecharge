package com.mxsella.smartrecharge.common.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.utils.LogUtil;

public abstract class BaseDialog<T extends ViewDataBinding> extends DialogFragment {

    public T binding;

    public Context context;

    public abstract void initEventAndData();

    public abstract int getLayout();

    public void onCreated(Bundle savedInstanceState) {

    }

    public void onStarted() {

    }

    public void onStopped() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(context, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            binding = DataBindingUtil.inflate(inflater, getLayout(), null, false);
        } catch (Exception e) {
            LogUtil.e("error layout -> " + e.getMessage());
            return null;
        }
        initView();
        return binding.getRoot();
    }

    public void initView() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding != null) {
            onCreated(savedInstanceState);
            initEventAndData();
            onStarted();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.context = context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onStopped();
        context = null;
        binding = null;
    }
}
