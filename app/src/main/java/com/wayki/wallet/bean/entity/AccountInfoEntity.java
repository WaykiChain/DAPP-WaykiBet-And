package com.wayki.wallet.bean.entity;

import java.util.List;

public class AccountInfoEntity {


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
         * customerAccountList : [{"createdAt":"2018-10-29 21:53:22","address":"wSLEAeHk6mH3n5XyCkdMJJP9TCH5BWT8ew","coinSymbol":"WICC","priceUSD":0.2136,"balanceReserved":0,"balanceAvailable":0.9999,"memo":"开始高度为:263124","id":1173799,"type":100,"priceCNY":1.4863078748,"status":100,"updatedAt":"2018-10-29 22:11:10"},{"createdAt":"2018-10-29 21:53:23","address":"wSLEAeHk6mH3n5XyCkdMJJP9TCH5BWT8ew","coinSymbol":"WICC_FROZEN","priceUSD":null,"balanceReserved":0,"balanceAvailable":0,"memo":null,"id":1173800,"type":100,"priceCNY":null,"status":100,"updatedAt":"2018-10-29 21:53:23"},{"createdAt":"2018-10-29 21:53:23","address":"wSLEAeHk6mH3n5XyCkdMJJP9TCH5BWT8ew","coinSymbol":"WTEST","priceUSD":null,"balanceReserved":0,"balanceAvailable":0,"memo":null,"id":1173801,"type":100,"priceCNY":0.01,"status":100,"updatedAt":"2018-10-29 21:53:23"}]
         * wicc2WusdRate : 0.2136
         * wiccPriceCNY : 1.4863078748
         * wusd2WiccRate : 4.68164794
         * wiccPriceUSD : 0.2136
         */

        private double wicc2WusdRate;
        private double wiccPriceCNY;
        private double wusd2WiccRate;
        private double wiccPriceUSD;
        private List<CustomerAccountListBean> customerAccountList;

        public double getWicc2WusdRate() {
            return wicc2WusdRate;
        }

        public void setWicc2WusdRate(double wicc2WusdRate) {
            this.wicc2WusdRate = wicc2WusdRate;
        }

        public double getWiccPriceCNY() {
            return wiccPriceCNY;
        }

        public void setWiccPriceCNY(double wiccPriceCNY) {
            this.wiccPriceCNY = wiccPriceCNY;
        }

        public double getWusd2WiccRate() {
            return wusd2WiccRate;
        }

        public void setWusd2WiccRate(double wusd2WiccRate) {
            this.wusd2WiccRate = wusd2WiccRate;
        }

        public double getWiccPriceUSD() {
            return wiccPriceUSD;
        }

        public void setWiccPriceUSD(double wiccPriceUSD) {
            this.wiccPriceUSD = wiccPriceUSD;
        }

        public List<CustomerAccountListBean> getCustomerAccountList() {
            return customerAccountList;
        }

        public void setCustomerAccountList(List<CustomerAccountListBean> customerAccountList) {
            this.customerAccountList = customerAccountList;
        }

        public static class CustomerAccountListBean {
            /**
             * createdAt : 2018-10-29 21:53:22
             * address : wSLEAeHk6mH3n5XyCkdMJJP9TCH5BWT8ew
             * coinSymbol : WICC
             * priceUSD : 0.2136
             * balanceReserved : 0.0
             * balanceAvailable : 0.9999
             * memo : 开始高度为:263124
             * id : 1173799
             * type : 100
             * priceCNY : 1.4863078748
             * status : 100
             * updatedAt : 2018-10-29 22:11:10
             */

            private String createdAt;
            private String address;
            private String coinSymbol;
            private double priceUSD;
            private double balanceReserved;
            private double balanceAvailable;
            private String memo;
            private int id;
            private int type;
            private double priceCNY;
            private int status;
            private String updatedAt;

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCoinSymbol() {
                return coinSymbol;
            }

            public void setCoinSymbol(String coinSymbol) {
                this.coinSymbol = coinSymbol;
            }

            public double getPriceUSD() {
                return priceUSD;
            }

            public void setPriceUSD(double priceUSD) {
                this.priceUSD = priceUSD;
            }

            public double getBalanceReserved() {
                return balanceReserved;
            }

            public void setBalanceReserved(double balanceReserved) {
                this.balanceReserved = balanceReserved;
            }

            public double getBalanceAvailable() {
                return balanceAvailable;
            }

            public void setBalanceAvailable(double balanceAvailable) {
                this.balanceAvailable = balanceAvailable;
            }

            public String getMemo() {
                return memo;
            }

            public void setMemo(String memo) {
                this.memo = memo;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public double getPriceCNY() {
                return priceCNY;
            }

            public void setPriceCNY(double priceCNY) {
                this.priceCNY = priceCNY;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }
        }
    }
}
