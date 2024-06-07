package com.mxsella.smartrecharge.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityDeviceListBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.Device;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.ui.adapter.DeviceListAdapter;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.SortUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.DeviceInfoDialog;
import com.mxsella.smartrecharge.view.dialog.InputDialog;
import com.mxsella.smartrecharge.view.dialog.LoadingDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;
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
    private InputDialog deviceSearchDialog;
    private InputDialog deviceAddDialog;
    private DeviceInfoDialog deviceInfoDialog;
    private InputDialog brandUpdateDialog;
    private int size = 20;
    private String newDeviceId = null;
    private String userRole = null;
    private User currentUser;
    private final UserViewModel userViewModel = new UserViewModel();

    @Override
    public int layoutId() {
        return R.layout.activity_device_list;
    }

    @Override
    public void initView() {
        adapter = new DeviceListAdapter();
        binding.rvRefresh.rv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvRefresh.rv.setAdapter(adapter);
        productName = deviceViewModel.getProductName();
        currentUser = userViewModel.getCurrentUser();
        if (currentUser != null) {
            UserEnum userEnum = Constants.roleMap.get(currentUser.getRole());
            if (userEnum != null) {
                userRole = userEnum.getRole();
                if (userEnum.getRole().equals(Constants.ROLE_ADMIN)) {
                    binding.addDevice.setVisibility(View.VISIBLE);
                }
            }
        }
        getDeviceList();
        RefreshLayout refreshLayout = binding.rvRefresh.refreshLayout;
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

        deviceViewModel.getDeviceList().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ListResponse<Device> data = result.getData();
                List<Device> records = data.getRecords();
                if (!records.isEmpty()) {
                    SortUtil.sortByTimeDescending(records);
                    adapter.submitList(records);
                }else {
                    binding.rvRefresh.empty.setVisibility(View.VISIBLE);
                }
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
            if (productName == null) {
                ToastUtils.showToast("请先设置您的产品名");
                return;
            }
            showAddDeviceDialog();
        });
        deviceViewModel.getAddDeviceResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                getDeviceList();
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            Device item = adapter.getItem(i);
            if (item == null) {
                return;
            }
            deviceInfoDialog = new DeviceInfoDialog(item);
            deviceInfoDialog.show(getSupportFragmentManager(), "device_info");
        });
        adapter.setEditListener((v, position) -> {
            if (userRole == null) {
                return;
            }
            if (userRole.equals(Constants.ROLE_STORE)) {
                ToastUtils.showToast("无权限");
                return;
            }
            Device item = adapter.getItem(position);
            if (item == null) {
                return;
            }
            showUpdateBrandDialog(item.getDeviceId());
        });
        deviceViewModel.getUpdateBrandResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                getDeviceList();
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
        deviceViewModel.getLoadingSate().observe(this, loading -> {
            if (loading) {
                binding.rvRefresh.avi.show();
            } else {
                binding.rvRefresh.avi.hide();
            }
        });
        binding.navBar.getRightImageView().setOnClickListener(v -> {
            binding.search.setVisibility(View.VISIBLE);
            binding.navBar.getRightImageView().setVisibility(View.GONE);
            binding.navBar.getRightTextView().setVisibility(View.VISIBLE);
            binding.navBar.getRightTextView().setText("取消");
        });
        binding.navBar.getRightTextView().setOnClickListener(v -> {
            binding.search.setVisibility(View.GONE);
            binding.navBar.getRightImageView().setVisibility(View.VISIBLE);
            binding.navBar.getRightTextView().setVisibility(View.GONE);
        });
        binding.sure.setOnClickListener(v -> {
            ToastUtils.showToast("此功能暂未开发");
        });
    }


    private void showUpdateBrandDialog(String deviceId) {

        String childRole = Constants.roleMap.get(currentUser.getRole()).getChildRole();
        brandUpdateDialog = new InputDialog(getString(R.string.device_update_product_role, childRole), getString(R.string.device_update_product_name, productName), getString(R.string.device_update_product_role_hint, childRole));
        brandUpdateDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                String targetUid = brandUpdateDialog.getInput();
                if (targetUid == null || targetUid.isEmpty()) {
                    ToastUtils.showToast("请输入有效的id");
                    brandUpdateDialog.dismiss();
                    return;
                }
                deviceViewModel.updateBrand(deviceId, targetUid);
            }

            @Override
            public void onCancel() {

            }
        });

        brandUpdateDialog.show(getSupportFragmentManager(), "brand_update");
    }

    private void getDeviceList() {
        deviceViewModel.getDeviceList(cur, size);
    }

    private void showAddDeviceDialog() {
        deviceAddDialog = new InputDialog("请输入设备id", getString(R.string.device_update_product_name, productName), "请输入设备id");
        deviceAddDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                newDeviceId = deviceAddDialog.getInput();
                if (newDeviceId == null || newDeviceId.isEmpty()) {
                    ToastUtils.showToast("请输入有效的产品名称或设备id");
                    deviceAddDialog.dismiss();
                    return;
                }
                //发起新增设备请求
                deviceViewModel.addDevice(newDeviceId);
            }

            @Override
            public void onCancel() {

            }
        });
        deviceAddDialog.show(getSupportFragmentManager(), "add_device");
    }

    private void showSearchDeviceDialog() {
        deviceSearchDialog = new InputDialog("请输入要搜索的设备id", getString(R.string.device_update_product_name, productName), "请输入设备id");
        deviceSearchDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
        deviceSearchDialog.show(getSupportFragmentManager(), "device_search");
    }
}