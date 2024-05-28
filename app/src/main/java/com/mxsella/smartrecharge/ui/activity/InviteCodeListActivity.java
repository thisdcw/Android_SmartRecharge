package com.mxsella.smartrecharge.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityInviteCodeListBinding;
import com.mxsella.smartrecharge.ui.adapter.InviteRecodeAdapter;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.InviteCodeDialog;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class InviteCodeListActivity extends BaseActivity<ActivityInviteCodeListBinding> {

    private InviteRecodeAdapter adapter;

    private final UserViewModel userViewModel = new UserViewModel();

    private int cur = 1;

    private int size = 20;

    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;

    private InviteCodeDialog codeDialog;

    @Override
    public void initView() {
        RefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            cur = 1;
            size = 20;
            userViewModel.getInviteCodeList(cur, size);
            refreshlayout.finishRefresh(FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            size += 20;
            userViewModel.getInviteCodeList(cur, size);
            refreshlayout.finishLoadMore(LOAD_DELAY);//传入false表示加载失败
        });
        adapter = new InviteRecodeAdapter();
        binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rv.setAdapter(adapter);

        userViewModel.getInviteRecordListResponse().observe(this, response -> {
            LogUtil.d("我的邀请码 -> " + response.getRecords());
            adapter.submitList(response.getRecords());
        });
        userViewModel.getInviteCodeList(cur, size);

        binding.navBar.getRightImageView().setOnClickListener(v -> showAddInviteCodeDialog());

        userViewModel.getCreateCodeState().observe(this, isCreate -> {
            if (isCreate) {
                ToastUtils.showToast("邀请码创建成功");
                userViewModel.getInviteCodeList(cur, size);
            } else {
                ToastUtils.showToast("邀请码创建失败");
            }
        });

        adapter.setClickClip(s -> {
            // 复制文本到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("inviteCode", s);
            clipboardManager.setPrimaryClip(clipData);

            ToastUtils.showToast("已复制邀请码到剪切板");
        });
    }

    private void showAddInviteCodeDialog() {
        codeDialog = new InviteCodeDialog();
        codeDialog.setDialogListener(new InviteCodeDialog.DialogListener() {
            @Override
            public void onConfirmClick() {
                String subName = codeDialog.getSubName();
                if (subName == null || subName.trim().isEmpty()) {
                    ToastUtils.showToast("品牌商名称有误");
                    codeDialog.dismiss();
                    return;
                }
                userViewModel.createInviteCode(subName);
            }

            @Override
            public void onCancelClick() {
            }
        });
        codeDialog.show(getSupportFragmentManager(), "invite_dialog");
    }

    @Override
    public int layoutId() {
        return R.layout.activity_invite_code_list;
    }
}