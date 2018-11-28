package com.wayki.wallet.bean.entity;

public class BindWalletEntity {

    /**
     * code : 0
     * data : {"createdAt":"2018-09-17T06:12:03.763Z","customerId":0,"id":0,"primary":"string","updatedAt":"2018-09-17T06:12:03.763Z","walletMnecode":"string","wiccAddress":"string"}
     * msg : string
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * createdAt : 2018-09-17T06:12:03.763Z
         * customerId : 0
         * id : 0
         * primary : string
         * updatedAt : 2018-09-17T06:12:03.763Z
         * walletMnecode : string
         * wiccAddress : string
         */

        private String createdAt;
        private int customerId;
        private int id;
        private String primary;
        private String updatedAt;
        private String walletMnecode;
        private String wiccAddress;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

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
}
