package com.mxsella.smartrecharge.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityChildUserBinding;
import com.mxsella.smartrecharge.model.User;
import com.mxsella.smartrecharge.ui.adapter.ChildUserAdapter;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class ChildUserListActivity extends BaseActivity<ActivityChildUserBinding> {

    private User currentUser = new User();

    private UserViewModel userViewModel = new UserViewModel();
    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;
    private int cur = 1;

    private int size = 20;

    private ChildUserAdapter childUserAdapter;

    @Override
    public int layoutId() {
        return R.layout.activity_child_user;
    }

    @Override
    public void initView() {
        String json = Config.getCurrentUser();
        currentUser = new Gson().fromJson(json, User.class);
        if (currentUser != null) {
            binding.navBar.getTitleTextView().setText(Constants.roleMap.get(currentUser.getRole()));
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
            userViewModel.getChildrenUser(cur, size);
            refreshlayout.finishRefresh(FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            size += 20;
            userViewModel.getChildrenUser(cur, size);
            refreshlayout.finishLoadMore(LOAD_DELAY);//传入false表示加载失败
        });
        userViewModel.getChildrenUser(cur, size);

        userViewModel.getListChildrenUser().observe(this, list -> {
            List<User> records = list.getRecords();
            if (!records.isEmpty()) {
                childUserAdapter.submitList(records);
            }
        });

        childUserAdapter.setClickClip(value -> {
            // 复制文本到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("inviteCode", value);
            clipboardManager.setPrimaryClip(clipData);

            ToastUtils.showToast("已复制手机号到剪切板");
        });
    }

}