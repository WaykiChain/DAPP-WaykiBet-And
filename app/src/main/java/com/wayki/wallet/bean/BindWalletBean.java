package com.wayki.wallet.bean;

public class BindWalletBean {

    /**
     * customerId : 1
     * id : 12
     * primary : 0
     * walletMnecode : sdfjkasdkfj2lsdfsdfsddxsdfdsdfsd34234
     * wiccAddress : W515145124112gjyhfdgrdkhkjhjfggfsdyh
     */

    private String walletMnecode;
    private String wiccAddress;

    public String getWalletMnecode() {
        return walletMnecode;
    }

    public void setWalletMnecode(String walletMnecode) {
        this.walletMnecode = walletMnecode;
    }

    public String getWiccAddress() {
        return wiccAddress;
    }

    public void setWiccAddress(String wiccAddress) {
        this.wiccAddress = wiccAddress;
    }
}
