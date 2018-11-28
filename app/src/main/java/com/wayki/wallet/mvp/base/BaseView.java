package com.wayki.wallet.mvp.base;

import android.support.annotation.IdRes;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface BaseView extends MvpView {
    void showLoadProgress();
    void hideLoadProgress();
    void showEmptyPager();
    void showLoadFail(String errorMsg, @IdRes int failId, int isVisible);
    void normalLoadFail();
    void showLoadSuccess();
    void showNoNetError();
    void setEmptyMsg(String msg);

    void loadingDialog(String tip);
    void dismissDialog();
    void showTip(String str);
}
