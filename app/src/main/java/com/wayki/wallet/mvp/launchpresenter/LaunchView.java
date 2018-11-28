package com.wayki.wallet.mvp.launchpresenter;

import com.wayki.wallet.mvp.base.BaseView;

public interface LaunchView<T> extends BaseView {
    void getDataSuccess(T t);
    void getDataFail(String fail);
}
