package com.wayki.wallet.bean.entity;

import java.util.List;

public class BottomBarEntity {


    /**
     * code : 0
     * msg : success
     * data : [{"onShelf":1,"selectImageUrlSecond":"https://sw91.net/bet/bet_selected@2x.png","redirectUrl":"http://bet.wayki.cn/guess/","imageUrlSecond":"https://sw91.net/bet/bet_normal@2x.png","displayOrder":1,"imageUrlThird":"bet_normal@3x.png","disableImageUrlThird":null,"title":"竞猜","type":100,"version":"1.0","createdAt":"2018-09-05 08:19:08","selectImageUrlThird":"https://sw91.net/bet/bet_selected@3x.png","disableImageUrlSecond":"","id":2,"updatedAt":"2018-10-16 03:29:37"},{"onShelf":1,"selectImageUrlSecond":"https://sw91.net/bet/wallet_selected@2x.png","redirectUrl":"","imageUrlSecond":"https://sw91.net/bet/wallet_normal@2x.png","displayOrder":2,"imageUrlThird":"https://sw91.net/bet/wallet_normal@3x.png","disableImageUrlThird":null,"title":"钱包","type":200,"version":"1.0","createdAt":"2018-09-05 08:19:14","selectImageUrlThird":"https://sw91.net/bet/wallet_selected@3x.png","disableImageUrlSecond":"","id":3,"updatedAt":"2018-10-16 03:32:16"},{"onShelf":1,"selectImageUrlSecond":"https://sw91.net/bet/me_selected@2x.png","redirectUrl":"http://bet.wayki.cn/mine/","imageUrlSecond":"https://sw91.net/bet/me_normal@2x.png","displayOrder":3,"imageUrlThird":"https://sw91.net/bet/me_normal@3x.png","disableImageUrlThird":null,"title":"我","type":100,"version":"1.0","createdAt":"2018-09-05 08:18:40","selectImageUrlThird":"https://sw91.net/bet/me_selected@3x.png","disableImageUrlSecond":"","id":1,"updatedAt":"2018-10-16 03:27:37"}]
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
         * onShelf : 1
         * selectImageUrlSecond : https://sw91.net/bet/bet_selected@2x.png
         * redirectUrl : http://bet.wayki.cn/guess/
         * imageUrlSecond : https://sw91.net/bet/bet_normal@2x.png
         * displayOrder : 1
         * imageUrlThird : bet_normal@3x.png
         * disableImageUrlThird : null
         * title : 竞猜
         * type : 100
         * version : 1.0
         * createdAt : 2018-09-05 08:19:08
         * selectImageUrlThird : https://sw91.net/bet/bet_selected@3x.png
         * disableImageUrlSecond :
         * id : 2
         * updatedAt : 2018-10-16 03:29:37
         */

        private int onShelf;
        private String selectImageUrlSecond;
        private String redirectUrl;
        private String imageUrlSecond;
        private int displayOrder;
        private String imageUrlThird;
        private Object disableImageUrlThird;
        private String title;
        private String type;
        private String version;
        private String createdAt;
        private String selectImageUrlThird;
        private String disableImageUrlSecond;
        private int id;
        private String updatedAt;

        public int getOnShelf() {
            return onShelf;
        }

        public void setOnShelf(int onShelf) {
            this.onShelf = onShelf;
        }

        public String getSelectImageUrlSecond() {
            return selectImageUrlSecond;
        }

        public void setSelectImageUrlSecond(String selectImageUrlSecond) {
            this.selectImageUrlSecond = selectImageUrlSecond;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getImageUrlSecond() {
            return imageUrlSecond;
        }

        public void setImageUrlSecond(String imageUrlSecond) {
            this.imageUrlSecond = imageUrlSecond;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getImageUrlThird() {
            return imageUrlThird;
        }

        public void setImageUrlThird(String imageUrlThird) {
            this.imageUrlThird = imageUrlThird;
        }

        public Object getDisableImageUrlThird() {
            return disableImageUrlThird;
        }

        public void setDisableImageUrlThird(Object disableImageUrlThird) {
            this.disableImageUrlThird = disableImageUrlThird;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getSelectImageUrlThird() {
            return selectImageUrlThird;
        }

        public void setSelectImageUrlThird(String selectImageUrlThird) {
            this.selectImageUrlThird = selectImageUrlThird;
        }

        public String getDisableImageUrlSecond() {
            return disableImageUrlSecond;
        }

        public void setDisableImageUrlSecond(String disableImageUrlSecond) {
            this.disableImageUrlSecond = disableImageUrlSecond;
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
