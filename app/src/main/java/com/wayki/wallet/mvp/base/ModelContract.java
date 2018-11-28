package com.wayki.wallet.mvp.base;

import android.support.annotation.NonNull;

import com.wayki.wallet.bean.HistoryListBean;
import com.wayki.wallet.bean.Token2WiccBean;
import com.wayki.wallet.bean.TransferBean;
import com.wayki.wallet.bean.Wicc2TokenBean;

import io.reactivex.disposables.Disposable;

public interface ModelContract{

    /**
     * 所有耗时请求集中处理
     * */
   interface MainMode extends IBaseModel{

        void walletInfo( MvpCallBack listener);

        void bottomItem( MvpCallBack listener);
   }

    interface LaunchMode extends IBaseModel{
        void checkUpDate(MvpCallBack listener);
    }

    interface TransferMode extends IBaseModel{

        void transfer(TransferBean transferBean, MvpCallBack listener);
        void accountInfo(MvpCallBack listener);
        void getblockNumber(MvpCallBack listener);

    }

    interface HistoryMode extends IBaseModel{
        void getExhangeList(HistoryListBean listBean,MvpCallBack listener);
        void getTradeList(HistoryListBean listBean,MvpCallBack listener);
        void accountInfo(MvpCallBack listener);
        void tradeDatail(Long id,MvpCallBack listener);
    }

    interface WalletMode extends IBaseModel{
        void bindWallet(String hash,String address,MvpCallBack listener);
        void queryRewardWallet(MvpCallBack listener);
    }

    interface ExchangeMode extends IBaseModel{

        void getRate(MvpCallBack listener);

        void transfer(TransferBean transferBean,MvpCallBack listener);

        void getActivsatus(MvpCallBack listener);

        void getBlockNumber(MvpCallBack listener);

        void wicc2Token(Wicc2TokenBean wicc2TokenBean, MvpCallBack listener);

        void accountInfo(MvpCallBack listener);

        void token2wicc(Token2WiccBean token2WiccBean, MvpCallBack listener);

        void getContract(MvpCallBack listener);

        void getWicc(MvpCallBack listener);

    }


    //在IBaseModel基类上进行扩展,不修改基类本身
    interface MvpCallBack extends IBaseModel.Listener{
        void accept(@NonNull Disposable d);
        void onSubscribe(Disposable d);
        void success(Object str);
        void error(Object code);
        void onComplete();
    }

 }
