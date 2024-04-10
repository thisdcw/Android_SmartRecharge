package com.mxsella.smartrecharge.entity;

import java.io.Serializable;
import java.util.Date;

public class History implements Serializable {

    private int id;
    private Date date;

    private int times;
    private String mac;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", date=" + date +
                ", times=" + times +
                ", mac='" + mac + '\'' +
                '}';
    }
}
