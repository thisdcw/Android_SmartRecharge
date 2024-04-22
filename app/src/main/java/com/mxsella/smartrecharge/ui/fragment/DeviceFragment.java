package com.mxsella.smartrecharge.ui.fragment;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.common.net.BaseResponse;
import com.mxsella.smartrecharge.common.net.NetworkRepository;
import com.mxsella.smartrecharge.databinding.FragmentDeviceBinding;
import com.mxsella.smartrecharge.model.User;
import com.mxsella.smartrecharge.ui.activity.ManualRechargeActivity;
import com.mxsella.smartrecharge.utils.LogUtil;

import java.util.List;

public class DeviceFragment extends BaseFragment<FragmentDeviceBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    public void initEventAndData() {

        binding.manual.setOnClickListener(v -> {
            navTo(ManualRechargeActivity.class);
        });


        NetworkRepository networkRepository = new NetworkRepository();

        networkRepository.fetchUser("octocat", new ApiResponseHandler<List<BaseResponse<User>>>() {
            @Override
            public void onSuccess(List<BaseResponse<User>> data) {
                for (BaseResponse<User> datum : data) {
                    LogUtil.d(datum.getData().toString());
                }
            }

            @Override
            public void onError(Throwable error) {
                LogUtil.d("出现错误");
            }
        });


    }
}