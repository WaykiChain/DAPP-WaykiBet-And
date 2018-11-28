package com.wayki.wallet.bean.entity;

public class GetWiccEntity {

    /**
     * code : 0
     * data : {"createdAt":"2018-10-24T12:42:49.989Z","id":0,"linkUrl":"string","onShelf":0,"title":"string","type":0,"updatedAt":"2018-10-24T12:42:49.989Z"}
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
         * createdAt : 2018-10-24T12:42:49.989Z
         * id : 0
         * linkUrl : string
         * onShelf : 0
         * title : string
         * type : 0
         * updatedAt : 2018-10-24T12:42:49.989Z
         */

        private String createdAt;
        private int id;
        private String linkUrl;
        private int onShelf;
        private String title;
        private int type;
        private String updatedAt;

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

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public int getOnShelf() {
            return onShelf;
        }

        public void setOnShelf(int onShelf) {
            this.onShelf = onShelf;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
