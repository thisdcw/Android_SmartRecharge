package com.mxsella.smartrecharge.model.response;

import java.util.List;

public class ListResponse<T> {
    private int total;

    private int size;

    private int current;

    private int pages;

    private List<T> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "ListResponse{" +
                "total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", pages=" + pages +
                ", records=" + records +
                '}';
    }
}
