package com.example.demo;

public class ItemBean {

    private int number;  //方块数字
    private boolean isMerge = false;

    public ItemBean(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isMerge() {
        return isMerge;
    }

    public void setMerge(boolean merge) {
        isMerge = merge;
    }
}
