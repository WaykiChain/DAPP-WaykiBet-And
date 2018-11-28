package com.wayki.wallet.bean;

import java.util.List;

public class LockAccountTimeDetailBean {

    /**
     * status : 1
     * error : null
     * result : {"allBonus":0,"lastTotalBonus":0,"result":[{"txid":"18dd09312b03d8101a3bc6b5b0b9b59750479aa138cba49a798b99c1f94e26c1","address":"WhvLin19goWdu6ZeihstjaxJJouK1PAdB4","lastbonus":0,"txtime":1530973917000,"unlocktime":1546525917000,"txmoney":3050,"ratio":3812.5,"selectmonth":null,"totalbonus":0,"mempooltx":false,"txStatus":"1","tlockPositionMonthBonusRecords":null},{"txid":"b9f994896ddc58cbb85eec2f5be7a3e10f15b7c1a53dcf5fcb7672fc85330487","address":"WhvLin19goWdu6ZeihstjaxJJouK1PAdB4","lastbonus":0,"txtime":1530973746000,"unlocktime":1655389746000,"txmoney":3050,"ratio":18300,"selectmonth":null,"totalbonus":0,"mempooltx":false,"txStatus":"1","tlockPositionMonthBonusRecords":null}],"result1":null}
     */

    private int status;
    private Object error;
    private ResultBeanX result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public ResultBeanX getResult() {
        return result;
    }

    public void setResult(ResultBeanX result) {
        this.result = result;
    }

    public static class ResultBeanX {
        /**
         * allBonus : 0
         * lastTotalBonus : 0
         * result : [{"txid":"18dd09312b03d8101a3bc6b5b0b9b59750479aa138cba49a798b99c1f94e26c1","address":"WhvLin19goWdu6ZeihstjaxJJouK1PAdB4","lastbonus":0,"txtime":1530973917000,"unlocktime":1546525917000,"txmoney":3050,"ratio":3812.5,"selectmonth":null,"totalbonus":0,"mempooltx":false,"txStatus":"1","tlockPositionMonthBonusRecords":null},{"txid":"b9f994896ddc58cbb85eec2f5be7a3e10f15b7c1a53dcf5fcb7672fc85330487","address":"WhvLin19goWdu6ZeihstjaxJJouK1PAdB4","lastbonus":0,"txtime":1530973746000,"unlocktime":1655389746000,"txmoney":3050,"ratio":18300,"selectmonth":null,"totalbonus":0,"mempooltx":false,"txStatus":"1","tlockPositionMonthBonusRecords":null}]
         * result1 : null
         */

        private int allBonus;
        private int lastTotalBonus;
        private Object result1;
        private List<ResultBean> result;

        public int getAllBonus() {
            return allBonus;
        }

        public void setAllBonus(int allBonus) {
            this.allBonus = allBonus;
        }

        public int getLastTotalBonus() {
            return lastTotalBonus;
        }

        public void setLastTotalBonus(int lastTotalBonus) {
            this.lastTotalBonus = lastTotalBonus;
        }

        public Object getResult1() {
            return result1;
        }

        public void setResult1(Object result1) {
            this.result1 = result1;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * txid : 18dd09312b03d8101a3bc6b5b0b9b59750479aa138cba49a798b99c1f94e26c1
             * address : WhvLin19goWdu6ZeihstjaxJJouK1PAdB4
             * lastbonus : 0
             * txtime : 1530973917000
             * unlocktime : 1546525917000
             * txmoney : 3050
             * ratio : 3812.5
             * selectmonth : null
             * totalbonus : 0
             * mempooltx : false
             * txStatus : 1
             * tlockPositionMonthBonusRecords : null
             */

            private String txid;
            private String address;
            private int lastbonus;
            private long txtime;
            private long unlocktime;
            private int txmoney;
            private double ratio;
            private Object selectmonth;
            private int totalbonus;
            private boolean mempooltx;
            private String txStatus;
            private Object tlockPositionMonthBonusRecords;

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getLastbonus() {
                return lastbonus;
            }

            public void setLastbonus(int lastbonus) {
                this.lastbonus = lastbonus;
            }

            public long getTxtime() {
                return txtime;
            }

            public void setTxtime(long txtime) {
                this.txtime = txtime;
            }

            public long getUnlocktime() {
                return unlocktime;
            }

            public void setUnlocktime(long unlocktime) {
                this.unlocktime = unlocktime;
            }

            public int getTxmoney() {
                return txmoney;
            }

            public void setTxmoney(int txmoney) {
                this.txmoney = txmoney;
            }

            public double getRatio() {
                return ratio;
            }

            public void setRatio(double ratio) {
                this.ratio = ratio;
            }

            public Object getSelectmonth() {
                return selectmonth;
            }

            public void setSelectmonth(Object selectmonth) {
                this.selectmonth = selectmonth;
            }

            public int getTotalbonus() {
                return totalbonus;
            }

            public void setTotalbonus(int totalbonus) {
                this.totalbonus = totalbonus;
            }

            public boolean isMempooltx() {
                return mempooltx;
            }

            public void setMempooltx(boolean mempooltx) {
                this.mempooltx = mempooltx;
            }

            public String getTxStatus() {
                return txStatus;
            }

            public void setTxStatus(String txStatus) {
                this.txStatus = txStatus;
            }

            public Object getTlockPositionMonthBonusRecords() {
                return tlockPositionMonthBonusRecords;
            }

            public void setTlockPositionMonthBonusRecords(Object tlockPositionMonthBonusRecords) {
                this.tlockPositionMonthBonusRecords = tlockPositionMonthBonusRecords;
            }
        }
    }
}
