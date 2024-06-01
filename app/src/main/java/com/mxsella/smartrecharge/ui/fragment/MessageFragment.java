package com.mxsella.smartrecharge.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentMessageBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.SortUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.MessageViewPageAdapter;
import com.mxsella.smartrecharge.view.dialog.InputDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;

import java.util.ArrayList;

public class MessageFragment extends BaseFragment<FragmentMessageBinding> {

    private final String[] titles = {"我的", "消息"};

    private final ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
        add(new RechargeApplyFragment());
        add(new DealApplyFragment());
    }};
    private InputDialog applyAddDialog;

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initEventAndData() {
        MessageViewPageAdapter adapter = new MessageViewPageAdapter(getChildFragmentManager(), fragments, titles);
        binding.vp.setAdapter(adapter);
        binding.vp.setOffscreenPageLimit(fragments.size());

        binding.segmentTabLayout.setTabData(titles);
        binding.segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                // Handle tab reselection if needed
            }
        });

        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.segmentTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 默认显示第一个Fragment
        binding.vp.setCurrentItem(0);

        binding.addApply.setOnClickListener(v -> {
            showAddApplyDialog();
        });

        deviceViewModel.getApplyTimesResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
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