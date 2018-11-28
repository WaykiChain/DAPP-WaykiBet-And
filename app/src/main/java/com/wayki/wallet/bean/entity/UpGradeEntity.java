package com.wayki.wallet.bean.entity;

public class UpGradeEntity {


    /**
     * code : 0
     * msg : success
     * data : {"releasePackageUrl":"http://www.apk.com","languageResourceCode":2,"releasedAt":"2018-08-30 10:58:02","forceUpgrade":-1,"platformType":"android","tabResourceCode":10,"description":"zhi","tabResourceUrl":"http://www.baodu.com","verName":"1.0","languageResourceUrl":"http://www.baidu.com","verCode":10}
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
         * releasePackageUrl : http://www.apk.com
         * languageResourceCode : 2
         * releasedAt : 2018-08-30 10:58:02
         * forceUpgrade : -1
         * platformType : android
         * tabResourceCode : 10
         * description : zhi
         * tabResourceUrl : http://www.baodu.com
         * verName : 1.0
         * languageResourceUrl : http://www.baidu.com
         * verCode : 10
         */

        private String releasePackageUrl;
        private int languageResourceCode;
        private String releasedAt;
        private int forceUpgrade;
        private String platformType;
        private int tabResourceCode;
        private String description;
        private String tabResourceUrl;
        private String verName;
        private String languageResourceUrl;
        private int verCode;

        public String getReleasePackageUrl() {
            return releasePackageUrl;
        }

        public void setReleasePackageUrl(String releasePackageUrl) {
            this.releasePackageUrl = releasePackageUrl;
        }

        public int getLanguageResourceCode() {
            return languageResourceCode;
        }

        public void setLanguageResourceCode(int languageResourceCode) {
            this.languageResourceCode = languageResourceCode;
        }

        public String getReleasedAt() {
            return releasedAt;
        }

        public void setReleasedAt(String releasedAt) {
            this.releasedAt = releasedAt;
        }

        public int getForceUpgrade() {
            return forceUpgrade;
        }

        public void setForceUpgrade(int forceUpgrade) {
            this.forceUpgrade = forceUpgrade;
        }

        public String getPlatformType() {
            return platformType;
        }

        public void setPlatformType(String platformType) {
            this.platformType = platformType;
        }

        public int getTabResourceCode() {
            return tabResourceCode;
        }

        public void setTabResourceCode(int tabResourceCode) {
            this.tabResourceCode = tabResourceCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTabResourceUrl() {
            return tabResourceUrl;
        }

        public void setTabResourceUrl(String tabResourceUrl) {
            this.tabResourceUrl = tabResourceUrl;
        }

        public String getVerName() {
            return verName;
        }

        public void setVerName(String verName) {
            this.verName = verName;
        }

        public String getLanguageResourceUrl() {
            return languageResourceUrl;
        }

        public void setLanguageResourceUrl(String languageResourceUrl) {
            this.languageResourceUrl = languageResourceUrl;
        }

        public int getVerCode() {
            return verCode;
        }

        public void setVerCode(int verCode) {
            this.verCode = verCode;
        }
    }
}
