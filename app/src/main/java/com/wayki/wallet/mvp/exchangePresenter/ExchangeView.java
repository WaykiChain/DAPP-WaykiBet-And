package com.wayki.wallet.mvp.exchangePresenter;

import com.wayki.wallet.bean.WalletBalanceBean;
import com.wayki.wallet.bean.entity.GetContractEntity;
import com.wayki.wallet.bean.entity.RateEntity;
import com.wayki.wallet.mvp.base.BaseView;

import java.util.ArrayList;

public interface ExchangeView extends BaseView {
    void setRateText(RateEntity.DataBean entity);
    void setActive(boolean isac);
    void setBalance(WalletBalanceBean balance);
    void setContract(ArrayList<GetContractEntity.DataBean> dataBeans);

    String getAmount1();
    String getAmount2();

    void dismissLoading();
}
