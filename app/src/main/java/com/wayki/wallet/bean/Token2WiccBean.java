package com.wayki.wallet.bean;

public class Token2WiccBean {

    /**
     * contractAddress : string
     * customerId : 0
     * price : 0
     * token : string
     * tokenAmount : 0
     * txTokenFee : 0
     * wiccAddress : string
     * wiccCount : 0
     */

    private String contractAddress;
    private double price;
    private String token;
    private double tokenAmount;
    private double txTokenFee;
    private String wiccAddress;
    private double wiccCount;
    private int direction;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(double tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public double getTxTokenFee() {
        return txTokenFee;
    }

    public void setTxTokenFee(double txTokenFee) {
        this.txTokenFee = txTokenFee;
    }

    public String getWiccAddress() {
        return wiccAddress;
    }

    public void setWiccAddress(String wiccAddress) {
        this.wiccAddress = wiccAddress;
    }

    public double getWiccCount() {
        return wiccCount;
    }

    public void setWiccCount(double wiccCount) {
        this.wiccCount = wiccCount;
    }
}
