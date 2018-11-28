package com.wayki.wallet.mvp.historypresenter;

import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.TradeDetailEntity;
import com.wayki.wallet.mvp.base.BaseView;

public interface HistoryView<T> extends BaseView{
    void loadDataSuccess(T t);
    void loadDataFail(String failcode);
    void setAmount(AccountInfoEntity.DataBean dataBean);
    void tradeDetail(TradeDetailEntity detailEntity);
}
