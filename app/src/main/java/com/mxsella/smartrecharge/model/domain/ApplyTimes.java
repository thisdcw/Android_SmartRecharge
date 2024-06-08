package com.mxsella.smartrecharge.model.domain;

public class ApplyTimes {

    private String applyId;
    private String uid;
    private String parentUid;
    private int productId;
    private String createTime;
    private int applyTimes;
    private int applyState;
    private String operatorUid;
    private String operateTime;
    private String subName;

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getParentUid() {
        return parentUid;
    }

    public void setParentUid(String parentUid) {
        this.parentUid = parentUid;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getApplyTimes() {
        return applyTimes;
    }

    public void setApplyTimes(int applyTimes) {
        this.applyTimes = applyTimes;
    }

    public int getApplyState() {
        return applyState;
    }

    public void setApplyState(int applyState) {
        this.applyState = applyState;
    }

    public String getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(String operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return "ApplyTimes{" +
                "applyId='" + applyId + '\'' +
                ", uid='" + uid + '\'' +
                ", parentUid='" + parentUid + '\'' +
                ", productId=" + productId +
                ", createTime='" + createTime + '\'' +
                ", applyTimes=" + applyTimes +
                ", applyState=" + applyState +
                ", operatorUid='" + operatorUid + '\'' +
                ", operateTime='" + operateTime + '\'' +
                ", subName='" + subName + '\'' +
                '}';
    }
}
