package com.mxsella.smartrecharge.common.db;

import org.litepal.LitePal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RechargeHistoryManager {

    private ExecutorService service = Executors.newSingleThreadExecutor();

    public static RechargeHistoryManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RechargeHistoryManager INSTANCE = new RechargeHistoryManager();
    }

    public List<RechargeHistory> getAllRechargeHistoryByKeyword(String uid, String productName) {
        return LitePal.where("uid = ? and productName = ?", uid, productName).find(RechargeHistory.class);
    }

    public void saveOrUpdate(RechargeHistory rechargeHistory) {
        service.execute(() -> rechargeHistory.saveOrUpdate("id = ?", String.valueOf(rechargeHistory.getId())));
    }

    public List<RechargeHistory> getRechargeHistoryList(int page, int size) {
        if (page < 1)
            page = 1;
        return LitePal.limit(size).offset((page - 1) * size).limit(size).find(RechargeHistory.class);
    }

    public List<RechargeHistory> getAllRechargeHistory() {
        return LitePal.findAll(RechargeHistory.class);
    }

    public boolean deleteByRechargeHistoryId(int rechargeId) {
        return LitePal.deleteAll(RechargeHistory.class, "id = ?", String.valueOf(rechargeId)) != 0;
    }

    public RechargeHistory getHistoryById(String historyId) {
        return LitePal.where("historyId = ?", historyId).findLast(RechargeHistory.class);
    }

    public void deleteAllRechargeHistory() {
        LitePal.deleteAll(RechargeHistory.class);
    }
}
