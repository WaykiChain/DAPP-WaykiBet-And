package com.wayki.wallet.bean;

public class WalletBalanceBean {
    private String b_wicc;
    private String b_wusd;
    private String b_amount;
    private String wusd_price;

    public String getWusd_price() {
        return wusd_price;
    }

    public void setWusd_price(String wusd_price) {
        this.wusd_price = wusd_price;
    }

    public String getB_wicc() {
        return b_wicc;
    }

    public void setB_wicc(String b_wicc) {
        this.b_wicc = b_wicc;
    }

    public String getB_wusd() {
        return b_wusd;
    }

    public void setB_wusd(String b_wusd) {
        this.b_wusd = b_wusd;
    }

    public String getB_amount() {
        return b_amount;
    }

    public void setB_amount(String b_amount) {
        this.b_amount = b_amount;
    }
}
