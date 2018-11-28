package com.wayki.wallet.mvp.historypresenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.HistoryListBean;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.ExchangeHistoryEntity;
import com.wayki.wallet.bean.entity.TradeDetailEntity;
import com.wayki.wallet.bean.entity.TradeHistoryEntity;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.ApiConstants;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.network.JsonConvert;
import com.wayki.wallet.utils.network.ObservableBody;

import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HistoryModelml implements ModelContract.HistoryMode {

    @Override
    public void getExhangeList(HistoryListBean listBean, final ModelContract.MvpCallBack listener) {

        String uuid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<ExchangeHistoryEntity>post(ApiConstants.exchanglist)//
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(listBean))
                .converter(new JsonConvert<ExchangeHistoryEntity>() {})
                .adapt(new ObservableBody<ExchangeHistoryEntity>())
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<ExchangeHistoryEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull ExchangeHistoryEntity bean) {
                        listener.success(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        listener.error(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }

    @Override
    public void getTradeList(HistoryListBean listBean, final ModelContract.MvpCallBack listener) {

        String uuid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<TradeHistoryEntity>post(ApiConstants.tradelist)//
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(listBean))
                .converter(new JsonConvert<TradeHistoryEntity>() {})
                .adapt(new ObservableBody<TradeHistoryEntity>())
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<TradeHistoryEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull TradeHistoryEntity bean) {
                        listener.success(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        listener.error(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }

    @Override
    public void accountInfo(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<AccountInfoEntity>get(ApiConstants.getaccountinfo)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<AccountInfoEntity>() {})//
                .adapt(new ObservableBody<AccountInfoEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccountInfoEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        listener.onSubscribe(disposable);
                    }

                    @Override
                    public void onNext(@NonNull AccountInfoEntity bean) {
                        listener.success(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        listener.error(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }

    @Override
    public void tradeDatail(Long id,final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<TradeDetailEntity>get(ApiConstants.tradedetail+id)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<TradeDetailEntity>() {})//
                .adapt(new ObservableBody<TradeDetailEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TradeDetailEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        listener.onSubscribe(disposable);
                    }

                    @Override
                    public void onNext(@NonNull TradeDetailEntity bean) {
                        listener.success(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        listener.error(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }
}
