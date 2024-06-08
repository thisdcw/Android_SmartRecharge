package com.mxsella.smartrecharge.ui.fragment;

import android.os.Build;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentMessageBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.ui.activity.TimesHistoryActivity;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.MessageViewPageAdapter;
import com.mxsella.smartrecharge.view.dialog.InputDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

import java.util.ArrayList;

public class MessageFragment extends BaseFragment<FragmentMessageBinding> {

    private final String[] titles = {"我的", "消息"};
    private final String[] storeTitles = {"我的申请"};
    private final String[] adminTitles = {"我的消息"};

    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private InputDialog applyAddDialog;
    private MessageViewPageAdapter adapter;

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();
    private final UserViewModel userViewModel = new UserViewModel();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initEventAndData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        User user = userViewModel.getCurrentUser();

        String[] title = titles;
        if (user.getRole().equals(UserEnum.STORE.getRole())) {
            fragments.add(new RechargeApplyFragment());
            title = storeTitles;
        } else if (user.getRole().equals(UserEnum.ADMIN.getRole())) {
            fragments.add(new DealApplyFragment());
            title = adminTitles;
        } else {
            fragments.add(new RechargeApplyFragment());
            fragments.add(new DealApplyFragment());
        }
        adapter = new MessageViewPageAdapter(getChildFragmentManager(), fragments, titles);
        binding.vp.setAdapter(adapter);
        binding.st.setViewPager(binding.vp, title);

        binding.st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        binding.addApply.setOnClickListener(v -> {
            showAddApplyDialog();
        });

        deviceViewModel.getApplyTimesResult().observe(this, result -> {
            ToastUtils.showToast(result.getMessage());
        });

    }

    public void success() {

    }

    public void failed() {

    }

    private void showAddApplyDialog() {
        applyAddDialog = new InputDialog("请输入你要请求的次数", null, "请输入次数");

        applyAddDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                int times = Integer.parseInt(applyAddDialog.getInput());
                deviceViewModel.applyTimes(times);
            }

            @Override
            public void onCancel() {

            }
        });

        applyAddDialog.show(getChildFragmentManager(), "add_apply");
    }
}