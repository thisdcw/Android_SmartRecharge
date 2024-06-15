package com.mxsella.smartrecharge.ui.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.comm.ICommunicateService;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.comm.ReceivePacket;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.common.db.RechargeHistory;
import com.mxsella.smartrecharge.common.db.RechargeHistoryManager;
import com.mxsella.smartrecharge.databinding.ActivityUseRechargeCodeBinding;
import com.mxsella.smartrecharge.model.domain.RechargeCode;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.ui.adapter.GridItemAdapter;
import com.mxsella.smartrecharge.utils.DateUtils;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;
import java.util.stream.Collectors;

public class UseRechargeCodeActivity extends BaseActivity<ActivityUseRechargeCodeBinding> {

    private int cur = 1;
    private int size = 20;

    private final GridItemAdapter gridItemAdapter = new GridItemAdapter();

    private String productName;
    private CountDownTimer countDownTimer;
    private String deviceMac;
    private boolean isPay = false;
    private String historyId;
    private ICommunicateService iCommunicateService;
    private RechargeHistory rechargeHistory;
    private boolean isCountDown = false;
    private RechargeHistoryManager historyManager;
    private RefreshLayout refreshLayout;

    @Override
    public int layoutId() {
        return R.layout.activity_use_recharge_code;
    }

    @Override
    public void initView() {
        historyManager = RechargeHistoryManager.getInstance();
        productName = deviceViewModel.getProductName();
        deviceMac = Config.getDeviceMac();
        iCommunicateService = BleService.getInstance();
        binding.rv.rv.setLayoutManager(new GridLayoutManager(mContext, 3));
        binding.rv.rv.setAdapter(gridItemAdapter);
        getCodeList();

        refreshLayout = binding.rv.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

    }

    @Override
    public void initObserve() {
        deviceViewModel.getRechargeCodeListResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ListResponse<RechargeCode> data = result.getData();
                List<RechargeCode> records = data.getRecords();
                List<RechargeCode> filterData = records.stream().filter(rechargeCode -> rechargeCode.getRechargeState() == 0).collect(Collectors.toList());
                if (!records.isEmpty()) {
                    gridItemAdapter.submitList(filterData);
                }
            }
            ToastUtils.showToast(result.getMessage());
        });
        deviceViewModel.getUseRechargeCodeResult().observe(this, result -> {
            String message = "充值失败";
            if (result.getResultCode() == ResultCode.SUCCESS) {
                getCodeList();
                rechargeHistory.setCheck(true);
                message = "充值成功";
            }
            saveRechargeHistory();
            ToastUtils.showToast(message);
        });
    }

    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            if (productName == null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            cur = 1;
            size = 20;
            getCodeList();
            refreshlayout.finishRefresh(Constants.FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            if (productName == null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            size += 20;
            getCodeList();
            refreshlayout.finishLoadMore(Constants.LOAD_DELAY);//传入false表示加载失败
        });
        iCommunicateService.setListener(this::handlePacket);

        gridItemAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> charge(gridItemAdapter.getItem(i)));


        binding.navBar.getRightTextView().setOnClickListener(v -> {

            navTo(UseHistoryActivity.class);
        });
    }

    private void handlePacket(ReceivePacket packet) {
        if (packet.getType().equals(ReceivePacket.TYPE_PAY)) {
            isPay = true;
            int remainTimes = packet.getRemainTimes();
            int workTime = packet.getWorkTime();
            rechargeHistory.setPay(true);
            saveRechargeHistory();
            LogUtil.test("支付成功! remainTimes => " + remainTimes + " work time => " + workTime);
        }
    }

    public void getCodeList() {
        deviceViewModel.getRechargeCodeList(cur, size, deviceMac);
    }

    public void charge(RechargeCode rechargeCode) {
        if (isCountDown) {
            ToastUtils.showToast("请等待");
            return;
        }
        RechargeHistory historyById = historyManager.getHistoryById(rechargeCode.getHistoryId());
        if (historyById != null && historyById.isPay()) {
            ToastUtils.showToast("该充值码已使用");
            return;
        }
        String mac = Config.getDeviceMac();
        if (!mac.equals(rechargeCode.getDeviceId())) {
            ToastUtils.showToast("设备不符");
            return;
        }
        if (rechargeHistory != null) {
            saveRechargeHistory();
        }
        rechargeHistory = new RechargeHistory();
        rechargeHistory.setHistoryId(rechargeCode.getHistoryId());
        rechargeHistory.setCreateTime(DateUtils.getNowDateTime());
        rechargeHistory.setProductName(deviceViewModel.getProductName());
        rechargeHistory.setPay(false);
        rechargeHistory.setTimes(rechargeCode.getTimes());
        rechargeHistory.setCheck(false);
        rechargeHistory.setUid(userViewModel.getCurrentUser().getUid());

        historyId = rechargeCode.getHistoryId();
        Config.savePassword(rechargeCode.getRechargePassword());
        countDown();
//        iCommunicateService.send(Protocol.hexToByteArray(rechargeCode.getRechargeCode()));
        iCommunicateService.imitateReceive(Protocol.hexToByteArray(rechargeCode.getRechargeCode()));
    }

    private void saveRechargeHistory() {
        RechargeHistoryManager instance = RechargeHistoryManager.getInstance();
        if (rechargeHistory != null) {
            instance.saveOrUpdate(rechargeHistory);
        }
    }

    private void countDown() {
        isCountDown = true;
        countDownTimer = new CountDownTimer(3 * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                isCountDown = false;
                if (isPay) {
                    //TODO 充值
                    deviceViewModel.useRechargeCode(historyId);
                } else {
                    LogUtil.test("充值失败");
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPay = false;
        saveRechargeHistory();
    }
}