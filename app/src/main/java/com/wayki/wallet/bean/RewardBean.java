package com.wayki.wallet.bean;

public class RewardBean {

    /**
     * address : string
     * sysCoinRewardType : 0
     */

    private String address;
    private int sysCoinRewardType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSysCoinRewardType() {
        return sysCoinRewardType;
    }

    public void setSysCoinRewardType(int sysCoinRewardType) {
        this.sysCoinRewardType = sysCoinRewardType;
    }
}
