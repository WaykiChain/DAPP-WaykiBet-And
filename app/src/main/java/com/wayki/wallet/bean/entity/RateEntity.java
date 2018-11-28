package com.wayki.wallet.bean.entity;

public class RateEntity {


    /**
     * code : 0
     * data : {"tokenRmbPrice":0,"tokenUsdPrice":0,"wicc2TokenRate":0,"wicc2TokenRateDown":0,"wicc2TokenRateUp":0}
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
         * tokenRmbPrice : 0
         * tokenUsdPrice : 0
         * wicc2TokenRate : 0
         * wicc2TokenRateDown : 0
         * wicc2TokenRateUp : 0
         */

        private double tokenRmbPrice;
        private double tokenUsdPrice;
        private double wicc2TokenRate;
        private double wicc2TokenRateDown;
        private double wicc2TokenRateUp;

        public double getTokenRmbPrice() {
            return tokenRmbPrice;
        }

        public void setTokenRmbPrice(double tokenRmbPrice) {
            this.tokenRmbPrice = tokenRmbPrice;
        }

        public double getTokenUsdPrice() {
            return tokenUsdPrice;
        }

        public void setTokenUsdPrice(double tokenUsdPrice) {
            this.tokenUsdPrice = tokenUsdPrice;
        }

        public double getWicc2TokenRate() {
            return wicc2TokenRate;
        }

        public void setWicc2TokenRate(double wicc2TokenRate) {
            this.wicc2TokenRate = wicc2TokenRate;
        }

        public double getWicc2TokenRateDown() {
            return wicc2TokenRateDown;
        }

        public void setWicc2TokenRateDown(double wicc2TokenRateDown) {
            this.wicc2TokenRateDown = wicc2TokenRateDown;
        }

        public double getWicc2TokenRateUp() {
            return wicc2TokenRateUp;
        }

        public void setWicc2TokenRateUp(double wicc2TokenRateUp) {
            this.wicc2TokenRateUp = wicc2TokenRateUp;
        }
    }
}
