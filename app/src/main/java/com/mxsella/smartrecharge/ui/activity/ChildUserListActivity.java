package com.mxsella.smartrecharge.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityChildUserBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
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

    private final UserViewModel userViewModel = new UserViewModel();
    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;
    private int cur = 1;

    private int size = 20;

    private ChildUserAdapter childUserAdapter;

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();
    private InputDialog userRechargeDialog;

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
        binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rv.setAdapter(childUserAdapter);
        RefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            cur = 1;
            size = 20;
            userViewModel.getChildUser(cur, size);
            refreshlayout.finishRefresh(FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            size += 20;
            userViewModel.getChildUser(cur, size);
            refreshlayout.finishLoadMore(LOAD_DELAY);//传入false表示加载失败
        });
        userViewModel.getChildUser(cur, size);

        userViewModel.getListChildUserResult().observe(this, result -> {
            ListResponse<User> data = result.getData();
            if (data == null) {
                binding.empty.setVisibility(View.VISIBLE);
                return;
            }
            List<User> records = data.getRecords();
            if (!records.isEmpty()) {
                childUserAdapter.submitList(records);
            } else {
                binding.empty.setVisibility(View.VISIBLE);
            }
        });

        childUserAdapter.setClickClip(value -> {
            // 复制文本到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("inviteCode", value);
            clipboardManager.setPrimaryClip(clipData);

            ToastUtils.showToast("已复制用户ID到剪切板");
        });

        childUserAdapter.setOnItemLongClickListener((baseQuickAdapter, view, i) -> {
            User item = childUserAdapter.getItem(i);
            if (item == null) {
                return false;
            }
            showUserRechargeDialog(item);
            return true;
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

//                binding.avi.smoothToShow();
            } else {

//                binding.avi.smoothToHide();
            }
        });
    }

    private void showUserRechargeDialog(User user) {
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