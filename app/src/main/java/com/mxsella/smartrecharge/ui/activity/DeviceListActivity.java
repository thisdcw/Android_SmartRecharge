package com.mxsella.smartrecharge.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityDeviceListBinding;
import com.mxsella.smartrecharge.model.Device;
import com.mxsella.smartrecharge.ui.adapter.DeviceListAdapter;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.DeviceAddDialog;
import com.mxsella.smartrecharge.view.dialog.DeviceSearchDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class DeviceListActivity extends BaseActivity<ActivityDeviceListBinding> {

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();
    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;
    private int cur = 1;
    private DeviceListAdapter adapter;
    private String productName = null;
    private DeviceSearchDialog deviceSearchDialog;
    private DeviceAddDialog deviceAddDialog;
    private int size = 20;
    private String newProductName = null;
    private String newDeviceId = null;

    @Override
    public int layoutId() {
        return R.layout.activity_device_list;
    }

    @Override
    public void initView() {
        adapter = new DeviceListAdapter();
        binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rv.setAdapter(adapter);

        RefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            if (productName == null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            cur = 1;
            size = 20;
            getDeviceList();
            refreshlayout.finishRefresh(FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            if (productName == null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            size += 20;
            getDeviceList();
            refreshlayout.finishLoadMore(LOAD_DELAY);//传入false表示加载失败
        });

        deviceViewModel.getDeviceList().observe(this, listResponse -> {
            List<Device> records = listResponse.getRecords();
            if (!records.isEmpty()) {
                adapter.submitList(records);
            }
        });
        binding.navBar.getRightImageView().setOnClickListener(v -> {
            showSearchDeviceDialog();
        });
        adapter.setClickClip(value -> {
            // 复制文本到剪贴板
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("deviceId", value);
            clipboardManager.setPrimaryClip(clipData);

            ToastUtils.showToast("已复制设备号到剪切板");
        });
        binding.addDevice.setOnClickListener(v -> {
            showAddDeviceDialog();
        });
        deviceViewModel.getAddDeviceSate().observe(this, isAdd -> {
            if (isAdd) {
                ToastUtils.showToast("新增设备成功");
                getDeviceList();
            }
        });
    }

    private void getDeviceList() {
        deviceViewModel.getDeviceList(productName, cur, size);
    }

    private void showAddDeviceDialog() {
        deviceAddDialog = new DeviceAddDialog();
        deviceAddDialog.setDialogListener(new DeviceAddDialog.DialogListener() {
            @Override
            public void onConfirmClick() {
                newProductName = deviceAddDialog.getProductName();
                newDeviceId = deviceAddDialog.getDeviceId();
                if (newDeviceId == null || newProductName == null || newDeviceId.isEmpty() || newProductName.isEmpty()) {
                    ToastUtils.showToast("请输入有效的产品名称或设备id");
                    deviceAddDialog.dismiss();
                    return;
                }
                //发起新增设备请求
                deviceViewModel.addDevice(newProductName, newDeviceId);
                productName = newProductName;
            }

            @Override
            public void onCancelClick() {

            }
        });
        deviceAddDialog.show(getSupportFragmentManager(), "add_device");
    }

    private void showSearchDeviceDialog() {
        deviceSearchDialog = new DeviceSearchDialog();
        deviceSearchDialog.setDialogListener(new DeviceSearchDialog.DialogListener() {
            @Override
            public void onConfirmClick() {
                productName = deviceSearchDialog.getProductName();
                if (productName == null) {
                    ToastUtils.showToast("请输入正确的产品名称");
                    return;
                }
                getDeviceList();
            }

            @Override
            public void onCancelClick() {

            }
        });
        deviceSearchDialog.show(getSupportFragmentManager(), "device_search");
    }
}