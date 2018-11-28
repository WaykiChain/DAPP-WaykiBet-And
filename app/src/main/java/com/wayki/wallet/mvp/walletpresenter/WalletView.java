package com.wayki.wallet.mvp.walletpresenter;

import com.wayki.wallet.mvp.base.BaseView;

import java.util.List;

public interface WalletView extends BaseView {
    List<String>  getWords();
    String getPwd1();
    String getPwd2();
    String getPwd3();
    void getDataSuccess(Object obj);
    void getDataFail(Object fail);
    void clearText();
}
