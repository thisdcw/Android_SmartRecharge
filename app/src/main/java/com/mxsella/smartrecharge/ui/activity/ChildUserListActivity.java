package com.mxsella.smartrecharge.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.google.gson.Gson;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityChildUserBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.ChildUser;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.ui.adapter.ChildUserAdapter;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.InputDialog;
import com.mxsella.smartrecharge.view.dialog.LoadingDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class ChildUserListActivity extends BaseActivity<ActivityChildUserBinding> {

    private User currentUser = new User();
    private int cur = 1;
    private int size = 20;
    private ChildUserAdapter childUserAdapter;
    private InputDialog userRechargeDialog;
    private InputDialog userModifyDialog;
    private InputDialog changeUserRemarkDialog;
    private RefreshLayout refreshLayout;

    @Override
    public int layoutId() {
        return R.layout.activity_child_user;
    }

    @Override
    public void initView() {
        String json = Config.getCurrentUser();
        currentUser = new Gson().fromJson(json, User.class);
        if (currentUser != null) {
            UserEnum userEnum = Constants.roleMap.get(currentUser.getRole());
            if (userEnum == null) {
                return;
            }
            binding.navBar.getTitleTextView().setText(userEnum.getChildRole());
        }
        childUserAdapter = new ChildUserAdapter();
        binding.rvRefresh.rv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvRefresh.rv.setAdapter(childUserAdapter);
        refreshLayout = binding.rvRefresh.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        getUserList();
    }

    @Override
    public void initObserve() {
        userViewModel.getListChildUserResult().observe(this, result -> {
            ListResponse<ChildUser> data = result.getData();
            if (data == null) {
                binding.rvRefresh.empty.setVisibility(View.VISIBLE);
                return;
            }
            List<ChildUser> records = data.getRecords();
            if (!records.isEmpty()) {
                childUserAdapter.submitList(records);
            } else {
                binding.rvRefresh.empty.setVisibility(View.VISIBLE);
            }
        });
        deviceViewModel.getUserRechargeResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
        deviceViewModel.getLoadingSate().observe(this, loading -> {
            if (loading) {

                binding.rvRefresh.avi.smoothToShow();
            } else {

                binding.rvRefresh.avi.smoothToHide();
            }
        });
        userViewModel.getChangeSubInfo().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                getUserList();
            }
            ToastUtils.showToast(result.getMessage());
        });
    }

    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            cur = 1;
            size = 20;
            getUserList();
            refreshlayout.finishRefresh(Constants.FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            size += 20;
            getUserList();
            refreshlayout.finishLoadMore(Constants.LOAD_DELAY);//传入false表示加载失败
        });
        childUserAdapter.setOnClickListener(user -> {
            LogUtil.d("点击");
            if (user == null) {
                return;
            }
            showUserModifyDialog(user);
        });
        childUserAdapter.setChangeRemarkListener(user -> {
            if (user == null) {
                return;
            }
            showUserRemarkModifyDialog(user);
        });

        childUserAdapter.setClickClip(value -> {
            // 复制文本到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("inviteCode", value);
            clipboardManager.setPrimaryClip(clipData);

            ToastUtils.showToast("已复制用户ID到剪切板");
        });


        childUserAdapter.setOnItemLongClickListener((baseQuickAdapter, view, i) -> {
            ChildUser item = childUserAdapter.getItem(i);
            if (item == null) {
                return false;
            }
            showUserRechargeDialog(item);
            return true;
        });


    }

    private void showUserRemarkModifyDialog(ChildUser childUser) {
        changeUserRemarkDialog = new InputDialog("请输入备注", getString(R.string.user_remark_modify_username, childUser.getRemark()), "请输入备注");
        changeUserRemarkDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                String new_remark = changeUserRemarkDialog.getInput();
                userViewModel.changeSub(childUser.getUid(), childUser.getSubName(), new_remark);
            }

            @Override
            public void onCancel() {

            }
        });
        changeUserRemarkDialog.show(getSupportFragmentManager(), "remark_modify");
    }

    private void getUserList() {
        userViewModel.getChildUser(cur, size);
    }

    private void showUserModifyDialog(ChildUser item) {
        userModifyDialog = new InputDialog("请输入新名称", getString(R.string.user_modify_username, item.getSubName()), null);
        userModifyDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                String new_name = userModifyDialog.getInput();
                userViewModel.changeSub(item.getUid(), new_name, item.getRemark());
            }

            @Override
            public void onCancel() {

            }
        });
        userModifyDialog.show(getSupportFragmentManager(), "sub_modify");
    }

    private void showUserRechargeDialog(ChildUser user) {
        userRechargeDialog = new InputDialog("请输入你要充值的次数", getString(R.string.device_user_recharge_user, user.getUserName()), "请输入次数");
        userRechargeDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                Integer times = Integer.valueOf(userRechargeDialog.getInput());
                deviceViewModel.userRecharge(user.getUid(), times);
            }

            @Override
            public void onCancel() {

            }
        });
        userRechargeDialog.show(getSupportFragmentManager(), "user_recharge");
    }

}