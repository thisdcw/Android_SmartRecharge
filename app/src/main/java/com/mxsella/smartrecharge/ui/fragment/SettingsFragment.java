package com.mxsella.smartrecharge.ui.fragment;

import android.view.View;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentSettingsBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.ui.activity.ChildUserListActivity;
import com.mxsella.smartrecharge.ui.activity.DeviceListActivity;
import com.mxsella.smartrecharge.ui.activity.InviteCodeListActivity;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.InputDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> {

    private User currentUser = new User();

    private InputDialog productDialog;

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    @Override
    public void initEventAndData() {

        binding.lltInviteRecord.setOnClickListener(v -> {
            LogUtil.d("1");
            navTo(InviteCodeListActivity.class);
        });

        binding.lltChildrenUser.setOnClickListener(v -> {
            navTo(ChildUserListActivity.class);
        });

        binding.lltDevice.setOnClickListener(v -> {
            navTo(DeviceListActivity.class);
        });

        binding.lltMyProduct.setOnClickListener(v -> {
            showSetProductNameDialog();
        });
        binding.lltRemainTimes.setOnClickListener(v -> {
            deviceViewModel.getUserTimes(binding.productName.getText().toString().trim());
        });
        deviceViewModel.getGetUserTimesResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                binding.remainTimes.setText(result.getMessage());
                ToastUtils.showToast(result.getMessage());
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
        binding.productName.setText(Config.getProductName());
        String json = Config.getCurrentUser();
        currentUser = new Gson().fromJson(json, User.class);
        if (currentUser != null) {
            if (currentUser.getRole().equals(Constants.ROLE_STORE)) {
                binding.lltChildrenUser.setVisibility(View.GONE);
                binding.lltInviteRecord.setVisibility(View.GONE);
            }
            UserEnum userEnum = Constants.roleMap.get(currentUser.getRole());
            if (userEnum != null) {
                binding.group.setText(userEnum.getChildRole());
                binding.role.setText(userEnum.getCn());
            }
            binding.username.setText(currentUser.getUserName());
            binding.civ.setImageUrl(currentUser.getAvatar());
        }
        deviceViewModel.getLoadingSate().observe(this,loading->{
            if (loading){
                binding.avi.show();
            }else {
                binding.avi.hide();
            }
        });
    }

    private void showSetProductNameDialog() {

        productDialog = new InputDialog("当前管理的产品名",null,"请输入产品名称");
        productDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                String productName = productDialog.getInput();
                binding.productName.setText(productName);
                Config.saveProductName(productName);
            }

            @Override
            public void onCancel() {

            }
        });
        productDialog.show(getChildFragmentManager(), "set_product_name");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settings;
    }
}