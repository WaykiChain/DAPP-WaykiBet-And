package com.wayki.wallet.bean;

public class Wicc2TokenBean {

    /**
     * coinSymbol : string
     * contractAddress : string
     * rawtx : string
     * txRemark : string
     * wiccAddress : string
     */

    private String coinSymbol;
    private String contractAddress;
    private String rawtx;
    private String txRemark;
    private String wiccAddress;
    private String requestContract;

    public String getRequestContract() {
        return requestContract;
    }

    public void setRequestContract(String requestContract) {
        this.requestContract = requestContract;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getRawtx() {
        return rawtx;
    }

    public void setRawtx(String rawtx) {
        this.rawtx = rawtx;
    }

    public String getTxRemark() {
        return txRemark;
    }

    public void setTxRemark(String txRemark) {
        this.txRemark = txRemark;
    }

    public String getWiccAddress() {
        return wiccAddress;
    }

    public void setWiccAddress(String wiccAddress) {
        this.wiccAddress = wiccAddress;
    }
}
