package com.wayki.wallet.bean.entity;

import java.util.ArrayList;

public class GetContractEntity {

    /**
     * code : 0
     * data : [{"active":0,"adminAddress":"string","coinSymbol":"string","contractAddress":"string","contractAddressRegId":"string","createdAt":"2018-09-29T10:08:54.977Z","id":0,"updatedAt":"2018-09-29T10:08:54.977Z"}]
     * msg : string
     */

    private int code;
    private String msg;
    private ArrayList<DataBean> data;

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

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * active : 0
         * adminAddress : string
         * coinSymbol : string
         * contractAddress : string
         * contractAddressRegId : string
         * createdAt : 2018-09-29T10:08:54.977Z
         * id : 0
         * updatedAt : 2018-09-29T10:08:54.977Z
         */

        private int active;
        private String adminAddress;
        private String coinSymbol;
        private String contractAddress;
        private String contractAddressRegId;
        private String createdAt;
        private int id;
        private String updatedAt;

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public String getAdminAddress() {
            return adminAddress;
        }

        public void setAdminAddress(String adminAddress) {
            this.adminAddress = adminAddress;
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

        public String getContractAddressRegId() {
            return contractAddressRegId;
        }

        public void setContractAddressRegId(String contractAddressRegId) {
            this.contractAddressRegId = contractAddressRegId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
