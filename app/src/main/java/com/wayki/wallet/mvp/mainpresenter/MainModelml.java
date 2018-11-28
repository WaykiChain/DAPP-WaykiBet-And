package com.wayki.wallet.mvp.mainpresenter;

import android.support.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.entity.BindInfoEntity;
import com.wayki.wallet.bean.entity.BottomBarEntity;
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

public class MainModelml implements ModelContract.MainMode{

    @Override
    public void walletInfo(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<BindInfoEntity>get(ApiConstants.bindinfo)//
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<BindInfoEntity>(){})//
                .adapt(new ObservableBody<BindInfoEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<BindInfoEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull BindInfoEntity bean) {
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
    public void bottomItem(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<BottomBarEntity>get(ApiConstants.bottom_item)//
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<BottomBarEntity>(){})//
                .adapt(new ObservableBody<BottomBarEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<BottomBarEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull BottomBarEntity bean) {
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
