package com.wayki.wallet.mvp.mainpresenter;

import com.wayki.wallet.bean.entity.BottomBarEntity;
import com.wayki.wallet.mvp.base.BaseView;

public interface MainView<T> extends BaseView {
   void getDataSuccess(T t);
   void getDataFail(String fail);

   void genBottomItem(BottomBarEntity infoEntity);
}
