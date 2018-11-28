package com.wayki.wallet.bean.entity;

import java.util.List;

public class RewardEntity {

    /**
     * code : 0
     * data : [{"address":"string","amount":0,"coinSymbol":"string","createdAt":"2018-10-23T05:47:33.690Z","customerId":0,"finishedAt":"2018-10-23T05:47:33.690Z","id":0,"requestUuid":"string","status":0,"sysCoinRewardType":0,"txid":"string","updatedAt":"2018-10-23T05:47:33.690Z"}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : string
         * amount : 0
         * coinSymbol : string
         * createdAt : 2018-10-23T05:47:33.690Z
         * customerId : 0
         * finishedAt : 2018-10-23T05:47:33.690Z
         * id : 0
         * requestUuid : string
         * status : 0
         * sysCoinRewardType : 0
         * txid : string
         * updatedAt : 2018-10-23T05:47:33.690Z
         */

        private String address;
        private int amount;
        private String coinSymbol;
        private String createdAt;
        private int customerId;
        private String finishedAt;
        private int id;
        private String requestUuid;
        private int status;
        private int sysCoinRewardType;
        private String txid;
        private String updatedAt;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCoinSymbol() {
            return coinSymbol;
        }

        public void setCoinSymbol(String coinSymbol) {
            this.coinSymbol = coinSymbol;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getFinishedAt() {
            return finishedAt;
        }

        public void setFinishedAt(String finishedAt) {
            this.finishedAt = finishedAt;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRequestUuid() {
            return requestUuid;
        }

        public void setRequestUuid(String requestUuid) {
            this.requestUuid = requestUuid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSysCoinRewardType() {
            return sysCoinRewardType;
        }

        public void setSysCoinRewardType(int sysCoinRewardType) {
            this.sysCoinRewardType = sysCoinRewardType;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
