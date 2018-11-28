package com.wayki.wallet.bean;

public class TransferBean {

    /**
     * rawtx : string
     * txRemark : string
     * type : 0
     * wiccAddress : string
     */

    private String rawtx;
    private String txRemark;
    private int type;
    private String wiccAddress;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWiccAddress() {
        return wiccAddress;
    }

    public void setWiccAddress(String wiccAddress) {
        this.wiccAddress = wiccAddress;
    }
}
