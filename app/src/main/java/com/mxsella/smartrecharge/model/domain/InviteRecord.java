package com.mxsella.smartrecharge.model.domain;

public class InviteRecord {

    private String codeId;

    private String uid;

    private String createTime;

    private String code;

    private Integer codeState;

    private String codeUid;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCodeState() {
        return codeState;
    }

    public void setCodeState(Integer codeState) {
        this.codeState = codeState;
    }

    public String getCodeUid() {
        return codeUid;
    }

    public void setCodeUid(String codeUid) {
        this.codeUid = codeUid;
    }

    @Override
    public String toString() {
        return "InviteRecord{" +
                "codeId='" + codeId + '\'' +
                ", uid='" + uid + '\'' +
                ", createTime='" + createTime + '\'' +
                ", code='" + code + '\'' +
                ", codeState=" + codeState +
                ", codeUid='" + codeUid + '\'' +
                '}';
    }
}
