package com.wayki.wallet.bean.entity;

public class TradeDetailEntity {


    /**
     * code : 0
     * msg : success
     * data : {"amount":1202.3,"comments":"交易测试测试","coinSymbol":"WICC","receiveAddress":"iwrsfsksfwoia3JS9iwefhsz9","txTime":"2018-10-12 11:57:39","minnerFee":0.32,"type":120,"txHash":"812eqjoied238wru82wew","sendAddress":"sdhzisfy8wrkw9fwfkfos9wew","status":100}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : 1202.3
         * comments : 交易测试测试
         * coinSymbol : WICC
         * receiveAddress : iwrsfsksfwoia3JS9iwefhsz9
         * txTime : 2018-10-12 11:57:39
         * minnerFee : 0.32
         * type : 120
         * txHash : 812eqjoied238wru82wew
         * sendAddress : sdhzisfy8wrkw9fwfkfos9wew
         * status : 100
         */

        private String amount;
        private String comments;
        private String coinSymbol;
        private String receiveAddress;
        private String txTime;
        private String minnerFee;
        private String type;
        private String txHash;
        private String sendAddress;
        private int status;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getCoinSymbol() {
            return coinSymbol;
        }

        public void setCoinSymbol(String coinSymbol) {
            this.coinSymbol = coinSymbol;
        }

        public String getReceiveAddress() {
            return receiveAddress;
        }

        public void setReceiveAddress(String receiveAddress) {
            this.receiveAddress = receiveAddress;
        }

        public String getTxTime() {
            return txTime;
        }

        public void setTxTime(String txTime) {
            this.txTime = txTime;
        }

        public String getMinnerFee() {
            return minnerFee;
        }

        public void setMinnerFee(String minnerFee) {
            this.minnerFee = minnerFee;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public String getSendAddress() {
            return sendAddress;
        }

        public void setSendAddress(String sendAddress) {
            this.sendAddress = sendAddress;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
