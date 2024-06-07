package com.mxsella.smartrecharge.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityInviteCodeListBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.InviteRecord;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.ui.adapter.InviteRecodeAdapter;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.InputDialog;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class InviteCodeListActivity extends BaseActivity<ActivityInviteCodeListBinding> {

    private InviteRecodeAdapter adapter;

    private final UserViewModel userViewModel = new UserViewModel();

    private int cur = 1;

    private int size = 20;

    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;

    private InputDialog codeDialog;

    @Override
    public void initView() {
        RefreshLayout refreshLayout = binding.rvRefresh.refreshLayout;
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
        binding.rvRefresh.rv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvRefresh.rv.setAdapter(adapter);

        userViewModel.getInviteCodeList().observe(this, response -> {
            if (response.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(response.getMessage());
                List<InviteRecord> records = response.getData().getRecords();
                if (!records.isEmpty()) {
                    adapter.submitList(records);
                }else {
                    binding.rvRefresh.empty.setVisibility(View.VISIBLE);
                }
            }

        });
        userViewModel.getInviteCodeList(cur, size);

        binding.navBar.getRightImageView().setOnClickListener(v -> showAddInviteCodeDialog());

        userViewModel.getCreateCodeResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                userViewModel.getInviteCodeList(cur, size);
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });

        adapter.setClickClip(s -> {
            // 复制文本到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("inviteCode", s);
            clipboardManager.setPrimaryClip(clipData);

            ToastUtils.showToast("已复制邀请码到剪切板");
        });
        userViewModel.getLoadingSate().observe(this,loading->{
            if (loading){
                binding.rvRefresh.avi.show();
            }else {
                binding.rvRefresh.avi.hide();
            }
        });
    }

    private void showAddInviteCodeDialog() {
        User user = userViewModel.getCurrentUser();
        UserEnum userEnum = Constants.roleMap.get(user.getRole());
        if (userEnum == null) {
            ToastUtils.showToast("出错啦!");
            return;
        }
        codeDialog = new InputDialog("确认生成邀请码吗?", "注意!邀请码的有效期只有一天!", getString(R.string.device_invite_name_hint, userEnum.getChildRole()));
        codeDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                String subName = codeDialog.getInput();
                if (subName == null || subName.trim().isEmpty()) {
                    ToastUtils.showToast("品牌商名称有误");
                    codeDialog.dismiss();
                    return;
                }
                userViewModel.createInviteCode(subName);
            }

            @Override
            public void onCancel() {

            }
        });
        codeDialog.show(getSupportFragmentManager(), "invite_dialog");
    }

    @Override
    public int layoutId() {
        return R.layout.activity_invite_code_list;
    }
}