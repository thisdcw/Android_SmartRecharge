package com.mxsella.smartrecharge.ui.activity;

import android.os.Handler;
import android.view.KeyEvent;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityProductEntryBinding;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.view.dialog.ProductInputDialog;

public class ProductEntryActivity extends BaseActivity<ActivityProductEntryBinding> {

    private final StringBuilder sb = new StringBuilder();
    private Handler myHandler = new Handler();
    boolean isScaning = false;

    boolean isShow = false;
    int len = 0;
    int oldLen = 0;

    private ProductInputDialog productInputDialog;

    @Override
    public void initView() {
        binding.res.setText(getString(R.string.product_id, "test"));
    }

    private void startScan() {
        if (isScaning) {
            return;
        }
        isScaning = true;
        timerScanCal();
    }

    private void timerScanCal() {
        oldLen = len;
        myHandler.postDelayed(() -> {
            if (oldLen != len) {
                timerScanCal();
                return;
            }
            isScaning = false;
            if (sb.length() > 0) {
                LogUtil.i("扫码:" + sb);
                showDialog(sb);
                sb.setLength(0);
            }
        }, 100);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isShow) {
            return super.dispatchKeyEvent(event);
        }
        int action = event.getAction();
        LogUtil.i("event code => " + event.getKeyCode());
        switch (action) {
            case KeyEvent.ACTION_DOWN:
                if (event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_RIGHT) {
                    return super.dispatchKeyEvent(event);
                }
                int unicodeChar = event.getUnicodeChar();
                sb.append((char) unicodeChar);
                LogUtil.i("dispatchKeyEvent: " + unicodeChar + " " + event.getKeyCode());
                len++;
                startScan();
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public void showDialog(StringBuilder id) {
        isShow = true;
        String productId = id.toString();
        productInputDialog = new ProductInputDialog();
        productInputDialog.setProductId(id.toString());
        productInputDialog.setDialogListener(new ProductInputDialog.DialogListener() {
            @Override
            public void onConfirmClick() {
                LogUtil.d("确认上传id: " + productId);
                runOnUiThread(() -> {
                    binding.res.setText(getString(R.string.product_id, productId));
                });
                isShow = false;
            }

            @Override
            public void onCancelClick() {
                LogUtil.d("取消上传id: " + productId);
                isShow = false;
            }
        });

        productInputDialog.show(getSupportFragmentManager(), "customer-input");
    }

    @Override
    public int layoutId() {
        return R.layout.activity_product_entry;
    }
}