package com.wayki.wallet.bean.entity;

import java.util.List;

public class TradeHistoryEntity {


    /**
     * code : 0
     * data : [{"amount":0,"coinSymbol":"string","confirmTime":"2018-10-20T03:19:15.620Z","id":0,"refOrderId":0,"status":0,"type":0}]
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
         * amount : 0
         * coinSymbol : string
         * confirmTime : 2018-10-20T03:19:15.620Z
         * id : 0
         * refOrderId : 0
         * status : 0
         * type : 0
         */

        private String amount;
        private String coinSymbol;
        private String confirmTime;
        private Long id;
        private Long refOrderId;
        private int status;
        private int type;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCoinSymbol() {
            return coinSymbol;
        }

        public void setCoinSymbol(String coinSymbol) {
            this.coinSymbol = coinSymbol;
        }

        public String getConfirmTime() {
            return confirmTime;
        }

        public void setConfirmTime(String confirmTime) {
            this.confirmTime = confirmTime;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getRefOrderId() {
            return refOrderId;
        }

        public void setRefOrderId(Long refOrderId) {
            this.refOrderId = refOrderId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
