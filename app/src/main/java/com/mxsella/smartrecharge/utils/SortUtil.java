package com.mxsella.smartrecharge.utils;

import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.domain.Device;

import java.util.Comparator;
import java.util.List;

public class SortUtil {
    public static void sortByTimeDescending(List<Device> devices) {
        devices.sort((device1, device2) -> device2.getCreateTime().compareTo(device1.getCreateTime()));
    }


    public static void sortByDescending(List<ApplyTimes> applyTimes) {
        applyTimes.sort((apply1, apply2) -> apply2.getCreateTime().compareTo(apply1.getCreateTime()));
    }
}
