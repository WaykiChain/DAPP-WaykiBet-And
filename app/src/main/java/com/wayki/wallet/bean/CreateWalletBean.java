package com.wayki.wallet.bean;

import core.JBtWallet;

public class CreateWalletBean {
    private String words;
    private String address;
    private  String privatekey;
    private String hash;
    private String tip;
    private JBtWallet jBtWallet;
    private String creata_import;

    public JBtWallet getjBtWallet() {
        return jBtWallet;
    }

    public void setjBtWallet(JBtWallet jBtWallet) {
        this.jBtWallet = jBtWallet;
    }

    public String getCreata_import() {
        return creata_import;
    }

    public void setCreata_import(String creata_import) {
        this.creata_import = creata_import;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
