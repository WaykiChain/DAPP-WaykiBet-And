package com.wayki.wallet.bean;

public class ExchangeBean {
    private String amount1;
    private String amount2;
    private String fee;
    private String pwd;
    private String type;
    private String destAddrid;
    private String destAddr;
    private String rate;

    public String getDestAddrid() {
        return destAddrid;
    }

    public void setDestAddrid(String destAddrid) {
        this.destAddrid = destAddrid;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount1() {
        return amount1;
    }

    public void setAmount1(String amount1) {
        this.amount1 = amount1;
    }

    public String getAmount2() {
        return amount2;
    }

    public void setAmount2(String amount2) {
        this.amount2 = amount2;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
