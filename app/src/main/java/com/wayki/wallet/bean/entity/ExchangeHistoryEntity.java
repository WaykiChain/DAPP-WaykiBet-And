package com.wayki.wallet.bean.entity;

import java.util.List;

public class ExchangeHistoryEntity {

    /**
     * code : 0
     * msg : success
     * data : [{"exchangeRate":null,"exchangeTime":null,"wusdAmount":1.5,"wiccAmount":1,"id":53,"walletAddress":"wbmM6mmJbATShuWZqUozFMtpkmevj4o3zc","status":800,"direction":null},{"exchangeRate":1.5,"exchangeTime":null,"wusdAmount":1.5,"wiccAmount":1,"id":54,"walletAddress":"wbmM6mmJbATShuWZqUozFMtpkmevj4o3zc","status":800,"direction":200},{"exchangeRate":0.21,"exchangeTime":"2018-09-28 12:55:55","wusdAmount":0.21,"wiccAmount":1,"id":74,"walletAddress":"wbmM6mmJbATShuWZqUozFMtpkmevj4o3zc","status":800,"direction":200},{"exchangeRate":null,"exchangeTime":"2018-09-28 12:58:30","wusdAmount":null,"wiccAmount":null,"id":75,"walletAddress":"wbmM6mmJbATShuWZqUozFMtpkmevj4o3zc","status":100,"direction":200},{"exchangeRate":0.21,"exchangeTime":"2018-09-28 12:58:50","wusdAmount":0.21,"wiccAmount":1,"id":76,"walletAddress":"wbmM6mmJbATShuWZqUozFMtpkmevj4o3zc","status":800,"direction":200}]
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
         * exchangeRate : null
         * exchangeTime : null
         * wusdAmount : 1.5
         * wiccAmount : 1.0
         * id : 53
         * walletAddress : wbmM6mmJbATShuWZqUozFMtpkmevj4o3zc
         * status : 800
         * direction : null
         */

        private String  exchangeRate;
        private String exchangeTime;
        private Double wusdAmount;
        private String wiccAmount;
        private int id;
        private String walletAddress;
        private int status;
        private String direction;

        public String getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(String exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public String getExchangeTime() {
            return exchangeTime;
        }

        public void setExchangeTime(String exchangeTime) {
            this.exchangeTime = exchangeTime;
        }

        public Double getWusdAmount() {
            return wusdAmount;
        }

        public void setWusdAmount(Double wusdAmount) {
            this.wusdAmount = wusdAmount;
        }

        public String getWiccAmount() {
            return wiccAmount;
        }

        public void setWiccAmount(String wiccAmount) {
            this.wiccAmount = wiccAmount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWalletAddress() {
            return walletAddress;
        }

        public void setWalletAddress(String walletAddress) {
            this.walletAddress = walletAddress;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
}
