package com.wayki.wallet.mvp.base;

public interface IBaseModel<T> {
    interface Listener {
        void success(Object str);
        void error(Object code);
    }
}
